package com.sbrw.auth.model;

import com.sbrw.auth.model.data.Authority;
import com.sbrw.auth.model.data.Token;
import com.sbrw.auth.model.data.User;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Repository with data storage in memory
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
    public Mono<String> extractEmailByToken(String token) {
        return Mono.justOrEmpty(
                tokenEmailMap.remove(token)
        );
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
        );
    }

    @Override
    public Mono<Authority> confirm(Authority auth) {
        return Mono.justOrEmpty(
                emailAuthMap.get(auth.getUser().getEmail())
        ).map(a -> {
            a.setConfirmed(true);
            return a;
        });
    }

}
