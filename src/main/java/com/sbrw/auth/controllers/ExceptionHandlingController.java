package com.sbrw.auth.controllers;

import com.sbrw.auth.model.data.out.ErrorResponse;
import com.sbrw.auth.model.exceptions.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

/**
 * Exception handler
 */
@ControllerAdvice
@ResponseBody
@Controller
public class ExceptionHandlingController {

    static private final String VALIDATION_MESSAGE = "JSON validation failed";
    static private final String VALIDATION_CODE = "VLD";

    static private final String DEFAULT_MESSAGE = "Error";
    static private final String DEFAULT_CODE = "ERR";

    @ExceptionHandler(AuthException.class)
    Mono<ErrorResponse> authException(AuthException e) {
        return Mono.just(
                new ErrorResponse(e.getMessage(), e.getCode())
        );
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> bindException(WebExchangeBindException e) {
        return Mono.just(
                new ErrorResponse(VALIDATION_MESSAGE, VALIDATION_CODE)
        );
    }

    @ExceptionHandler(Exception.class)
    Mono<ErrorResponse> authException(Exception e) {
        return Mono.just(
                new ErrorResponse(DEFAULT_MESSAGE + ": " + e.getClass().getName() + (e.getMessage() != null ? " " + e.getMessage() : ""), DEFAULT_CODE)
        );
    }

}
