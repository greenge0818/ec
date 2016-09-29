package com.prcsteel.ec.model.dto;

import com.prcsteel.ec.core.model.Constant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.function.ObjDoubleConsumer;

/**
 * @ClassName: RequirementDto
 * @Author Tiny
 * @Date 2016年05月04日
 */
public class RequirementDto implements Cloneable, Serializable {
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
     * 创建时间字符串
     */
    private String createdStr;

    public String getCreatedStr() {
        return Constant.SDF.format(this.created);
    }

    public void setCreatedStr(String createdStr) {
        this.createdStr = createdStr;
    }

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
     * id(行情中心使用)
     */
    private Integer id;
    /**
     * 标题(行情中心使用)
     */
    private String title;
    /**
     * 作者(行情中心使用)
     */
    private String author;
    /**
     * 正文(行情中心使用)
     */
    private String text;
    /**
     * 链接(行情中心使用)
     */
    private String url;
    /**
     * 缩略图(行情中心使用)
     */
    private String thumbnail;
    /**
     *
     */
    private String requestType;
    /**
     * 需求明细列表
     */
    private List<RequirementItemDto> items;

    //模拟数据需要用字段，之后会删掉
    private String remoteCode;

    public RequirementDto() {
    }

    //行情中心数据构造函数
    public RequirementDto(String thumbnail, String url, String author, String title, String text, Date created, String stageStatus, String type) {
        this.thumbnail = thumbnail;
        this.url = url;
        this.author = author;
        this.title = title;
        this.text = text;
        this.created = created;
        this.stageStatus = stageStatus;
        this.type = type;
    }

    //询价单数据构造函数
    public RequirementDto(String code, Date created, String source, String city, String operator, String mobile, String stageStatus, String type) {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public List<RequirementItemDto> getItems() {
        return items;
    }

    public void setItems(List<RequirementItemDto> items) {
        this.items = items;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public RequirementDto clone() {
        try {
            return (RequirementDto) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
