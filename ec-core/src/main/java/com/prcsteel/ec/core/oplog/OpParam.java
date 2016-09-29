package com.prcsteel.ec.core.oplog;

import java.lang.annotation.*;

/**
 * @author peanut
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(OpParamGroup.class)
public @interface OpParam {

	/**
	 * 参数存储到OpLog中的名称
	 * 如果index与name都没有指定，则通过该参数查找参数的值
	 */
	String value();
	
	/**
	 * 从方法中获取值的参数的索引
	 * @return
	 */
	int index() default -1;
	
	/**
	 * 从方法中获取值的参数的名称
	 * @return
	 */
	String name() default "";
	/**
	 * 参数的默认值
	 */
	String defaultValue() default "";
}
