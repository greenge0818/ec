package com.prcsteel.ec.model.dto;

import java.io.Serializable;

/**
 * Created by myh on 2015/11/16.
 */
public class SmartVo implements Serializable{
    private Object data;
    private String code;

    public SmartVo(String code, Object data) {
        this.data = data;
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
