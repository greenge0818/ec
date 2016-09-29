package com.prcsteel.ec.model.domain.ec;

import com.prcsteel.ec.core.util.UserUtils;
import com.prcsteel.ec.model.annotations.Column;
import com.prcsteel.ec.model.annotations.Entity;
import com.prcsteel.ec.model.annotations.KeyColumn;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @ClassName: AppLoginLog
 * @Description: app登录日志
 * @Author Tiny
 * @Date 2016年08月24日
 */
@Entity("busi_app_login_log")
public class AppLoginLog {

    @KeyColumn(useGeneratedKeys = true)
    @Column(value = "id")
    private Integer id;

    @Column(value = "user_id")
    private Integer userId;

    @Column(value = "token")
    private String token;

    @Column(value = "created")
    private Date created;

    @Column(value = "last_updated")
    private Date lastUpdated;

    @Column(value = "modification_number")
    private Integer modificationNumber;

    public AppLoginLog() {
    }

    public AppLoginLog(Integer userId) {
        this.userId = userId;
    }

    public AppLoginLog(String token) {
        this.token = token;
    }

    public AppLoginLog(Integer userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    /**
     * 插入之前执行方法，需要手动调用
     */
    public void preInsert() {
        this.lastUpdated = new Date();
        this.created = this.lastUpdated;
        this.modificationNumber = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getModificationNumber() {
        return modificationNumber;
    }

    public void setModificationNumber(Integer modificationNumber) {
        this.modificationNumber = modificationNumber;
    }
}
