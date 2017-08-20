package com.sbrw.auth.model.exceptions;

public class UserNotFoundException extends AuthException {
    public UserNotFoundException() {
        super("User not found", "UNF");
    }
}
