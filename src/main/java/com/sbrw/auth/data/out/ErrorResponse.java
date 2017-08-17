package com.sbrw.auth.data.out;

/**
 * Created by Anatoly Samoylenko on 17.08.2017.
 */
public class ErrorResponse extends BaseResponse {

    private String error;

    public ErrorResponse(String message, String error) {
        super(false, message);
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
