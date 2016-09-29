package com.prcsteel.ec.route.handler;

import com.google.gson.Gson;
import com.prcsteel.ec.core.enums.MqLogModual;
import com.prcsteel.ec.core.enums.RemoteDataSource;
import com.prcsteel.ec.core.exception.BusinessException;
import com.prcsteel.ec.model.domain.ec.MqLog;
import com.prcsteel.ec.model.query.RequirementStatusChangerQuery;
import com.prcsteel.ec.service.GenericDaoService;
import com.prcsteel.ec.service.RequirementService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * @Author: Tiny
 * @Description: 分拣系统更新需求单状态
 * @Date: 2016年06月02日
 */
public class AssRequirementStatusHandler implements Processor {
    private final Logger log = LoggerFactory.getLogger(AssRequirementStatusHandler.class);

    @Resource
    private RequirementService requirementService;

    @Resource
    private GenericDaoService genericDaoService;

    @Override
    public void process(Exchange exchange) throws Exception {
        log.info("Got exchange id={}", exchange.getExchangeId());
        String content = (String) exchange.getIn().getBody();
        MqLog mqLog = new MqLog(MqLogModual.ASS_REQUIREMENT_STATUS.getModule(), RemoteDataSource.PICK.getCode(), content, "Y", null);
        if (StringUtils.isNotBlank(content)) {
            try {
                RequirementStatusChangerQuery pick = new Gson().fromJson(content, RequirementStatusChangerQuery.class);
                pick.setSource(RemoteDataSource.PICK.getCode());
                requirementService.updateRequirementByRemote(pick);
                genericDaoService.insert(mqLog);
            } catch (BusinessException be) {
                mqLog.setIsSuccess("N");
                mqLog.setErrorMsg(be.getMsg());
                genericDaoService.insert(mqLog);
            } catch (Exception e) {
                mqLog.setIsSuccess("N");
                mqLog.setErrorMsg("解析分拣单提供的更新需求单失败：" + e.getMessage());
                genericDaoService.insert(mqLog);
            }
        } else {
            mqLog.setContent("无");
            mqLog.setIsSuccess("N");
            mqLog.setErrorMsg("获取分拣单提供的更新需求单失败:changeId-" + exchange.getExchangeId());
            genericDaoService.insert(mqLog);
        }
    }
}
