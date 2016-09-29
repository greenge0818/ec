package com.prcsteel.ec.model.model;

/**
 * @ClassName: CreateRequirementFromPick
 * @Description: 分拣系统推送新增需求单时传入的封装对象
 * @Author Tiny
 * @Date 2016年05月26日
 */
public class CreateRequirementFromPick {
    /**
     * 需求单号
     */
    private String requirementCode;
    /**
     * 需求单生成时间
     */
    private String requirementCreated;
    /**
     * 客户手机号
     */
    private String userMobile;
    /**
     * 客户姓名
     */
    private String userName;
    /**
     * 客户所属公司
     */
    private String userAccount;
    /**
     * 分拣单号
     */
    private String pickCode;
    /**
     * 分拣单生成时间
     */
    private String pickCreated;

    public String getRequirementCode() {
        return requirementCode;
    }

    public void setRequirementCode(String requirementCode) {
        this.requirementCode = requirementCode;
    }

    public String getRequirementCreated() {
        return requirementCreated;
    }

    public void setRequirementCreated(String requirementCreated) {
        this.requirementCreated = requirementCreated;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getPickCode() {
        return pickCode;
    }

    public void setPickCode(String pickCode) {
        this.pickCode = pickCode;
    }

    public String getPickCreated() {
        return pickCreated;
    }

    public void setPickCreated(String pickCreated) {
        this.pickCreated = pickCreated;
    }
}
