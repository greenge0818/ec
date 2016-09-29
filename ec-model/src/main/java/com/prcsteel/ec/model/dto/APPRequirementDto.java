package com.prcsteel.ec.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: RequirementDto
 * @Author Tiny
 * @Date 2016年05月04日
 */
public class APPRequirementDto implements Cloneable, Serializable {
    /**
     * 需求Guid
     */
    private String guid;
    /**
     * 需求单号
     */
    private String code;
    /**
     * 我的要求
     */
    private String request;
    /**
     * 采购清单文件地址
     */
    private String fileUrl;
    /**
     * 需求来源
     */
    private String source;
    /**
     * 类型
     */
    private String type;
    /**
     * 状态
     */
    private String stageStatus;
    /**
     * 关闭阶段
     */
    private String closeStage;
    /**
     * 关闭原因
     */
    private String closeReason;
    /**
     * 交货地
     */
    private String city;
    /**
     * 创建时间
     */
    private Date created;
    /**
     * 操作人
     */
    private String operator;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 订单金额（已报价之后才会有）
     */
    private BigDecimal amount;
    /**
     *
     */
    private String requestType;
    /**
     * 需求明细列表
     */
    private List<RequirementItemDto> requirementItemList;

    //模拟数据需要用字段，之后会删掉
    private String remoteCode;

    public APPRequirementDto() {
    }

    //询价单数据构造函数
    public APPRequirementDto(String code, Date created, String source, String city, String operator, String mobile, String stageStatus, String type) {
        this.code = code;
        this.created = created;
        this.source = source;
        this.city = city;
        this.operator = operator;
        this.mobile = mobile;
        this.stageStatus = stageStatus;
        this.type = type;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
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

    public String getStageStatus() {
        return stageStatus;
    }

    public void setStageStatus(String stageStatus) {
        this.stageStatus = stageStatus;
    }

    public String getCloseReason() {
        return closeReason;
    }

    public void setCloseReason(String closeReason) {
        this.closeReason = closeReason;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<RequirementItemDto> getRequirementItemList() {
        return requirementItemList;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public void setRequirementItemList(List<RequirementItemDto> requirementItemList) {
        this.requirementItemList = requirementItemList;
    }

    public String getCloseStage() {
        return closeStage;
    }

    public void setCloseStage(String closeStage) {
        this.closeStage = closeStage;
    }

    public String getRemoteCode() {
        return remoteCode;
    }

    public void setRemoteCode(String remoteCode) {
        this.remoteCode = remoteCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public APPRequirementDto clone() {
        try {
            return (APPRequirementDto) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
