package com.prcsteel.ec.security;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by rolyer on 15-6-26.
 */
public class CustomCASRealm extends CasRealm {

    private final Logger log = LoggerFactory.getLogger(CustomCASRealm.class);

    /*
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 * @see org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection)
	 */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.debug("添加授权信息");

        Set<String> roleNames = new HashSet<>();
        Set<String> permissions = new HashSet<>();
        roleNames.add("user");
        permissions.add("user:view");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
        info.setStringPermissions(permissions);

        return info;
    }
}
