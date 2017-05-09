/**
 * @(#)AuthenticationException.java Copyright 2011 jointown, Inc. All rights reserved.
 */
package com.hwz.common.security;

/**
 * description
 *
 * @author jianguo.xu
 * @version 1.0, 2011-2-17
 */
public class AuthenticationException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public AuthenticationException() {
        super();
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
    }

}
