package com.prcsteel.ec.core.enums;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

/**
 * @ClassName: RequirementCloseStage
 * @Description: 需求关闭阶段枚举
 * @Author Tiny
 * @Date 2016年05月05日
 */
public enum RequirementCloseStage {
    PICKUP("PICKUP","分拣阶段"),
    INQUIRY("INQUIRY","询价阶段"),
    QUOTED("QUOTED","报价阶段"),
    BILL("BILL","订单阶段");


    private String code;
    private String msg;

    RequirementCloseStage(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 根据code获取信息
     *
     * @param code
     * @return
     */
    public static String getMsgByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return Arrays.asList(RequirementCloseStage.values()).stream().filter(e -> code.equals(e.getCode())).findFirst().get().getMsg();
    }
}
