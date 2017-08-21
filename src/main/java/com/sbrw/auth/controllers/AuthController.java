package com.sbrw.auth.controllers;

import com.sbrw.auth.model.data.in.AuthRequest;
import com.sbrw.auth.model.data.in.TokenRequest;
import com.sbrw.auth.model.data.out.BaseResponse;
import com.sbrw.auth.model.data.out.OkResponse;
import com.sbrw.auth.model.data.out.TokenResponse;
import com.sbrw.auth.model.data.out.UserResponse;
import com.sbrw.auth.model.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * API
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
    Mono<BaseResponse> register(@Valid @RequestBody AuthRequest authRequest) {
        return registrationService.registerUser(authRequest.getEmail(), authRequest.getPassword())
                .map(t ->
                        new TokenResponse(t.getToken())
                );
    }

    @PostMapping("/confirm")
    Mono<BaseResponse> confirm(@Valid @RequestBody TokenRequest tokenRequest) {
        return registrationService.confirmUser(tokenRequest.getToken())
                .map(UserResponse::new);
    }

    @PostMapping("/login")
    Mono<BaseResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        return registrationService.loginUser(authRequest.getEmail(), authRequest.getPassword())
                .map(UserResponse::new);
    }
}
