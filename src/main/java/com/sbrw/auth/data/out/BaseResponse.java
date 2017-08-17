package com.sbrw.auth.data.out;

/**
 * Base class for response
 */
abstract public class BaseResponse {

    private boolean success;
    private String message;

    public BaseResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

}
