package com.prcsteel.ec.model.domain.ec;


import com.prcsteel.ec.model.annotations.Column;
import com.prcsteel.ec.model.annotations.Entity;
import com.prcsteel.ec.model.annotations.KeyColumn;
import com.prcsteel.ec.model.common.DataEntity;

import java.util.Date;

/**
 * @author peanut
 * @description 短信验证码实体
 * @date 2016/4/28 15:36
 */
@Entity("base_sms_valid_code")
public class SmsValidCode extends DataEntity<SmsValidCode> {

    private static final long serialVersionUID = 1L;

    @KeyColumn(useGeneratedKeys = true)
    @Column(value = "id")
    private Integer id;

    @Column(value = "module")
    private String module;

    @Column(value = "mobile")
    private String mobile;

    @Column(value = "valid_code")
    private String validCode;

    @Column(value = "resend_time")
    private Date resendTime;

    @Column(value = "expire_time")
    private Date expireTime;

    public SmsValidCode() {

    }

    public SmsValidCode(String module, String mobile, String validCode, Date resendTime, Date expireTime) {
        this.module = module;
        this.mobile = mobile;
        this.validCode = validCode;
        this.resendTime = resendTime;
        this.expireTime = expireTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getValidCode() {
        return validCode;
    }

    public void setValidCode(String validCode) {
        this.validCode = validCode;
    }

    public Date getResendTime() {
        return resendTime;
    }

    public void setResendTime(Date resendTime) {
        this.resendTime = resendTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}
