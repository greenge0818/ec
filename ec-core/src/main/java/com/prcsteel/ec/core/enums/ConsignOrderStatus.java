package com.prcsteel.ec.core.enums;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

/**
 * Created by myh on 2016/5/11.
 * 采购管理---寄售单状态
 */
public enum ConsignOrderStatus {
    ALL("ALL", "全部"),
    TO_BE_RELATED("RELATED","待付款"),
    TO_BE_SECONDARY("SECONDSETTLE", "待结算"),
    FINISHED("FINISH", "交易完成"),
    CLOSED("CLOSED", "交易关闭");

    private String code;
    private String msg;

    ConsignOrderStatus(String code, String msg) {
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
        return Arrays.asList(ConsignOrderStatus.values()).stream().filter(e -> code.equals(e.getCode())).findFirst().get().getMsg();
    }
}
