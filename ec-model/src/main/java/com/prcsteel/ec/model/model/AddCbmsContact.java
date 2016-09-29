package com.prcsteel.ec.model.model;

import java.io.Serializable;

/**
 * @ClassName: AddCbmsContact
 * @Description: 超市推送联系人信息到CBMS
 * @Author Tiny
 * @Date 2016年06年13日
 */
public class AddCbmsContact implements Serializable {
    private static final long serialVersionUID = 1L;

    //超市userId
    private Integer id;

    //手机号码
    private String mobile;

    //名字
    private String name;

    //公司
    private String account;

    //来源
    private String source;

    public AddCbmsContact() {
    }

    public AddCbmsContact(Integer id, String mobile, String name, String account, String source) {
        this.id = id;
        this.mobile = mobile;
        this.name = name;
        this.account = account;
        this.source = source;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
