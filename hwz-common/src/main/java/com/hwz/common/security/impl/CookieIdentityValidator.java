/**
 * @(#)CookieIdentityValidatorImpl.java Copyright 2011 jointown, Inc. All rights reserved.
 */
package com.hwz.common.security.impl;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hwz.common.security.AuthenticationException;
import com.hwz.common.security.AuthenticationProvider;
import com.hwz.common.security.IdentityValidator;
import com.hwz.common.security.KeepLoginStatusVerifier;
import com.hwz.common.security.Principal;
import com.hwz.common.security.UUIDGenerator;
import com.hwz.common.security.Verifier;
import com.hwz.platform.YvanUtil;
import com.hwz.platform.springmvc.WebContext;

/**
 * description
 *
 * @author jianguo.xu
 * @version 1.0, 2011-2-17
 */
public abstract class CookieIdentityValidator implements IdentityValidator {

    private static final Log LOG = LogFactory.getLog(CookieIdentityValidator.class);

    public abstract void setAuthenticationProvider(AuthenticationProvider authenticationProvider);

    public abstract AuthenticationProvider getAuthenticationProvider();

    protected abstract String getPrincipalcookieName();

    /**
     * 是否重复登陆免检key
     *
     * @return
     */
    protected abstract String getImmune();

    protected abstract String getvisitorCookieName();

    protected abstract String getBizLastLoginTimeCookieName();

    /**
     * 是否是单一客户端登录，即一个账户同时只能有一个客户端登录<br/>
     * 如果是单一客户端登录那么采用帐号的最后登录时间戳作为sha摘要<br/>
     * 否则时间戳会保留在每个客户端保证每个客户端都可登录<br/>
     *
     * @return
     * @author xu.jianguo
     */
    protected abstract boolean singleClientLogin();

    /**
     * 多账户登录的最后时间戳cookie name
     *
     * @return
     * @author xu.jianguo
     */
    protected abstract String getLastLoginTimeCookieName();

    /**
     * 自动登录cookie name
     */
    public static final String  AUTO_LOGIN_COOKIE_NAME  = "auto_login";

    /**
     * 多账户登录的最后时间戳cookie name
     */
    // public static final String LAST_LOGIN_TIME="last_login_time";

    /**
     * 会话cookie 超时时间(以秒为单位)
     */
    private static final int    PRINCIPALCOOKIE_MAX_AGE = 30 * 60;
    /**
     * 持久cookie 超时时间(以秒为单位)
     */
    private static final int    VISITORCOOKIE_MAX_AGE   = 3600 * 24 * 30;
    private static final String SPLIT                   = "&";
    private static final String ENCODE                  = "utf-8";

    private String getSHAParam(Serializable id, Long lastLoginTime) {
        if (id == null || lastLoginTime == null) return "";
        return id.toString() + lastLoginTime.toString();
    }

    

    /**
     * 通过调用CookieUtils查找持久cookie，
     *
     * @return 找到返回true，否则返回false
     */
    public boolean isVisited() {
        return currentVisitor() != null;
    }
    
    /**
     * 通过调用CookieUtils查找持久cookie,
     *
     * @return 找到返回cookie中用户名，否则返回null
     */
    public String currentVisitor() {
        Object visitorObj = WebContext.currentRequest().getAttribute(getvisitorCookieName());
        if (visitorObj != null) return (String) visitorObj;
        try {
            Cookie cookie = YvanUtil.getCookie(getvisitorCookieName());
            if (cookie == null) return null;
            return URLDecoder.decode(cookie.getValue(), ENCODE);
        } catch (UnsupportedEncodingException e) {
            LOG.error(e.toString());
            return null;
        }
    }

    /**
     * 参照currentPrincipal方法的逻辑，如果用户已登录返回 true ,否则返回null
     *
     * @return
     */
    public boolean isLogined() {
        return currentPrincipal() != null;
    }

    /**
     * 通过调用CookieUtils查找会话cookie，<br>
     * 如果找到，取得cookie中id，调用验证提供者的get方法，得到用户，<br>
     * 再根据cookie摘要与生成摘要对比，如果一致再判断时间戳距离当前时间的间隔是否在合适范围，<br>
     * 如果是返回认证用户，否则返回null
     *
     * @return true 表示是已登录 false 表示没有登录
     */
    public Principal currentPrincipal() {
        Principal principal = getRequestPrincipal();
        if (principal != null) return principal;
        principal = getByPrincipalCookie();
        if (principal != null) return principal;
        // principal = getByAutoLogin();
        // if (principal != null) {
        // YvanUtil.writeCookie(createPrincipalCookie(principal));
        // WebContext.currentRequest().setAttribute(getPrincipalcookieName(), principal);
        // }
        return principal;
    }
    
