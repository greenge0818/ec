
    /**  
    * @Title: DateUtil.java
    * @Package com.prcsteel.ec.core.util
    * @Description: TODO(用一句话描述该文件做什么)
    * @author Green.Ge
    * @date 2016年4月27日
    * @version V1.0  
    */
    
package com.prcsteel.ec.core.util;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
    * @ClassName: DateUtil
    * @Description: 日期相关工具类
    * @Author Green.Ge
    * @Date 2016年4月27日
    *
    */

public class DateUtil {
	/**
     * 按照参数format的格式，日期转字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateToStr(Date date, String format) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } else {
            return "";
        }
    }

    /**
     * 时间字符串转Date对象
     * @param date
     * @return
     */
    public static Date strToDate(String date, String format){
        if(StringUtils.isBlank(date)){
            return null;
        }else{
            try{
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期
                sdf.setLenient(false);
                return sdf.parse(date);
            }catch (ParseException e){
                return null;
            }
        }
    }
}
