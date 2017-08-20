package com.sbrw.auth.model;

import com.sbrw.auth.data.Authority;
import com.sbrw.auth.data.Token;
import com.sbrw.auth.data.User;
import com.sbrw.auth.model.exceptions.UserAlreadyExistsException;
import com.sbrw.auth.model.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
            if (user.getPasswordHash().equals(password)) {
                String tokenStr = UUID.randomUUID().toString();
                return authRepository.saveToken(tokenStr, user.getEmail());
            } else {
                return Mono.error(new UserAlreadyExistsException());
            }
        });
    }

    @Override
    public Mono<User> confirmUser(String token) {
        return authRepository.confirmUserByToken(token);
    }

    @Override
    public Mono<User> loginUser(String email, String password) {
        return authRepository.findAuthorityByEmail(email).filter(auth ->
                auth.getUser().getPasswordHash().equals(password)
        ).map(Authority::getUser)
                .switchIfEmpty(
                        Mono.error(new UserNotFoundException())
                );
    }

}
