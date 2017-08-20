package com.sbrw.auth.model.data.out;

import com.sbrw.auth.model.data.User;

/**
 * User response
 */
public class UserResponse extends BaseResponse {

    private User user;

    public UserResponse(User user) {
        super(true, "");
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
