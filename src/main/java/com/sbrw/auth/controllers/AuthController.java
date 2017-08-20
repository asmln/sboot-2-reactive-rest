package com.sbrw.auth.controllers;

import com.sbrw.auth.data.in.AuthReq;
import com.sbrw.auth.data.in.TokenReq;
import com.sbrw.auth.data.out.*;
import com.sbrw.auth.model.RegistrationService;
import org.reactivestreams.Publisher;
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

    @GetMapping("/usual")
    BaseResponse helloUsual() {
        return new OkResponse("hello");
    }

    @GetMapping("/reactive")
    Mono<BaseResponse> helloReactive() {
        return Mono.just(new OkResponse("hello"));
    }

    @PostMapping("/register")
    Mono<BaseResponse> register(@RequestBody AuthReq authReq) {
        return registrationService.registerUser(authReq.getEmail(), authReq.getPassword())
                .map(t ->
                        new TokenResponse(t.getToken())
                );
    }

    @PostMapping("/confirm")
    Mono<BaseResponse> confirm(@RequestBody TokenReq tokenReq) {
        return registrationService.confirmUser(tokenReq.getToken())
                .map(UserResponse::new);
    }

    @PostMapping("/login")
    Mono<BaseResponse> login(@RequestBody AuthReq authReq) {
        return registrationService.loginUser(authReq.getEmail(), authReq.getPassword())
                .map(UserResponse::new);
    }
}
