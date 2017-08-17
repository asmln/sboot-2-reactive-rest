package com.sbrw.auth.controllers;

import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * Auth handler
 */
@Component
public class AuthHandler {

    public Mono<ServerResponse> register(ServerRequest request) {
        return ok().body(fromObject("register"));
    }

    public Mono<ServerResponse> confirm(ServerRequest request) {
        return ok().body(fromObject("confirm"));
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return ok().body(fromObject("login"));
    }

}
