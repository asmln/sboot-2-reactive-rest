package com.sbrw.auth.model;

import com.sbrw.auth.model.data.Authority;
import com.sbrw.auth.model.data.Token;
import com.sbrw.auth.model.data.User;
import com.sbrw.auth.model.exceptions.TokenNotFoundException;
import com.sbrw.auth.model.exceptions.UserAlreadyExistsException;
import com.sbrw.auth.model.exceptions.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit Test для com.sbrw.auth.model.RegistrationServiceImpl
 * Mockito и StepVerifier для тестирования react
 */
@RunWith(MockitoJUnitRunner.class)
public class RegistrationServiceImplWTCUnitTests {

    private String email = "test@mail.com";
    private String password= "qwerty";
    private String wrongPassword= "wrongpassword";

    private String tokenString = "token_string";

    private RegistrationService registrationService;

    @Mock
    private AuthRepository authRepository;

    @Before
    public void setUp() throws Exception {
        registrationService = new RegistrationServiceImpl(authRepository);
    }

    @Test
    public void testUserNotFound() throws Exception {
        when(authRepository.findAuthorityByEmail(any())).thenReturn(Mono.empty());

        StepVerifier.create(
            registrationService.loginUser(email, password)
        ).expectError(UserNotFoundException.class).verify();
    }

    @Test
    public void testTokenNotFound() throws Exception {
        when(authRepository.extractEmailByToken(any())).thenReturn(Mono.empty());

        StepVerifier.create(
            registrationService.confirmUser(tokenString)
        ).expectError(TokenNotFoundException.class).verify();
    }

    @Test
    public void testUserNotFoundByToken() throws Exception {
        when(authRepository.extractEmailByToken(any())).thenReturn(Mono.just(email));
        when(authRepository.findAuthorityByEmail(any())).thenReturn(Mono.empty());

        StepVerifier.create(
            registrationService.confirmUser(tokenString)
        ).expectError(UserNotFoundException.class).verify();
    }

    @Test
    public void testUserAlreadyExists() throws Exception {
        User user = new User(email, "1234");
        when(authRepository.saveUser(any())).thenReturn(Mono.just(user));

        StepVerifier.create(
            registrationService.registerUser(email, password)
        ).expectError(UserAlreadyExistsException.class).verify();
    }

    @Test
    public void testRegister() throws Exception {
        User user = new User(email, password);
        Token token = new Token(tokenString);
        when(authRepository.saveUser(any())).thenReturn(Mono.just(user));
        when(authRepository.saveToken(any(), any())).thenReturn(Mono.just(token));

        StepVerifier.create(
            registrationService.registerUser(email, password)
        ).expectNext(token).verifyComplete();
    }

    @Test
    public void testConfirm() throws Exception {
        User user = new User(email, password);
        Authority authority = new Authority(user, false);
        when(authRepository.extractEmailByToken(any())).thenReturn(Mono.just(email));
        when(authRepository.findAuthorityByEmail(email)).thenReturn(Mono.just(authority));
        when(authRepository.confirm(any())).thenReturn(Mono.just(new Authority(user, true)));

        StepVerifier.create(
                registrationService.confirmUser(tokenString)
        ).expectNext(user).verifyComplete();
    }

    @Test
    public void testRegisterConfirm() throws Exception {
        User user = new User(email, password);
        Token token = new Token(tokenString);
        Authority authority = new Authority(user, false);
        when(authRepository.saveUser(any())).thenReturn(Mono.just(user));
        when(authRepository.saveToken(any(), any())).thenReturn(Mono.just(token));
        when(authRepository.extractEmailByToken(any())).thenReturn(Mono.just(email));
        when(authRepository.findAuthorityByEmail(email)).thenReturn(Mono.just(authority));
        when(authRepository.confirm(any())).thenReturn(Mono.just(new Authority(user, true)));

        StepVerifier.create(
                registrationService.registerUser(email, password)
        ).consumeNextWith(t ->
            StepVerifier.create(
                    registrationService.confirmUser(t.getToken())
            ).expectNext(user).verifyComplete()
        ).verifyComplete();
    }

    @Test
    public void testWrongPassword() throws Exception {
        User user = new User(email, password);
        Token token = new Token(tokenString);
        Authority authority = new Authority(user, false);
        when(authRepository.saveUser(any())).thenReturn(Mono.just(user));
        when(authRepository.saveToken(any(), any())).thenReturn(Mono.just(token));
        when(authRepository.extractEmailByToken(any())).thenReturn(Mono.just(email));
        when(authRepository.findAuthorityByEmail(email)).thenReturn(Mono.just(authority));
        when(authRepository.confirm(any())).thenReturn(Mono.just(new Authority(user, true)));

        StepVerifier.create(
                registrationService.registerUser(email, password)
        ).consumeNextWith(t ->
                StepVerifier.create(
                        registrationService.confirmUser(t.getToken())
                ).consumeNextWith(u ->
                        StepVerifier.create(
                                registrationService.loginUser(email, wrongPassword)
                        ).expectError(UserNotFoundException.class).verify()
                ).verifyComplete()
        ).verifyComplete();
    }

    @Test
    public void testLogin() throws Exception {
        User user = new User(email, password);
        Authority authority = new Authority(user, false);
        when(authRepository.findAuthorityByEmail(any())).thenReturn(Mono.just(authority));

        StepVerifier.create(
                registrationService.loginUser(user.getEmail(), user.getPassword())
        ).expectNext(user).verifyComplete();
    }
}
