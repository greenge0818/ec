package com.prcsteel.ec.core.model;

import java.io.Serializable;

public class RestResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private String code;
    private String status;
    private Object data;

    public RestResult() {
    }

    public RestResult(String status, Object data) {
        this.status = status;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
