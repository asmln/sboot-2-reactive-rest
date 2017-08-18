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

import java.util.UUID;

/**
 * RegistrationService realization
 */
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private boolean registerImmediately = false;
    private AuthRepository authRepository;

    @Autowired
    public RegistrationServiceImpl(AuthRepository authRepository, @Value("${app.register.immediately}") boolean registerImmediately) {
        this.registerImmediately = registerImmediately;
        this.authRepository = authRepository;
    }

    @Override
    public Mono<Token> registerUser(String email, String password) {
        //TODO добавить хэширование пароля
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
                auth.getUser().getPasswordHash().equals(password) && (registerImmediately || auth.isConfirmed())
        ).map(Authority::getUser)
                .switchIfEmpty(
                        Mono.error(new UserNotFoundException())
                );
    }
}
