package com.prcsteel.ec.core.model;

import java.io.Serializable;

public class TabCountResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private String ALL = "0";
    private String RELATED = "0";
    private String SECONDSETTLE = "0";
    private String FINISH = "0";
    private String CLOSED = "0";

    public Integer getALL() {
        return Double.valueOf(ALL).intValue();
    }

    public void setALL(String ALL) {
        this.ALL = ALL;
    }

    public Integer getRELATED() {
        return Double.valueOf(RELATED).intValue();
    }

    public Integer getSECONDSETTLE() {
        return Double.valueOf(SECONDSETTLE).intValue();
    }

    public Integer getFINISH() {
        return Double.valueOf(FINISH).intValue();
    }

    public void setRELATED(String RELATED) {
        this.RELATED = RELATED;
    }

    public void setSECONDSETTLE(String SECONDSETTLE) {
        this.SECONDSETTLE = SECONDSETTLE;
    }

    public void setFINISH(String FINISH) {
        this.FINISH = FINISH;
    }

    public Integer getCLOSED() {
        return Double.valueOf(CLOSED).intValue();
    }

    public void setCLOSED(String CLOSED) {
        this.CLOSED = CLOSED;
    }
}
