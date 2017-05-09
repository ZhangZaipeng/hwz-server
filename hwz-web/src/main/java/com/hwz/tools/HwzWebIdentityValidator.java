package com.hwz.tools;

import org.springframework.beans.factory.annotation.Autowired;

import com.hwz.common.security.AuthenticationProvider;
import com.hwz.common.security.impl.CookieIdentityValidator;

public class HwzWebIdentityValidator extends CookieIdentityValidator {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Override
    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public AuthenticationProvider getAuthenticationProvider() {
        return this.authenticationProvider;
    }

    @Override
    protected String getPrincipalcookieName() {
        return "hwz_PRINCIPAL";
    }

    @Override
    protected String getImmune() {
        return null;
    }

    @Override
    protected String getvisitorCookieName() {
        return "hwz_VISITOR";
    }

    @Override
    protected String getBizLastLoginTimeCookieName() {
        return "hwz_BIZ_LASTLOGIN";
    }

    @Override
    protected boolean singleClientLogin() {
        return false;
    }

    @Override
    protected String getLastLoginTimeCookieName() {
        return "hwz_LASTLOGIN";
    }
}