    private Principal getRequestPrincipal() {
        HttpServletRequest request = WebContext.currentRequest();
        if (request == null) return null;
        return (Principal) request.getAttribute(getPrincipalcookieName());
    }

    /**
     * 通过调用CookieUtils查找会话cookie，<br>
     * 如果找到，取得cookie中id，调用验证提供者的get方法，得到用户，<br>
     * 再根据cookie摘要与生成摘要对比，如果一致再判断时间戳距离当前时间的间隔是否在合适范围，<br>
     * 如果是返回认证用户，否则返回null
     *
     * @return true 表示是已登录 false 表示没有登录
     */
    private Principal getByPrincipalCookie() {
        Cookie cookie = YvanUtil.getCookie(getPrincipalcookieName());
        if (cookie == null) return null;
        String cookieValue = cookie.getValue();
        // String[] values = cookieValue.split(SPLIT);
        // Serializable id = values[0];
        // String cookieSHAValue = values[1];
        String uuid = cookieValue;
        Principal principal = getAuthenticationProvider().get(uuid);
        Date lastRequestTime = getAuthenticationProvider().getLastRequestTime(uuid);
        if (principal == null) return null;
        Cookie immuneCookie = YvanUtil.getCookie(getImmune());
        if (immuneCookie != null) {
            String immune = immuneCookie.getValue();
            if (!StringUtils.isEmpty(immune) && immune.equals(getImmune())) {
                getAuthenticationProvider().setLastRequestTime(new Date(), principal.getIdentity(), uuid);
                return principal;
            }
        }

        Long lastLoginTime = getPrincipalLastLoginTime(principal);
        if (lastLoginTime == null) return null;
        if (lastRequestTime == null) return null;
        String shaParam = getSHAParam(principal.getIdentity(), lastLoginTime);
        String shaValue = YvanUtil.urlEncoding(DigestUtils.shaHex(shaParam));
        // if (!shaValue.equals(cookieSHAValue)) return null;

        getAuthenticationProvider().setLastRequestTime(new Date(), principal.getIdentity(), uuid);
        return principal;
    }

    private Long getPrincipalLastLoginTime(Principal principal) {
        if (singleClientLogin()) {
            return principal.getLastLoginTime();
        }
        Cookie cookie = YvanUtil.getCookie(getLastLoginTimeCookieName());
        if (cookie == null) return null;
        try {
            return Long.valueOf(cookie.getValue());
        } catch (NumberFormatException e) {
            LOG.error("last_login_time cookie format number error.", e);
        }
        return null;
    }

    /**
     * 根据保持登录状态的持久cookie得到认证用户
     *
     * @return
     * @author jianguo.xu
     */
    private Principal getByAutoLogin() {
        /*
         * Cookie cookie = YvanUtil.getCookie(AUTO_LOGIN_COOKIE_NAME); if (cookie == null) return null; String
         * encodingValue = cookie.getValue(); if (encodingValue == null) return null; String decodeValue =
         * YvanUtil.decodeBase64(encodingValue); try { Long id = new Long(decodeValue.split(SPLIT)[0]); Principal
         * pricipal = getAuthenticationProvider().get(id); if (pricipal == null) return null; if
         * (!pricipal.getLoginName().equals(decodeValue.split(SPLIT)[1])) return null; return pricipal; } catch
         * (Exception e) { LOG.error(e); return null; }
         */
        return null;
    }

    public void logout() {
        YvanUtil.removeCookie(getPrincipalcookieName(), "/");
        YvanUtil.removeCookie(getLastLoginTimeCookieName(), "/");
        YvanUtil.removeCookie(AUTO_LOGIN_COOKIE_NAME, "/");
        // CookieUtils.removeCookie(getvisitorCookieName(), "/");
        WebContext.currentRequest().setAttribute(getPrincipalcookieName(), null);
        WebContext.currentRequest().setAttribute(getvisitorCookieName(), null);
    }

    /**
     * 通过调用验证提供者进行登录，如果登录成功，将会话cookie和持久cookie写入客户端
     *
     * @param verifier 用户名
     * @return 返回登录后的认证主体
     * @throws AuthenticationException
     */
    public Principal login(Verifier verifier) throws AuthenticationException {
        Principal principal = getAuthenticationProvider().authenticate(verifier);
        if (principal == null) {
            throw new AuthenticationException("登录失败");
        }
        writeLoginCookie(principal, verifier);
        return principal;
    }

    public Principal login_auto(Verifier verifier) throws AuthenticationException {
        Principal principal = getAuthenticationProvider().authenticate_auto(verifier);
        if (principal == null) {
            throw new AuthenticationException("登录失败");
        }
        writeLoginCookie(principal, verifier);
        return principal;
    }

