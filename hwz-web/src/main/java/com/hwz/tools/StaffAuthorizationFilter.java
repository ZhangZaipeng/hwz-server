package com.hwz.tools;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hwz.common.security.IdentityValidator;
import com.hwz.common.security.Principal;
import com.hwz.platform.springmvc.WebApplicationContextUtils;
import com.hwz.user.domain.StaffAgent;
import com.mysql.jdbc.StringUtils;

/**
 * 用户认证过滤器
 */
public class StaffAuthorizationFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(StaffAuthorizationFilter.class);

    private String[]            excludeUrls;

    private ServletContext      ctx;

    private IdentityValidator   identityValidator;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
                                                                                              ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String thisUrl = httpRequest.getRequestURI();
        
        // 首先验证cookie中数据 uuid principal 不为空 直接跳转到主页
        Principal principal = identityValidator.currentPrincipal();
        if (thisUrl.endsWith("login.htm")) {
            if (principal != null && principal instanceof StaffAgent) {
            	httpResponse.sendRedirect("/staff/main.htm");
            }
        }
        
        // url 后缀不是   htm / 放行
        if ((!thisUrl.endsWith(".htm") && !thisUrl.endsWith("/"))) { 
            chain.doFilter(httpRequest, httpResponse);
            return;
        }
        
        // url 包含在excludeUrls 中的   放行
        if (exclude(thisUrl)) {	
        	chain.doFilter(httpRequest, httpResponse);
            return;
        }
        
        if (principal != null && principal instanceof StaffAgent) {
            chain.doFilter(httpRequest, httpResponse);
            return;
        }

        httpResponse.sendRedirect("/staff/login.htm");
    }

    private boolean exclude(String url) {
        url = url.toLowerCase();
        if (excludeUrls != null) {
            for (int i = 0; i < excludeUrls.length; i++)
                if (url.equals(excludeUrls[i])) return true;
        }
        return false;
    }
    
    @Override
    public void init(FilterConfig config) throws ServletException {

        ctx = config.getServletContext();
        identityValidator = WebApplicationContextUtils.getService(HwzWebIdentityValidator.class, ctx);
        if (config.getInitParameter("excludeUrls") == null) return;
        String urlString = config.getInitParameter("excludeUrls").toLowerCase().trim();
        if (!StringUtils.isNullOrEmpty(urlString)) {
            excludeUrls = urlString.split(",");
        }

    }

    @Override
    public void destroy() {

    }
}

