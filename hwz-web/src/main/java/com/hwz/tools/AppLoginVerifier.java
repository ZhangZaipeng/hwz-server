package com.hwz.tools;

import com.hwz.common.security.impl.PasswordVerifier;

/**
 * 移动端 ：登录名 + 登录密码
 *
 */
public class AppLoginVerifier extends PasswordVerifier {

    public AppLoginVerifier(String loginName, String password){
        super(loginName, password);
    }

}
