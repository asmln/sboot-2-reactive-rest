package com.sbrw.auth.model.data.out;

/**
 * Ok response. Just message.
 */
public class OkResponse extends BaseResponse {

    public OkResponse(String message) {
        super(true, message);
    }

}