    /**
     * 通过调用验证提供者进行登录，如果登录成功，将会话cookie和持久cookie写入客户端
     *
     * @param verifier 用户名
     * @return 返回登录后的认证主体
     * @throws AuthenticationException
     */
    public Principal login(Verifier verifier, String ip) throws AuthenticationException {
        Principal principal = getAuthenticationProvider().authenticate(verifier, ip);
        if (principal == null) {
            throw new AuthenticationException("登录失败");
        }
        writeLoginCookie(principal, verifier);
        return principal;
    }

    private void writeLoginCookie(Principal principal, Verifier verifier) {
        if (principal.getBizLastLoginTime() != null) {
            YvanUtil.writeCookie(createBizLastLoginTime(principal.getBizLastLoginTime()));
        }
        String uuid = UUIDGenerator.getUUID();
        
        YvanUtil.writeCookie(createPrincipalCookie(uuid, principal));
        YvanUtil.writeCookie(createLastLoginTime(principal));

        if (principal.getLoginName() != null) {
            YvanUtil.writeCookie(createVisitorCookie(principal.getLoginName()));
        }

        // YvanUtil.writeCookie(createAutoLoginCookie(verifier, principal));
        WebContext.currentRequest().setAttribute(getPrincipalcookieName(), principal);

        if (principal.getLoginName() != null) {
            WebContext.currentRequest().setAttribute(getvisitorCookieName(), principal.getLoginName());
        }

        // getAuthenticationProvider().setLastRequestTime(new Date(), principal.getIdentity());
        getAuthenticationProvider().setLastRequestTime(new Date(), principal.getIdentity(), uuid);
    }
    
    /**
     * 通过调用CookieUtils和MD5Utils产生会话cookie
     *
     * @param principal 用户对象
     * @return 创建的cookie
     * @author zhangzaipeng
     */
    private Cookie createPrincipalCookie(String uuid, Principal principal) {
        StringBuffer cookValue = new StringBuffer();
        /*
         * Long lastLoginTime = principal.getLastLoginTime(); String shaParam = getSHAParam(principal.getIdentity(),
         * lastLoginTime); cookValue.append(principal.getIdentity().toString()); cookValue.append(SPLIT +
         * YvanUtil.urlEncoding(DigestUtils.shaHex(shaParam)));
         */
        cookValue.append(uuid);
        Cookie cookie = new Cookie(getPrincipalcookieName(), cookValue.toString());
        cookie.setPath("/");
        return cookie;
    }
    
    private Cookie createBizLastLoginTime(Long time) {
        if (!StringUtils.isEmpty(getBizLastLoginTimeCookieName())) {
            Cookie cookie = new Cookie(getBizLastLoginTimeCookieName(), time.toString());
            cookie.setPath("/");
            return cookie;
        }
        return null;
    }
    
    /**
     * 通过调用CookieUtils产生自动登录的持久cookie
     *
     * @param principal 用户对象
     * @return 创建的cookie
     * @author zhangzaipeng
     */
    private Cookie createAutoLoginCookie(Verifier verifier, Principal principal) {
        YvanUtil.removeCookie(getvisitorCookieName(), "/");
        if (!(verifier instanceof KeepLoginStatusVerifier)) return null;
        KeepLoginStatusVerifier vf = (KeepLoginStatusVerifier) verifier;
        if (!vf.isKeepLoginStatus() || vf.getKeepLoginMaxTime() == 0) return null;
        String value = principal.getIdentity().toString() + SPLIT + principal.getLoginName();
        Cookie cookie = new Cookie(AUTO_LOGIN_COOKIE_NAME, YvanUtil.urlEncoding(value));
        cookie.setMaxAge(vf.getKeepLoginMaxTime());
        cookie.setPath("/");
        return cookie;
    }

    /**
     * 通过调用CookieUtils产生持久cookie
     *
     * @param registerName 用户名
     * @return 创建的cookie
     */
    private Cookie createVisitorCookie(String registerName) {
        try {
            registerName = URLEncoder.encode(registerName, ENCODE);
            Cookie cookie = new Cookie(getvisitorCookieName(), registerName);
            cookie.setMaxAge(VISITORCOOKIE_MAX_AGE);
            cookie.setPath("/");
            return cookie;
        } catch (UnsupportedEncodingException e) {
            LOG.error(e);
            return null;
        }
    }

    /**
     * 通过调用CookieUtils和MD5Utils产生最后登录的会话cookie 如果是单一客户端登录的设置将不会被创建
     *
     * @param principal 用户对象
     * @return 创建的cookie
     * @author zhangzaipeng
     */
    private Cookie createLastLoginTime(Principal principal) {
        if (singleClientLogin()) return null;
        if (principal.getLastLoginTime() != null) {
            Cookie cookie = new Cookie(getLastLoginTimeCookieName(), principal.getLastLoginTime().toString());
            cookie.setPath("/");
            return cookie;
        } else {
            return null;
        }
    }

}
