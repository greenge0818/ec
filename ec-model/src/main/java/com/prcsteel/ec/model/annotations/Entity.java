package com.prcsteel.ec.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记该类对应的数据库 的 库名 和 表名
 *
 * Created by Rolyer on 2016/4/27.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {
    /**
     * 数据库库名
     * @return
     */
    String database() default "";
    /**
     * 数据库表名
     * @return
     */
    String value();
}
