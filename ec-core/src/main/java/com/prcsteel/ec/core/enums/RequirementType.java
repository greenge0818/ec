package com.prcsteel.ec.core.enums;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

/**
 * @ClassName: RequirementType
 * @Description: 需求类型
 * @Author Tiny
 * @Date 2016年05月04日
 */
public enum RequirementType {
    HELP("HELP","帮您找"),
    CART("CART", "购物车"),
    RECEIPT("RECEIPT", "回执"),
    ONEMORE("ONEMORE", "再来一单"),
    IMAGE("IMAGE", "图片"),
    MARKETANALYSIS("MARKETANALYSIS", "行情分析");

    private String code;
    private String msg;

    RequirementType(String code, String msg) {
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
        return Arrays.asList(RequirementType.values()).stream().filter(e -> code.equals(e.getCode())).findFirst().get().getMsg();
    }

}
