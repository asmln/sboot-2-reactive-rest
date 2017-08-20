package com.sbrw.auth.controllers;

import com.sbrw.auth.model.data.Token;
import com.sbrw.auth.model.data.User;
import com.sbrw.auth.model.data.in.AuthRequest;
import com.sbrw.auth.model.data.in.TokenRequest;
import com.sbrw.auth.model.RegistrationService;
import com.sbrw.auth.model.exceptions.TokenNotFoundException;
import com.sbrw.auth.model.exceptions.UserAlreadyExistsException;
import com.sbrw.auth.model.exceptions.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit Test для com.sbrw.auth.controllers.AuthController
 * WebTestClient и Mockito
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthControllerWTCUnitTests {

    private AuthRequest authRequest = new AuthRequest();
    private TokenRequest tokenRequest = new TokenRequest();

    private WebTestClient webTestClient;

    @Mock
    private RegistrationService registrationService;

    @Before
    public void setUp() throws Exception {
        webTestClient = WebTestClient.bindToController(
                new AuthController(
                        registrationService
                )
        ).controllerAdvice(new ExceptionHandlingController()).build();

        authRequest.setEmail("test@mail.com");
        authRequest.setPassword("qwerty");

        tokenRequest.setToken("token_string");

    }

    @Test
    public void testUserNotFound() throws Exception {
        when(registrationService.loginUser(any(), any())).thenReturn(Mono.error(new UserNotFoundException()));

        login(authRequest)
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("User not found")
                .jsonPath("$.error").isEqualTo("UNF");
    }

    @Test
    public void testTokenNotFound() throws Exception {
        when(registrationService.confirmUser(any())).thenReturn(Mono.error(new TokenNotFoundException()));

        confirm(tokenRequest)
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("Token not found")
                .jsonPath("$.error").isEqualTo("TNF");
    }

    @Test
    public void testUserNotFoundByToken() throws Exception {
        when(registrationService.confirmUser(any())).thenReturn(Mono.error(new UserNotFoundException()));

        confirm(tokenRequest)
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("User not found")
                .jsonPath("$.error").isEqualTo("UNF");
    }

    @Test
    public void testUserAlreadyExists() throws Exception {
        when(registrationService.registerUser(any(), any())).thenReturn(Mono.error(new UserAlreadyExistsException()));

        register(authRequest)
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("User already exists")
                .jsonPath("$.error").isEqualTo("UAE");
    }

    @Test
    public void testRegister() throws Exception {
        when(registrationService.registerUser(any(), any())).thenReturn(Mono.just(new Token(tokenRequest.getToken())));

        register(authRequest)
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.message").isEqualTo("")
                .jsonPath("$.token").isEqualTo(tokenRequest.getToken());
    }

    @Test
    public void testConfirm() throws Exception {
        User user = new User(authRequest.getEmail(), authRequest.getPassword());
        when(registrationService.confirmUser(any())).thenReturn(Mono.just(user));

        confirm(tokenRequest)
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.message").isEqualTo("")
                .jsonPath("$.user.id").isEqualTo(user.getId())
                .jsonPath("$.user.email").isEqualTo(user.getEmail())
                .jsonPath("$.user.created").isEqualTo(user.getCreated())
                .jsonPath("$.user.passwordHash").doesNotExist();
    }

    @Test
    public void testLogin() throws Exception {
        User user = new User(authRequest.getEmail(), authRequest.getPassword());
        when(registrationService.loginUser(any(), any())).thenReturn(Mono.just(user));

        login(authRequest)
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.message").isEqualTo("")
                .jsonPath("$.user.id").isEqualTo(user.getId())
                .jsonPath("$.user.email").isEqualTo(user.getEmail())
                .jsonPath("$.user.created").isEqualTo(user.getCreated())
                .jsonPath("$.user.passwordHash").doesNotExist();
    }

    private WebTestClient.BodyContentSpec login(AuthRequest authRequest) {
        return webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(authRequest), AuthRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody();
    }

    private WebTestClient.BodyContentSpec register(AuthRequest authRequest) {
        return webTestClient.post().uri("/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(authRequest), AuthRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody();
    }

    private WebTestClient.BodyContentSpec confirm(TokenRequest tokenRequest) {
        return webTestClient.post().uri("/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(tokenRequest), TokenRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody();
    }
}
