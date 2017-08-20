package com.sbrw.auth.model.exceptions;

/**
 * Exceptions base class
 */
public abstract class AuthException extends Exception {

    private String code;

    public AuthException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
