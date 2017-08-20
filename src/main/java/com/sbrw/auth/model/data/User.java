package com.sbrw.auth.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.concurrent.atomic.AtomicLong;

/**
 * User
 */
public class User {

    static private final AtomicLong NEXT_ID = new AtomicLong(0);

    private Long id = NEXT_ID.getAndIncrement();
    private String email;
    @JsonIgnore
    private String password;
    private long created = System.currentTimeMillis() / 1000L;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }
}
