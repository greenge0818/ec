package com.prcsteel.ec.model.domain.ec;

import com.prcsteel.ec.model.annotations.Column;
import com.prcsteel.ec.model.annotations.Entity;
import com.prcsteel.ec.model.annotations.KeyColumn;
import com.prcsteel.ec.model.common.DataEntity;

import java.util.Date;

/**
 * @ClassName: Requirement
 * @Description: 需求单
 * @Author Tiny
 * @Date 2016年4月28日
 */
@Entity("busi_requirement")
public class Requirement extends DataEntity<Requirement> {

    @KeyColumn(useGeneratedKeys = true)
    @Column(value = "id")
    private Integer id;

    @Column(value = "code")
    private String code;

    @Column(value = "user_guid")
    private String userGuid;

    @Column(value = "request")
    private String request;

    @Column(value = "file_url")
    private String fileUrl;

    @Column(value = "source")
    private String source;

    @Column(value = "type")
    private String type;

    @Column(value = "stage_status")
    private String stageStatus;

    @Column(value = "close_stage")
    private String closeStage;

    @Column(value = "close_reason")
    private String closeReason;

    public Requirement() {
    }

    public Requirement(String code) {
        this.code = code;
    }

    public Requirement(String code, String userGuid, String source, String type, String stageStatus,
                       String createdBy, String laseCreatedBy) {
        this.code = code;
        this.userGuid = userGuid;
        this.source = source;
        this.type = type;
        this.stageStatus = stageStatus;
        this.setCreatedBy(createdBy);
        this.setLastUpdatedBy(laseCreatedBy);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
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

    public String getCloseStage() {
        return closeStage;
    }

    public void setCloseStage(String closeStage) {
        this.closeStage = closeStage;
    }

    public String getCloseReason() {
        return closeReason;
    }

    public void setCloseReason(String closeReason) {
        this.closeReason = closeReason;
    }
}
