package com.sbrw.auth.model.data.in;

import javax.validation.constraints.NotNull;

/**
 * Token request
 */
public class TokenRequest {

    @NotNull
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
