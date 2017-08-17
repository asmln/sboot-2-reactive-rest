package com.sbrw.auth.data.out;

/**
 * Created by Anatoly Samoylenko on 17.08.2017.
 */
public class OkResponse extends BaseResponse {

    public OkResponse(String message) {
        super(true, message);
    }

}
