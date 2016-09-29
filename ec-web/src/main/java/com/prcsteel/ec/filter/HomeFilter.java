package com.prcsteel.ec.filter;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 首页过滤器<br/>
 *
 * 去除CAS登录跳转时附带的参数<br/>
 * e.g.: http://localhost:8081/web/?ticket=ST-16-oQrAmO3JeKfagbuEpgDF-cas01.prcsteel.com --> http://localhost:8081/web/
 *
 * Created by Rolyer on 2016/8/4.
 */
public class HomeFilter extends HttpServlet implements Filter {
    @Override
    public void init(FilterConfig config) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        HttpServletResponse response  =(HttpServletResponse) servletResponse;
        String query = request.getQueryString();
        if (hasDirtyQueryString(query)) {
            response.sendRedirect(request.getRequestURI());
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private boolean hasDirtyQueryString(String query){
        if(StringUtils.contains(query, "ticket")){
            return true;
        }

        return false;
    }
}