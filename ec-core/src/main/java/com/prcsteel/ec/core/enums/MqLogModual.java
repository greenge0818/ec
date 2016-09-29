package com.prcsteel.ec.core.enums;

/**
 * @ClassName: MqLogModual
 * @Description: mq记录操作模块
 * @Author Tiny
 * @Date 2016年07月13日
 */
public enum MqLogModual {
    ADD_CBMS_CONTACT("ADD_CBMS_CONTACT","超市新增用户推送到cbms"),
    UPDATE_CBMS_CONTACT("UPDATE_CBMS_CONTACT","超市更新用户推送到cbms"),
    ADD_MARKET_USER_ID("ADD_MARKET_USER_ID","超市推送新增联系人的超市userId给CBMS"),
    ADD_MARKET_USER("ADD_MARKET_USER","cbms新增用户推送到超市"),
    UPDATE_MARKET_USER("UPDATE_MARKET_USER","cbms更新用户推送到超市"),
    CHANGE_CONTACT_STATUS("CHANGE_CONTACT_STATUS","CBMS禁用/启用联系人"),
    MARKET_REQUIREMENT("MARKET_REQUIREMENT","超市推送需求单到分检"),
    ASS_REQUIREMENT("ASS_REQUIREMENT","分检推送需求单到超市"),
    ASS_REQUIREMENT_STATUS("ASS_REQUIREMENT_STATUS","分拣更新超市需求单状态"),
    SM_REQUIREMENT_STATUS("SM_REQUIREMENT_STATUS","找货更新超市需求单状态"),
    CBMS_REQUIREMENT_STATUS("CBMS_REQUIREMENT_STATUS","CBMS更新超市需求单状态");

    private String module;
    private String content;

    MqLogModual(String module, String content) {
        this.module = module;
        this.content = content;
    }

    public String getModule() {
        return module;
    }

    public String getContent() {
        return content;
    }
}
