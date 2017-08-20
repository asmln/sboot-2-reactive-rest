package com.sbrw.auth.model;

import com.sbrw.auth.model.data.Authority;
import com.sbrw.auth.model.data.Token;
import com.sbrw.auth.model.data.User;
import reactor.core.publisher.Mono;

/**
 * AuthRepository.
 */
public interface AuthRepository {

    Mono<User> saveUser(Mono<User> user);

    Mono<Token> saveToken(String token, String email);

    Mono<String> extractEmailByToken(String token);

    Mono<Authority> findAuthorityByEmail(String email);

    Mono<Authority> confirm(Authority auth);
}
