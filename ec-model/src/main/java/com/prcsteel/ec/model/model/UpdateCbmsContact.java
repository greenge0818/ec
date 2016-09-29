package com.prcsteel.ec.model.model;

import java.io.Serializable;

/**
 * @ClassName: UpdateCbmsContact
 * @Description: 超市更新联系人信息到CBMS
 * @Author Tiny
 * @Date 2016年06年13日
 */
public class UpdateCbmsContact implements Serializable {
    private static final long serialVersionUID = 1L;

    //超市userId
    private Integer id;

    //手机号码
    private String mobile;

    public UpdateCbmsContact() {
    }

    public UpdateCbmsContact(Integer id, String mobile) {
        this.id = id;
        this.mobile = mobile;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
