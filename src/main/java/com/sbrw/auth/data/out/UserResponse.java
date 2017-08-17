package com.sbrw.auth.data.out;

import com.sbrw.auth.data.User;

/**
 * Created by Anatoly Samoylenko on 17.08.2017.
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
