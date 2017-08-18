package com.sbrw.auth.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.concurrent.atomic.AtomicLong;

/**
 * User
 */
public class User {

    static final AtomicLong NEXT_ID = new AtomicLong(0);

    private Long id = NEXT_ID.getAndIncrement();
    private String email;
    @JsonIgnore
    private String passwordHash;
    private long created = System.currentTimeMillis() / 1000L;

    public User(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public long getCreated() {
        return created;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
