package com.prcsteel.ec.model.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.prcsteel.ec.core.model.Constant;
import com.prcsteel.ec.core.util.IdGen;
import com.prcsteel.ec.core.util.UserUtils;
import com.prcsteel.ec.model.annotations.Column;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * Created by Rolyer on 2016/4/26.
 */
public abstract class DataEntity<T> extends BaseEntity<T> {
    private static final long serialVersionUID = 1L;

    @Column(value = "remark")
    private String remark;	        // 备注
    @Column(value = "created_by")
    private String createdBy;	    // 创建者
    @Column(value = "created")
    private Date created;	        // 创建日期
    @Column(value = "last_updated_by")
    private String lastUpdatedBy;	// 更新者
    @Column(value = "last_updated")
    private Date lastUpdated;	    // 更新日期
    @Column(value = "modification_number")
    private Integer modificationNumber; // 修改次数
    @Column(value = "is_deleted")
    private String isDeleted; 	    // 删除标记

    public DataEntity() {
        super();
    }

    public DataEntity(String uid) {
        super(uid);
    }

    /**
     * 插入之前执行方法，需要手动调用
     */
    @Override
    public void preInsert(Long id){
        if (!this.isNewRecord){
            setGuid(IdGen.idgenWithPrefix(id));
        }
        String user = UserUtils.getPrincipal();
        if (StringUtils.isNotBlank(user)){
            this.lastUpdatedBy = user;
            this.createdBy = user;
        }else{
            this.lastUpdatedBy = Constant.SYS_USER;
            this.createdBy = Constant.SYS_USER;
        }
        this.lastUpdated = new Date();
        this.created = this.lastUpdated;
        this.isDeleted = DEL_NO;
        this.modificationNumber=0;
    }

    /**
     * 更新之前执行方法，需要手动调用
     */
    @Override
    public void preUpdate(){
        String user = UserUtils.getPrincipal();
        if (StringUtils.isNotBlank(user)){
            this.lastUpdatedBy = user;
        }
        this.lastUpdated = new Date();
    }

    @Length(min=0, max=255)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @JsonIgnore
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @JsonIgnore
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @JsonIgnore
    @Length(min=1, max=1)
    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    @JsonIgnore
    public Integer getModificationNumber() {
        return modificationNumber;
    }

    public void setModificationNumber(Integer modificationNumber) {
        this.modificationNumber = modificationNumber;
    }
}
