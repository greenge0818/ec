package com.prcsteel.ec.model.dto;

import java.util.Date;
import java.util.List;

/**
 * @Author:Green.Ge
 * @Description:返回给分拣系统的需求单bean
 * @Date:2016/5/6.
 */
public class RequirementForPickDto {
    private String fileUrl;
    private String userGUID;
    private String code;
    private String source;
    private String requestContent;
    private String mobile;
    private String requestType;
    private String createTime;
    private List<RequirementItemForPickDto> items;

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getUserGUID() {
        return userGUID;
    }

    public void setUserGUID(String userGUID) {
        this.userGUID = userGUID;
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

    public String getRequestContent() {
        return requestContent;
    }

    public void setRequestContent(String requestContent) {
        this.requestContent = requestContent;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<RequirementItemForPickDto> getItems() {
        return items;
    }

    public void setItems(List<RequirementItemForPickDto> items) {
        this.items = items;
    }
}
