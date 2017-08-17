package com.sbrw.auth.controllers;

import com.sbrw.auth.data.User;
import com.sbrw.auth.data.in.AuthReq;
import com.sbrw.auth.data.in.TokenReq;
import com.sbrw.auth.data.out.BaseResponse;
import com.sbrw.auth.data.out.OkResponse;
import com.sbrw.auth.data.out.TokenResponse;
import com.sbrw.auth.data.out.UserResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * REST controller
 */
@RestController
public class AuthController {

    @GetMapping("/")
    BaseResponse hello() {
        return new OkResponse("hello");
    }


    @PostMapping("/register")
    Mono<BaseResponse> register(@RequestBody Mono<AuthReq> authReqPublisher) {
        return authReqPublisher.map(r ->
                new TokenResponse(UUID.randomUUID().toString())
        );
    }

    @PostMapping("/confirm")
    Mono<BaseResponse> confirm(@RequestBody Mono<TokenReq> tokenReqPublisher) {
        return tokenReqPublisher.map(r ->
                new UserResponse(new User(r.getToken() + "@mail.xxx"))
        );
    }

    @PostMapping("/login")
    Mono<BaseResponse> login(@RequestBody Mono<AuthReq> authReqPublisher) {
        return authReqPublisher.map(r ->
                new UserResponse(new User(r.getEmail()))
        );
    }
}
