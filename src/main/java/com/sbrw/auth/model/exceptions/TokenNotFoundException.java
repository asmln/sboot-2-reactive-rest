package com.sbrw.auth.model.exceptions;

public class TokenNotFoundException extends AuthException {
    public TokenNotFoundException() {
        super("Token not found", "TNF");
    }
}
