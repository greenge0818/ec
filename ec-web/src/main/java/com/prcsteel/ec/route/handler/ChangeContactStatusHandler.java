package com.prcsteel.ec.route.handler;

import com.google.gson.Gson;
import com.prcsteel.ec.core.enums.MqLogModual;
import com.prcsteel.ec.core.enums.RemoteDataSource;
import com.prcsteel.ec.core.exception.BusinessException;
import com.prcsteel.ec.model.domain.ec.MqLog;
import com.prcsteel.ec.model.dto.ChangeContactStatusDto;
import com.prcsteel.ec.service.GenericDaoService;
import com.prcsteel.ec.service.UserService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * @ClassName: ChangeContactStatusHandler
 * @Description: CBMS禁用/启用联系人
 * @Author Tiny
 * @Date 2016年07月01日
 */
public class ChangeContactStatusHandler implements Processor {
    private final Logger log = LoggerFactory.getLogger(AssRequirementStatusHandler.class);

    @Resource
    private UserService userService;

    @Resource
    private GenericDaoService genericDaoService;

    @Override
    public void process(Exchange exchange) throws Exception {
        log.info("Got exchange id={}", exchange.getExchangeId());
        String content = (String) exchange.getIn().getBody();
        MqLog mqLog = new MqLog(MqLogModual.CHANGE_CONTACT_STATUS.getModule(), RemoteDataSource.CBMS.getCode(), content, "Y", null);
        if (StringUtils.isNotBlank(content)) {
            try {
                ChangeContactStatusDto changeContactStatusDto = new Gson().fromJson(content, ChangeContactStatusDto.class);
                userService.changeContactStatus(changeContactStatusDto);
                genericDaoService.insert(mqLog);
            } catch (BusinessException be) {
                mqLog.setIsSuccess("N");
                mqLog.setErrorMsg(be.getMsg());
                genericDaoService.insert(mqLog);
            } catch (Exception e) {
                mqLog.setIsSuccess("N");
                mqLog.setErrorMsg("解析CBMS禁用/启用联系人失败：" + e.getMessage());
                genericDaoService.insert(mqLog);
            }
        } else {
            mqLog.setContent("无");
            mqLog.setIsSuccess("N");
            mqLog.setErrorMsg("获取CBMS禁用/启用联系人失败:changeId-" + exchange.getExchangeId());
            genericDaoService.insert(mqLog);
        }
    }
}
