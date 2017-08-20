package com.sbrw.auth.model;

import com.sbrw.auth.model.data.Authority;
import com.sbrw.auth.model.data.Token;
import com.sbrw.auth.model.data.User;
import com.sbrw.auth.model.exceptions.TokenNotFoundException;
import com.sbrw.auth.model.exceptions.UserAlreadyExistsException;
import com.sbrw.auth.model.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * RegistrationService realization
 */
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private AuthRepository authRepository;

    @Autowired
    public RegistrationServiceImpl(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public Mono<Token> registerUser(String email, String password) {
        return authRepository.saveUser(
                Mono.just(new User(email, password))
        ).flatMap(user -> {
            if (user.getPassword().equals(password)) {
                String tokenStr = UUID.randomUUID().toString();
                return authRepository.saveToken(tokenStr, user.getEmail());
            } else {
                return Mono.error(new UserAlreadyExistsException());
            }
        });
    }

    @Override
    public Mono<User> confirmUser(String token) {
        return authRepository.extractEmailByToken(token).switchIfEmpty(
                Mono.error(new TokenNotFoundException())
        ).flatMap(email ->
                authRepository.findAuthorityByEmail(email)
        ).switchIfEmpty(
                Mono.error(new UserNotFoundException())
        ).flatMap(auth ->
            authRepository.confirm(auth)
        ).switchIfEmpty(
                Mono.error(new UserNotFoundException())
        ).map(Authority::getUser);
    }

    @Override
    public Mono<User> loginUser(String email, String password) {
        return authRepository.findAuthorityByEmail(
                email
        ).switchIfEmpty(
                Mono.error(new UserNotFoundException())
        ).filter(auth ->
                auth.getUser().getPassword().equals(password)
        ).switchIfEmpty(
                Mono.error(new UserNotFoundException())
        ).map(
                Authority::getUser
        );
    }

}
