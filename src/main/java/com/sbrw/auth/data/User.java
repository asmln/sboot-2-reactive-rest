package com.sbrw.auth.data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * User
 */
public class User {

    static final AtomicLong NEXT_ID = new AtomicLong(0);

    private Long id = NEXT_ID.getAndIncrement();
    private String email;
    private long created = System.currentTimeMillis() / 1000L;

    public User(String email) {
        this.email = email;
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

}
