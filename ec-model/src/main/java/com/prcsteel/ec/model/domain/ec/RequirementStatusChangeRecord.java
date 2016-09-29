package com.prcsteel.ec.model.domain.ec;

import com.prcsteel.ec.model.annotations.Column;
import com.prcsteel.ec.model.annotations.Entity;
import com.prcsteel.ec.model.annotations.KeyColumn;
import com.prcsteel.ec.model.common.DataEntity;

import java.util.Date;

/**
 * 需求单状态变化记录表
 */
@Entity("busi_requirement_status_change_record")
public class RequirementStatusChangeRecord extends DataEntity {
    /**
     * id
     */
    @KeyColumn(useGeneratedKeys = true)
    @Column(value = "id")
    private Integer id;

    /**
     * 客户guid
     */
    @Column(value = "user_guid")
    private String userGuid;

    /**
     * 需求单guid
     */
    @Column(value = "requirement_guid")
    private String requirementGuid;

    /**
     * 需求单号
     */
    @Column(value = "requirement_code")
    private String requirementCode;

    /**
     * 外部系统单号(采购单、报价单)
     */
    @Column(value = "remote_order_code")
    private String remoteOrderCode;

    /**
     * 外部系统订单(采购单、报价单)创建时间
     */
    @Column(value = "remote_order_created")
    private Date remoteOrderCreated;

    /**
     * 原状态码：NEW -新建（待确认）  PICKED -分拣完成(待报价,报价中) - QUOTED --已报价  --FINISHED已完成  --CLOSED已关闭
     * RequirementStatus枚举
     */
    @Column(value = "origin_status")
    private String originStatus;

    /**
     * 新状态码：NEW -新建（待确认）  PICKED -分拣完成(待报价,报价中) - QUOTED --已报价  --FINISHED已完成  --CLOSED已关闭
     * RequirementStatus枚举
     */
    @Column(value = "change_to_status")
    private String changeToStatus;

    /**
     * 数据来源：APP，EC超市，PICK分拣系统，SMART智能找货，CBMS寄售管理系统
     * RemoteDataSource枚举
     */
    @Column(value = "source")
    private String source;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public String getRequirementGuid() {
        return requirementGuid;
    }

    public void setRequirementGuid(String requirementGuid) {
        this.requirementGuid = requirementGuid;
    }

    public String getRequirementCode() {
        return requirementCode;
    }

    public void setRequirementCode(String requirementCode) {
        this.requirementCode = requirementCode;
    }

    public String getRemoteOrderCode() {
        return remoteOrderCode;
    }

    public void setRemoteOrderCode(String remoteOrderCode) {
        this.remoteOrderCode = remoteOrderCode;
    }

    public Date getRemoteOrderCreated() {
        return remoteOrderCreated;
    }

    public void setRemoteOrderCreated(Date remoteOrderCreated) {
        this.remoteOrderCreated = remoteOrderCreated;
    }

    public String getOriginStatus() {
        return originStatus;
    }

    public void setOriginStatus(String originStatus) {
        this.originStatus = originStatus;
    }

    public String getChangeToStatus() {
        return changeToStatus;
    }

    public void setChangeToStatus(String changeToStatus) {
        this.changeToStatus = changeToStatus;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public RequirementStatusChangeRecord() {
    }

    public RequirementStatusChangeRecord(String userGuid, String requirementGuid, String requirementCode, String remoteOrderCode,
                                         Date remoteOrderCreated, String originStatus, String changeToStatus, String source) {
        this.userGuid = userGuid;
        this.requirementGuid = requirementGuid;
        this.requirementCode = requirementCode;
        this.remoteOrderCode = remoteOrderCode;
        this.remoteOrderCreated = remoteOrderCreated;
        this.originStatus = originStatus;
        this.changeToStatus = changeToStatus;
        this.source = source;
    }

    public RequirementStatusChangeRecord(String userGuid, String requirementGuid, String requirementCode, String remoteOrderCode,
                                         Date remoteOrderCreated, String originStatus, String changeToStatus, String source,
                                         String createdBy, String lastUpdateBy) {
        this.userGuid = userGuid;
        this.requirementGuid = requirementGuid;
        this.requirementCode = requirementCode;
        this.remoteOrderCode = remoteOrderCode;
        this.remoteOrderCreated = remoteOrderCreated;
        this.originStatus = originStatus;
        this.changeToStatus = changeToStatus;
        this.source = source;
        this.setCreatedBy(createdBy);
        this.setLastUpdatedBy(lastUpdateBy);
    }
}