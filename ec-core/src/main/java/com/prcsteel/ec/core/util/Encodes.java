package com.prcsteel.ec.core.util;

import java.util.Arrays;

/**
 * Created by Rolyer on 2016/4/26.
 */
public class Encodes {
    private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    /**
     *
     * 将10进制转化为62进制
     *
     * @param number 十进制数值
     * @param length 转化成的62进制后，字符长度不足length长度时高位补0，否则不改变什么。
     * @return
     */
    public static String convertDecimalBase62(Long number, int length){
        char[] res = new char[length];
        Arrays.fill(res, '0');

        for (int i = res.length-1; i >=0; i--) {
            res[i] = BASE62[(int) (number % BASE62.length)];
            number /= BASE62.length;
        }

        if(number > 0){
            throw new RuntimeException("code overflow");
        }

        return String.valueOf(res);
    }
}
