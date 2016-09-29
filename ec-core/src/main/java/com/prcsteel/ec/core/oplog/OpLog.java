package com.prcsteel.ec.core.oplog;


import com.prcsteel.ec.core.enums.OpType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author peanut
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OpLog {
	/**
	 * 操作类型
	 */
	OpType value();
}
