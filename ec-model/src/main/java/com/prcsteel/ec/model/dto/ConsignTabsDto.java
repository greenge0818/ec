package com.prcsteel.ec.model.dto;

/**
 * Created by myh on 2016/4/28.
 */
public class ConsignTabsDto {
    String code;      //状态code
    String msg;     //状态
    Integer count;   //角标

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
