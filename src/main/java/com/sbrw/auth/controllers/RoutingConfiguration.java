package com.sbrw.auth.controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * Routing
 */
@Configuration
public class RoutingConfiguration {

    @Bean
    public RouterFunction<ServerResponse> authRouterFunction(AuthHandler authHandler) {
        return route(GET("/"), request -> ok().body(fromObject("hello")))
                .andRoute(POST("/register").and(accept(APPLICATION_JSON)),
                        authHandler::register)
                .andRoute(POST("/confirm").and(accept(APPLICATION_JSON)),
                        authHandler::confirm)
                .andRoute(POST("/login").and(accept(APPLICATION_JSON)),
                        authHandler::login);
    }

}
