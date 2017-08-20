package com.sbrw.auth.model.data.out;

/**
 * Token response
 */
public class TokenResponse extends BaseResponse {

    private String token;

    public TokenResponse(String token) {
        super(true, "");
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
