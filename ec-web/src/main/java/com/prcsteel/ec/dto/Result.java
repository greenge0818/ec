package com.prcsteel.ec.dto;

import com.prcsteel.ec.core.enums.ResultMsgType;

import java.io.Serializable;

public class Result implements Serializable {

    private static final long serialVersionUID = 1L;
    private String code;
    private String status;
    private Object data;
    private ResultMsgType msgType;

    public Result() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Result(Object data) {
        this.data = data;
    }

    public Result(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Result(ResultMsgType msgType) {
        this.msgType = msgType;
    }

    public Result(String code, Object data, ResultMsgType msgType) {
        this.code = code;
        this.data = data;
        this.msgType = msgType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
