package com.sbrw.auth.model.exceptions;

/**
 * Created by Anatoly Samoylenko on 18.08.2017.
 */
public class UserAlreadyExistsException extends AuthException {
    public UserAlreadyExistsException() {
        super("User already exists", "UAE");
    }
}
