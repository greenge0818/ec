package com.prcsteel.ec.core.util;

/**
 * Created by Rolyer on 2016/4/26.
 */
public class IdGen {

    private static final int DEFAULT_LENGTH = 10;
    private static final String DEFAULT_PREFIX = "GUID_";


    public static String idgen(Long number){
        return DEFAULT_PREFIX + Encodes.convertDecimalBase62(number, DEFAULT_LENGTH);
    }

    public static String idgenWithPrefix(Long number){
        return idgen(number);
    }


}
