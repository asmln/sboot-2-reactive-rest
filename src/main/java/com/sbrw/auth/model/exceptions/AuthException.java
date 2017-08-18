package com.sbrw.auth.model.exceptions;

/**
 * Created by Anatoly Samoylenko on 18.08.2017.
 */
public class AuthException extends Exception {

    private String code;

    public AuthException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
