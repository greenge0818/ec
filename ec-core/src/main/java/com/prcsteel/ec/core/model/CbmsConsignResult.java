package com.prcsteel.ec.core.model;

import java.io.Serializable;
public class CbmsConsignResult implements Serializable {
   private static final long serialVersionUID = 1L;
   private String total;
   private Object list;

    public Integer getTotal() {
        return Double.valueOf(total).intValue();
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Object getList() {
        return list;
    }

    public void setList(Object list) {
        this.list = list;
    }
}
