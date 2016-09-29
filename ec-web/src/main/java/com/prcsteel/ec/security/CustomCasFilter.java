package com.prcsteel.ec.security;

import com.prcsteel.ec.core.util.CookieUtil;
import com.prcsteel.ec.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.cas.CasToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 自定义CAS Filter，用于用户登录认证
 * Created by Rolyer on 2016/7/29.
 */
public class CustomCasFilter extends AuthenticatingFilter {
    private UserService userService;

    private static Logger logger = LoggerFactory.getLogger(CustomCasFilter.class);
    private static final String TICKET_PARAMETER = "ticket";
    private String failureUrl;

    public CustomCasFilter() {
    }

    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        String ticket = httpRequest.getParameter("ticket");
        return new CasToken(ticket);
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return this.executeLogin(request, response);
    }

    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        mergeCart(request); //合并购物车
        this.issueSuccessRedirect(request, response);
        return false;
    }

    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException ae, ServletRequest request, ServletResponse response) {
        Subject subject = this.getSubject(request, response);
        if(!subject.isAuthenticated() && !subject.isRemembered()) {
            try {
                WebUtils.issueRedirect(request, response, this.failureUrl);
            } catch (IOException var7) {
                logger.error("Cannot redirect to failure url : {}", this.failureUrl, var7);
            }
        } else {
            try {
                this.issueSuccessRedirect(request, response);
            } catch (Exception var8) {
                logger.error("Cannot redirect to the default success url", var8);
            }
        }

        return false;
    }

    public void setFailureUrl(String failureUrl) {
        this.failureUrl = failureUrl;
    }

    /**
     * （登录成功后）合并购物车
     * @param request
     */
    private void mergeCart(ServletRequest request){
        HttpServletRequest req = (HttpServletRequest) request;
        String cookieId = CookieUtil.getCookieId(req);
        logger.debug("get cookie Id: "+cookieId);

        userService = getUserService(req);
        if(userService!=null) {
            userService.afterLogin(CookieUtil.getCookieId(req));
        } else {
            logger.warn("Cart data merge action has be canceled.");
        }
    }

    /**
     * 获取UserService接口
     * @param req
     * @return
     */
    private UserService getUserService(HttpServletRequest req){
        ServletContext sc = req.getSession().getServletContext();
        XmlWebApplicationContext cxt = (XmlWebApplicationContext) WebApplicationContextUtils.getWebApplicationContext(sc);

        if(userService == null) {
            if(cxt != null && cxt.getBean("userService") != null){
                return userService = (UserService) cxt.getBean("userService");
            } else {
                logger.error("The 'userService' bean cannot be found.");
            }
        }
        return userService;
    }
}

