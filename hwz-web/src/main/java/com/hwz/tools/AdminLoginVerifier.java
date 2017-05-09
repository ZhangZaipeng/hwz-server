package com.hwz.tools;

import com.hwz.common.security.impl.PasswordVerifier;

/**
 * web端登录，用户名+密码+验证码
 * 
 */
public class AdminLoginVerifier extends PasswordVerifier {

    private String vcode;

    public AdminLoginVerifier(String loginName, String password){
        super(loginName, password);
        this.vcode = vcode;
    }

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

}
