package com.hwz.tools;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 下载地址转码过滤器
 * 
 * @author Administrator
 */
public class CharacterEncodingFilter implements Filter {

    protected FilterConfig filterConfig = null;
    protected String       encoding     = "";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        this.encoding = filterConfig.getInitParameter("encoding");

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
                                                                                              ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String url = httpRequest.getRequestURI();
        if (url != null && !"".equals(url)) {
            url = java.net.URLDecoder.decode(url, "UTF-8");

            httpRequest.getRequestURL().append(url);
            httpRequest.getRequestDispatcher(url).forward(httpRequest, httpResponse);
            httpResponse.getOutputStream().flush();

        }
        if (encoding != null) {
            request.setCharacterEncoding(encoding);
            chain.doFilter(httpRequest, httpResponse);
            httpResponse.setCharacterEncoding(encoding);
        }

    }

    @Override
    public void destroy() {
        filterConfig = null;
        encoding = null;
    }

}
