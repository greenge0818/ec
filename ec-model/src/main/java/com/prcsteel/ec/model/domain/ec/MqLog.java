package com.prcsteel.ec.model.domain.ec;

import com.prcsteel.ec.model.annotations.Column;
import com.prcsteel.ec.model.annotations.Entity;
import com.prcsteel.ec.model.annotations.KeyColumn;

import java.util.Date;

/**
 * @ClassName:
 * @Description:
 * @Author Tiny
 * @Date
 */
@Entity("busi_mq_log")
public class MqLog {

    @KeyColumn(useGeneratedKeys = true)
    @Column(value = "id")
    private Integer id;

    @Column(value = "modual")
    private String modual;

    @Column(value = "remote_sys")
    private String remoteSys;

    @Column(value = "content")
    private String content;

    @Column(value = "is_success")
    private String isSuccess;

    @Column(value = "error_msg")
    private String errorMsg;

    @Column(value = "created")
    private Date created;

    public MqLog() {
    }

    public MqLog(String modual, String remoteSys, String content, String isSuccess, String errorMsg) {
        this.modual = modual;
        this.remoteSys = remoteSys;
        this.content = content;
        this.isSuccess = isSuccess;
        this.errorMsg = errorMsg;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModual() {
        return modual;
    }

    public void setModual(String modual) {
        this.modual = modual;
    }

    public String getRemoteSys() {
        return remoteSys;
    }

    public void setRemoteSys(String remoteSys) {
        this.remoteSys = remoteSys;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
