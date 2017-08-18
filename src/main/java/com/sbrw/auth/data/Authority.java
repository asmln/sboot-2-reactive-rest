package com.sbrw.auth.data;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Authority
 */
public class Authority {

    private AtomicReference<Token> tokenBox = new AtomicReference<>();
    private User user;
    private boolean confirmed;

    public Authority(User user, boolean confirmed) {
        this.user = user;
        this.confirmed = confirmed;
    }

    public User getUser() {
        return user;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Token putToken(Token token) {
        return tokenBox.getAndSet(token);
    }
}
