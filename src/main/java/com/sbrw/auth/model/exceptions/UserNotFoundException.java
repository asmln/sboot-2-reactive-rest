package com.sbrw.auth.model.exceptions;

/**
 * Created by Anatoly Samoylenko on 18.08.2017.
 */
public class UserNotFoundException extends AuthException {
    public UserNotFoundException() {
        super("User not found", "UNF");
    }
}
