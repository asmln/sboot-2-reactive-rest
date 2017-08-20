package com.sbrw.auth;

import com.sbrw.auth.controllers.AuthController;
import com.sbrw.auth.controllers.ExceptionHandlingController;
import com.sbrw.auth.data.in.AuthReq;
import com.sbrw.auth.data.in.TokenReq;
import com.sbrw.auth.model.AuthRepository;
import com.sbrw.auth.model.RegistrationService;
import com.sbrw.auth.model.RegistrationServiceImpl;
import com.sbrw.auth.model.exceptions.TokenNotFoundException;
import com.sbrw.auth.model.exceptions.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit Test для
 */
//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(MockitoJUnitRunner.class)
public class AuthControllerWTCUnitTests {

    private WebTestClient webTestClient;

    @Mock
    private AuthRepository authRepository;

    @Before
    public void setUp() throws Exception {
        webTestClient = WebTestClient.bindToController(
                new AuthController(
                        new RegistrationServiceImpl(authRepository)
                )
        ).controllerAdvice(new ExceptionHandlingController()).build();
    }

    @Test
    public void testUserNotFound() throws Exception {
        when(authRepository.findAuthorityByEmail(any())).thenReturn(Mono.error(new UserNotFoundException()));

        AuthReq req = new AuthReq();
        req.setEmail("testUserNotFound@mail.com");
        req.setPassword("rrrr");
        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(req), AuthReq.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("User not found")
                .jsonPath("$.error").isEqualTo("UNF");
    }

    @Test
    public void testTokenNotFound() throws Exception {
        when(authRepository.confirmUserByToken(any())).thenReturn(Mono.error(new TokenNotFoundException()));

        TokenReq tokenReq = new TokenReq();
        tokenReq.setToken("faketoken");

        webTestClient.post().uri("/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(tokenReq), TokenReq.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("Token not found")
                .jsonPath("$.error").isEqualTo("TNF");
    }
}
