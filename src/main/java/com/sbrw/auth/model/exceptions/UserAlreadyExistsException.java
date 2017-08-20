package com.sbrw.auth.model.exceptions;

public class UserAlreadyExistsException extends AuthException {
    public UserAlreadyExistsException() {
        super("User already exists", "UAE");
    }
}
