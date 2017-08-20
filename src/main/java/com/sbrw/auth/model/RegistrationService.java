package com.sbrw.auth.model;

import com.sbrw.auth.model.data.Token;
import com.sbrw.auth.model.data.User;
import reactor.core.publisher.Mono;

/**
 * RegistrationService
 */
public interface RegistrationService {

    Mono<Token> registerUser(String email, String password);

    Mono<User> confirmUser(String token);

    Mono<User> loginUser(String email, String password);

}
