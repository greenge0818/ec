package com.prcsteel.ec.core.enums;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

/**
 * @author peanut
 * @description 各模块需要发送的短信内容
 * @date 2016/4/28 16:31
 */
public enum SMSTemplate {
    REGISTER("REGISTER", "您本次的验证码为：#code ，30分钟内输入有效（请勿泄露）"),
    DEFAULTPWD("DEFAULTPWD", "您本次注册产生的随机密码为：#code ！请您妥善保管，勿告他人，谢谢!"),
    LOGIN("LOGIN", "您本次的验证码为：#code ，30分钟内输入有效（请勿泄露）"),
    FORGOT_PASSWORD("FORGOT_PASSWORD","您本次的验证码为：#code ，30分钟内输入有效（请勿泄露）"),
    RESET_PASSWORD("RESET_PASSWORD","您本次的验证码为：#code ，30分钟内输入有效（请勿泄露）"),
    RESET_PHONE_OLD("RESET_PHONE_OLD","您本次的验证码为：#code ，30分钟内输入有效（请勿泄露）"),
    RESET_PHONE_NEW("RESET_PHONE_NEW","您本次的验证码为：#code ，30分钟内输入有效（请勿泄露）"),
    CBMS_USER_DYNAMIC_PWD("CBMS_USER_DYNAMIC_PWD", "您本次在CBMS注册产生的随机密码为：#code ！请您妥善保管，勿告他人，谢谢!"),
    PICK_USER_DYNAMIC_PWD("PICK_USER_DYNAMIC_PWD", "您本次在分拣系统注册产生的随机密码为：#code ！请您妥善保管，勿告他人，谢谢!");

    private String module;
    private String content;

    SMSTemplate(String module, String content) {
        this.module = module;
        this.content = content;
    }

    public String getModule() {
        return module;
    }

    public String getContent() {
        return content;
    }

    /**
     * 根据模块获取短信内容信息
     *
     * @param module
     * @return
     */
    public static String getContentByModule(String module) {
        if (StringUtils.isBlank(module)) {
            return null;
        }
        return Arrays.asList(SMSTemplate.values()).stream().filter(e -> module.equals(e.getModule())).findFirst().get().getContent();
    }
}
