package com.sbrw.auth.data.out;

/**
 * Created by Anatoly Samoylenko on 17.08.2017.
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
