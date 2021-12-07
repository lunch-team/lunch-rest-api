package com.lunchteam.lunchrestapi.api.exception;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
        super();
    }

    public AuthenticationException(String msg) {
        super(msg);
    }
}
