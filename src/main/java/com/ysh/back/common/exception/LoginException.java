package com.ysh.back.common.exception;

import org.springframework.security.authentication.InternalAuthenticationServiceException;

public class LoginException extends InternalAuthenticationServiceException{
    public LoginException(String message) {
        super(message);
    }

    public LoginException() {
        super("로그인 오류");
    }
}
