package com.prcsteel.ec.core.util;

import com.prcsteel.ec.core.model.Constant;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Created by myh on 2016/4/28.
 */
public class CookieUtil {
    private static String createCookieId(){
        return UUID.randomUUID().toString();
    }

    /**
     * 检查是否为新用户，如果是就给一个cookie
     * @param request
     * @param response
     */
    public static void checkCookie(HttpServletRequest request, HttpServletResponse response){
        if(getCookieId(request) == null){
            Cookie newCookie = new Cookie(Constant.COOKIE_KEY, createCookieId());
            newCookie.setMaxAge(Constant.COOKIE_MAX_AGE);   //cookie时效
            newCookie.setPath("/");
            response.addCookie(newCookie);
        }
    }

    /**
     * 获取当前用户的cookieId
     * @param request
     * @return
     */
    public static String getCookieId(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (Constant.COOKIE_KEY.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
