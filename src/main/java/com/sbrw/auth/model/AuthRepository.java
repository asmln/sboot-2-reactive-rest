package com.sbrw.auth.model;

import com.sbrw.auth.data.Authority;
import com.sbrw.auth.data.Token;
import com.sbrw.auth.data.User;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * AuthRepository
 */
public interface AuthRepository {

    Mono<User> saveUser(Mono<User> user);

    Mono<Token> saveToken(String token, String email);

    Mono<Authority> findAuthorityByEmail(String email);

    Mono<User> confirmUserByToken(String token);

}
