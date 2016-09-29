package com.prcsteel.ec.model.model;

import java.io.Serializable;

/**
 * @Author:Green.Ge
 * @Description:用户外部系统修改需求单时传入的封装对象
 * @Date: 2016/5/6.
 */
public class RequirementStatusChanger implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;                //要修改的需求单单号
    private String statusTo;            //要设置成状态
    private String operateCode;     //调用方系统对应的单号
    private String operated;  //调用方系统对应的单号生成时间
    private String closeReason;         //关闭原因

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatusTo() {
        return statusTo;
    }

    public void setStatusTo(String statusTo) {
        this.statusTo = statusTo;
    }

    public String getOperateCode() {
        return operateCode;
    }

    public void setOperateCode(String operateCode) {
        this.operateCode = operateCode;
    }

    public String getOperated() {
        return operated;
    }

    public void setOperated(String operated) {
        this.operated = operated;
    }

    public String getCloseReason() {
        return closeReason;
    }

    public void setCloseReason(String closeReason) {
        this.closeReason = closeReason;
    }
}
