package com.sbrw.auth.model.exceptions;

/**
 * Created by Anatoly Samoylenko on 18.08.2017.
 */
public class TokenNotFoundException extends AuthException {
    public TokenNotFoundException() {
        super("Token not found", "TNF");
    }
}
