package com.prcsteel.ec.core.enums;

/**
 * @Author:Green.Ge
 * @Description:超市推送给分拣的需求单类型
 * @Date:2019-05-26
 */
public enum ASSRequirementType {

    RECEIPT("1", "回执需求"),
    IMAGE("2", "图片需求"),
    FORM("3", "表单需求"),
    MKT_FILE("4", "文本文件需求");

    private String code;
    private String name;

    ASSRequirementType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
