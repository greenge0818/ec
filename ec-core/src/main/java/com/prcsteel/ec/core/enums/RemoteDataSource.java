package com.prcsteel.ec.core.enums;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

/**
 * Created by myh on 2016/5/4.
 */
public enum RemoteDataSource {
    APP("APP","APP"),
    WEB("WEB","超市"),
    PICK("PICK","分拣系统"),
    SMART("SMART","智能找货"),
    CBMS("CBMS","寄售管理系统");


    private String code;
    private String msg;

    RemoteDataSource(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 根据code获取信息
     *
     * @param code
     * @return
     */
    public static String getMsgByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return Arrays.asList(RemoteDataSource.values()).stream().filter(e -> code.equals(e.getCode())).findFirst().get().getMsg();
    }
}
