package com.prcsteel.ec.controller;

import com.prcsteel.ec.core.util.CookieUtil;
import com.prcsteel.ec.service.CommonService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by myh on 2016/4/28.
 */
@Component
public class BaseController {
    @Resource
    CommonService commonService;
    @Value("${loginUrl}")
    protected String loginUrl;
    @Value("${casServerUrlPrefix}")
    protected String casServer;
    @Value("${ut.switch}")
    private String ut;
    /**
     * 检查是否为新用户，如果是就给一个cookie
     * @param request
     * @param response
     */
    @ModelAttribute
    public void checkCookies(HttpServletRequest request, HttpServletResponse response, ModelMap out){
        CookieUtil.checkCookie(request, response);
        /** 当前登录用户信息 **/
        out.put("memberInfo", commonService.getCurrentUser());
        /** 登录url **/
        out.put("loginUrl", loginUrl);
        /** cas 路径 **/
        out.put("casUrl", casServer);
        /** 是否开启网站统计 **/
        out.put("ut", ut);
    }
}
