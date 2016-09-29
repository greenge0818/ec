package com.prcsteel.ec.core.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;

/**
 * 用户工具类
 * Created by Rolyer on 2016/4/26.
 */
public class UserUtils {

	/**
	 * 获取当前登录者对象
	 */
	public static String getPrincipal(){
		try{
			Object principal = SecurityUtils.getSubject().getPrincipal();
			if (principal != null){
				return (String) principal;
			}

		}catch (UnavailableSecurityManagerException e) {

		}catch (InvalidSessionException e){

		}

		return null;
	}
}
