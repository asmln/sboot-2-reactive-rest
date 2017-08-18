package com.sbrw.auth.controllers;

import com.sbrw.auth.data.out.ErrorResponse;
import com.sbrw.auth.model.exceptions.AuthException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

/**
 * Created by Anatoly Samoylenko on 18.08.2017.
 */
@ControllerAdvice
@ResponseBody
public class ExceptionHandlingController {

    static private final String DEFAULT_MESSAGE = "Unknown error";
    static private final String DEFAULT_CODE = "UN";

    @ExceptionHandler(AuthException.class)
    Mono<ErrorResponse> authException(AuthException e) {
        return Mono.just(
                new ErrorResponse(e.getMessage(), e.getCode())
        );
    }

    @ExceptionHandler(Exception.class)
    Mono<ErrorResponse> authException() {
        return Mono.just(
                new ErrorResponse(DEFAULT_MESSAGE, DEFAULT_CODE)
        );
    }

}
