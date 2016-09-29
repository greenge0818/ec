package com.prcsteel.ec.core.enums;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

/**
 * Created by myh on 2016/5/4.
 */
public enum RequirementStatus {
    NEW("NEW", "新建（待确认）"),
    PICKED("PICKED", "分拣完成(待报价,报价中)"),
    QUOTED("QUOTED", "已报价"),
    FINISHED("FINISHED", "已完成"),
    CLOSED("CLOSED", "已关闭"),
    QUOTED_CLOSED("QUOTED_CLOSED","已报价已关闭"),
    ACTIVATED("ACTIVATED","已激活");

    private String code;
    private String msg;

    RequirementStatus(String code, String msg) {
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
        return Arrays.asList(RequirementStatus.values()).stream().filter(e -> code.equals(e.getCode())).findFirst().get().getMsg();
    }
}
