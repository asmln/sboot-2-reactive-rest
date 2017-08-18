package com.sbrw.auth.model;

import com.sbrw.auth.data.Authority;
import com.sbrw.auth.data.Token;
import com.sbrw.auth.data.User;
import com.sbrw.auth.model.exceptions.TokenNotFoundException;
import com.sbrw.auth.model.exceptions.UserNotFoundException;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User repository in memory
 */
@Repository
public class AuthRepositoryMem implements AuthRepository {

    private final ConcurrentHashMap<String, Authority> emailAuthMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> tokenEmailMap = new ConcurrentHashMap<>();

    @Override
    public Mono<User> saveUser(Mono<User> userPublisher) {
        return userPublisher.map(user -> {
            Authority authSaved = emailAuthMap.putIfAbsent(user.getEmail(), new Authority(user, false));
            return (authSaved != null) ? authSaved.getUser() : user;
        });
    }

    @Override
    public Mono<Token> saveToken(String token, String email) {
        return findAuthorityByEmail(email).flatMap(auth -> {
            Token tOld = auth.putToken(new Token(token));
            if (tOld != null) {
                tokenEmailMap.remove(tOld.getToken(), email);
            }
            return Mono.empty();
        }).then(Mono.fromCallable(() -> {
            tokenEmailMap.put(token, email);
            return new Token(token);
        }));
    }

    @Override
    public Mono<Authority> findAuthorityByEmail(String email) {
        return Mono.justOrEmpty(
                emailAuthMap.get(email)
        ).switchIfEmpty(
                Mono.error(new UserNotFoundException())
        );
    }

    @Override
    public Mono<User> confirmUserByToken(String token) {
        return Mono.justOrEmpty(
                tokenEmailMap.remove(token)
        ).switchIfEmpty(
                Mono.error(new TokenNotFoundException())
        ).map(emailAuthMap::get).switchIfEmpty(
                Mono.error(new UserNotFoundException())
        ).map(auth -> {
            auth.setConfirmed(true);
            return auth.getUser();
        });
    }
}
