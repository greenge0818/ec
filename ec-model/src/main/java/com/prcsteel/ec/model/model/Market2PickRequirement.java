package com.prcsteel.ec.model.model;

/**
 * @ClassName: Market2PickRequirement
 * @Description: 超市推送给分拣的需求信息封装对象
 * @Author Tiny
 * @Date 2016年05月27日
 */
public class Market2PickRequirement {
    /**
     * 需求单号
     */
    private String code;
    /**
     * 数据来源
     */
    private String source;
    /**
     * 客户电话
     */
    private String mobile;
    /**
     * 超市推送给分拣的需求单类型
     */
    private String type;

    public Market2PickRequirement() {

    }

    public Market2PickRequirement(String code, String source, String type, String mobile) {
        this.code = code;
        this.source = source;
        this.type = type;
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
