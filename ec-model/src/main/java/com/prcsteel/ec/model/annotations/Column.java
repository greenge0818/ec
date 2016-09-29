package com.prcsteel.ec.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记该类的字段和数据库表字段的对应关系
 *
 * Created by Rolyer on 2016/4/27.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    /**
     * 字段名
     * @return
     */
    String value() default "";

    /**
     * 默认值
     * 注意:字符串注意带引号,示例 100,'abc', now()
     * @return
     */
    String insertIfNull() default "";
    /**
     * 默认值
     * 注意:字符串注意带引号,示例 100,'abc', now()
     * @return
     */
    String updateIfNull() default "";
}
