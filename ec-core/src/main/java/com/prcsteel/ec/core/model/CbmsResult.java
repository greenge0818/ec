package com.prcsteel.ec.core.model;

import java.io.Serializable;

public class CbmsResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private String status;
    private String message;
    private Object data;
    private String total;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
