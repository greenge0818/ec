package com.prcsteel.ec.core.util;

import com.prcsteel.ec.core.model.Constant;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * 数字工具类
 *
 * @author peanut
 * @date 2016/5/25 17:42
 */
public class NumberUtil {

    /**
     * 金额格式化，保留小数位和处理千分位逗号
     * <p> 例：money=123456.98733----->123,456.99 </p>
     *
     * @param money 金额
     * @return 返回格式化后的金额数据
     * @author peanut
     * @date 2016/05/25
     */
    public static String moneyFormat(BigDecimal money) {
        if (money == null) return null;
        //默认两位
        return moneyFormat(money, Constant.MONEY_DIGIT);
    }

    /**
     * 金额格式化，保留小数位和处理千分位逗号
     * <p> 例：money=123456.98733,len=3----->123,456.987</p>
     *
     * @param money 金额
     * @param len   保留小数位数
     * @return 返回格式化后的金额数据
     * @author peanut
     * @date 2016/05/25
     */
    public static String moneyFormat(BigDecimal money, int len) {
        if (money == null) return null;
        return NumberFormat.getCurrencyInstance(Locale.CHINA).format(money.setScale(len, BigDecimal.ROUND_HALF_UP));
    }

    /**
     * 计算分页页数
     *
     * @param total    总数
     * @param pageSize 每页数量
     * @return 总页数
     * @author peanut
     * @date 2016/05/26
     */
    public static int pageCount(int total, int pageSize) {
        if (pageSize == 0) {
            throw new IllegalArgumentException("pageSize value could not be zero！");
        }
        return total % pageSize > 0 ? (total / pageSize) + 1 : total / pageSize;
    }
}
