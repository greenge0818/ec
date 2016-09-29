package com.prcsteel.ec.core.enums;

/**
 * @author peanut
 */
public enum OpType {
    None("无操作", OpLevel.Safe),
    USER_REGISTER("用户注册", OpLevel.Safe),
    ADD_REQUIREMENT("添加需求", OpLevel.Safe),
    USER_LOGIN("用户登陆", OpLevel.Safe),
    USER_MODIFY_PHONE("用户修改手机号", OpLevel.Warning),
    USER_MODIFY_PWD("用户修改密码", OpLevel.Safe),
    USER_RESET_PWD("用户重置密码", OpLevel.Safe),
    CBMS_USER_ADD("新增CBMS联系人信息", OpLevel.Safe),
    CBMS_USER_MODIFY_MOBILE("修改CBMS联系人手机", OpLevel.Warning),
    CBMS_USER_MODIFY_NAME("修改CBMS联系人名字", OpLevel.Safe),
    PICK_USER_ADD("新增分拣联系人信息", OpLevel.Safe);


    String description;
    OpLevel level;

    OpType(String desc, OpLevel level) {
        this.description = desc;
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public OpLevel getLevel() {
        return level;
    }
}
