package com.prcsteel.ec.job;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Rolyer on 2016/5/23.
 */
public abstract class BaseJob {

    /**
     * 执行内容
     */
    public abstract void execute();

    /**
     * 是否启用当前Job判断（满足针对特定Job需要单独启用/禁用）。
     * @return
     */
    public abstract boolean isEnabled();
}
