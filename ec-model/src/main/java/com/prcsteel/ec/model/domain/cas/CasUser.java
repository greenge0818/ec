package com.prcsteel.ec.model.domain.cas;

import java.util.Date;

public class CasUser {
    private Integer id;

    private String account;

    private String password;

    private String dynamicPassword;

    private Date dynamicPasswordExpired;

    private String isActivated;

    private String isLocked;

    private String isDeleted;

    private Date created;

    private Date lastUpdated;

    public CasUser() {
    }

    public CasUser(String account, String password, String dynamicPassword, Date dynamicPasswordExpired, String isActivated, String isLocked, String isDeleted, Date created, Date lastUpdated) {
        this.account = account;
        this.password = password;
        this.dynamicPassword = dynamicPassword;
        this.dynamicPasswordExpired = dynamicPasswordExpired;
        this.isActivated = isActivated;
        this.isLocked = isLocked;
        this.isDeleted = isDeleted;
        this.created = created;
        this.lastUpdated = lastUpdated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDynamicPassword() {
        return dynamicPassword;
    }

    public void setDynamicPassword(String dynamicPassword) {
        this.dynamicPassword = dynamicPassword;
    }

    public Date getDynamicPasswordExpired() {
        return dynamicPasswordExpired;
    }

    public void setDynamicPasswordExpired(Date dynamicPasswordExpired) {
        this.dynamicPasswordExpired = dynamicPasswordExpired;
    }

    public String getIsActivated() {
        return isActivated;
    }

    public void setIsActivated(String isActivated) {
        this.isActivated = isActivated;
    }

    public String getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
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
}