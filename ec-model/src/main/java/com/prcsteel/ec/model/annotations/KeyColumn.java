package com.prcsteel.ec.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记该字段是主键
 * Created by Rolyer on 2016/4/27.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface KeyColumn {

    /**
     * 是否使用生成的主键,默认false
     *
     */
    boolean useGeneratedKeys() default false;
}
