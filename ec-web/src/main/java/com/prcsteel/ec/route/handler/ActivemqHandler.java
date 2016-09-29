package com.prcsteel.ec.route.handler;

import com.google.gson.Gson;
import com.prcsteel.ec.core.enums.MqLogModual;
import com.prcsteel.ec.core.enums.RemoteDataSource;
import com.prcsteel.ec.core.exception.BusinessException;
import com.prcsteel.ec.model.domain.ec.MqLog;
import com.prcsteel.ec.model.model.CreateRequirementFromPick;
import com.prcsteel.ec.service.GenericDaoService;
import com.prcsteel.ec.service.RequirementService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Processor that fetches message from the JMS channel.
 * <p>
 * Created by Rolyer on 2016/5/23.
 */
public class ActivemqHandler implements Processor {
    private final Logger log = LoggerFactory.getLogger(ActivemqHandler.class);

    @Resource
    private RequirementService requirementService;

    @Resource
    private GenericDaoService genericDaoService;

    @Override
    public void process(Exchange exchange) throws Exception {
        log.info("Got exchange id={}", exchange.getExchangeId());
        String content = (String) exchange.getIn().getBody();
        MqLog mqLog = new MqLog(MqLogModual.ASS_REQUIREMENT.getModule(), RemoteDataSource.PICK.getCode(), content, "Y", null);
        if (StringUtils.isNotBlank(content)) {
            try {
                requirementService.createRequirementPick(new Gson().fromJson(content, CreateRequirementFromPick.class));
                genericDaoService.insert(mqLog);
            } catch (BusinessException be) {
                mqLog.setIsSuccess("N");
                mqLog.setErrorMsg(be.getMsg());
                genericDaoService.insert(mqLog);
            } catch (Exception e) {
                mqLog.setIsSuccess("N");
                mqLog.setErrorMsg("解析分拣单提供的新增需求单失败：" + e.getMessage());
                genericDaoService.insert(mqLog);
            }
        } else {
            mqLog.setContent("无");
            mqLog.setIsSuccess("N");
            mqLog.setErrorMsg("获取分拣单提供的新增需求单失败:changeId-" + exchange.getExchangeId());
            genericDaoService.insert(mqLog);
        }
    }
}
