package com.sbrw.auth.controllers;

import com.sbrw.auth.data.in.AuthReq;
import com.sbrw.auth.data.in.TokenReq;
import com.sbrw.auth.data.out.*;
import com.sbrw.auth.model.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * REST controller
 */
@RestController
public class AuthController {

    private RegistrationService registrationService;

    @Autowired
    public AuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/")
    BaseResponse hello() {
        return new OkResponse("hello");
    }


    @PostMapping("/register")
    Mono<BaseResponse> register(@RequestBody Mono<AuthReq> authReqPublisher) {
        return authReqPublisher.flatMap(r ->
                registrationService.registerUser(r.getEmail(), r.getPassword())
        ).doOnError(
                throwable -> new ErrorResponse(throwable.getMessage(), "000")
        ).map(t ->
                new TokenResponse(t.getToken())
        );
    }

    @PostMapping("/confirm")
    Mono<BaseResponse> confirm(@RequestBody Mono<TokenReq> tokenReqPublisher) {
        return tokenReqPublisher.flatMap(r ->
                registrationService.confirmUser(r.getToken())
        ).doOnError(
                throwable -> new ErrorResponse(throwable.getMessage(), "000")
        ).map(UserResponse::new);
    }

    @PostMapping("/login")
    Mono<BaseResponse> login(@RequestBody Mono<AuthReq> authReqPublisher) {
        return authReqPublisher.flatMap(r ->
                registrationService.loginUser(r.getEmail(), r.getPassword())
        ).doOnError(
                throwable -> new ErrorResponse(throwable.getMessage(), "000")
        ).map(UserResponse::new);
    }
}
