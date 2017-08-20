package com.sbrw.auth.model.data.out;

/**
 * Error response
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
