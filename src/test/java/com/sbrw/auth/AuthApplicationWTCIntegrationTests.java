package com.sbrw.auth;

import com.sbrw.auth.controllers.AuthController;
import com.sbrw.auth.controllers.ExceptionHandlingController;
import com.sbrw.auth.model.data.in.AuthRequest;
import com.sbrw.auth.model.data.in.TokenRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.Assert.assertEquals;

/**
 * WebTestClient
 * для тестирование reactive web
 * интеграционное тестирование API
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = AuthApplication.class)
public class AuthApplicationWTCIntegrationTests {

	private JacksonJsonParser parser;

	@Autowired
	private AuthController authController;

	@Autowired
	private ExceptionHandlingController exceptionHandlingController;

	private WebTestClient webTestClient;

	@Before
	public void setUp() throws Exception {
		parser = new JacksonJsonParser();

		webTestClient = WebTestClient.bindToController(authController)
				.controllerAdvice(exceptionHandlingController).build();
	}

	@Test
	public void testGetUsual() throws Exception {
		webTestClient.get().uri("/usual")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.message").isEqualTo("hello");
	}

	@Test
	public void testGetReactive() throws Exception {
		webTestClient.get().uri("/reactive")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.message").isEqualTo("hello");
	}

	@Test
	public void testUserNotFound() throws Exception {
		AuthRequest req = new AuthRequest();
		req.setEmail("testUserNotFound@mail.com");
		req.setPassword("rrrr");
		login(req)
				.jsonPath("$.success").isEqualTo(false)
				.jsonPath("$.message").isEqualTo("User not found")
				.jsonPath("$.error").isEqualTo("UNF");
	}

	@Test
	public void testTokenNotFound() throws Exception {
		TokenRequest tokenRequest = new TokenRequest();
		tokenRequest.setToken("faketoken");

		confirm(tokenRequest)
				.jsonPath("$.success").isEqualTo(false)
				.jsonPath("$.message").isEqualTo("Token not found")
				.jsonPath("$.error").isEqualTo("TNF");
	}

	@Test
	public void testRegister() throws Exception {
		AuthRequest req = new AuthRequest();
		req.setEmail("testRegister@mail.com");
		req.setPassword("123456");
		register(req)
				.jsonPath("$.token").exists();
	}

	@Test
	public void testRegisterLogin() throws Exception {
		AuthRequest req = new AuthRequest();
		req.setEmail("testRegisterLogin@mail.com");
		req.setPassword("123456");
		register(req)
				.jsonPath("$.token").exists();
		login(req)
				.jsonPath("$.success").isEqualTo(true)
				.jsonPath("$.user").exists()
				.jsonPath("$.user.email").isEqualTo(req.getEmail())
				.jsonPath("$.user.id").isNumber()
				.jsonPath("$.user.created").isNumber();
	}

	@Test
	public void testRegisterConfirmLogin() throws Exception {
		AuthRequest req = new AuthRequest();
		req.setEmail("testRegisterConfirmLogin@mail.com");
		req.setPassword("123456");

		EntityExchangeResult<byte[]> registerResult = register(req).returnResult();

		String tokenJson = new String(registerResult.getResponseBody(), "UTF-8");
		String token = parser.parseMap(tokenJson).get("token").toString();
		TokenRequest tokenRequest = new TokenRequest();
		tokenRequest.setToken(token);

		EntityExchangeResult<byte[]> confirmResult = confirm(tokenRequest)
				.jsonPath("$.success").isEqualTo(true)
				.jsonPath("$.user").exists()
				.jsonPath("$.user.email").isEqualTo(req.getEmail())
				.jsonPath("$.user.id").isNumber()
				.jsonPath("$.user.created").isNumber()
				.returnResult();

		String userConfirmJson = new String(confirmResult.getResponseBody(), "UTF-8");

		EntityExchangeResult<byte[]> loginResult = login(req)
				.jsonPath("$.success").isEqualTo(true)
				.jsonPath("$.user").exists()
				.jsonPath("$.user.email").isEqualTo(req.getEmail())
				.jsonPath("$.user.id").isNumber()
				.jsonPath("$.user.created").isNumber()
				.returnResult();

		String userLoginJson = new String(loginResult.getResponseBody(), "UTF-8");

		assertEquals(userConfirmJson, userLoginJson);
	}

    @Test
    public void test_Register_Register_FirstTokenNotFound_SecondOk() throws Exception {
        AuthRequest req = new AuthRequest();
        req.setEmail("testRegisterRegister@mail.com");
        req.setPassword("123456");

        EntityExchangeResult<byte[]> registerResult1 = register(req).returnResult();
        String tokenJson1 = new String(registerResult1.getResponseBody(), "UTF-8");
        String token1 = parser.parseMap(tokenJson1).get("token").toString();
        TokenRequest tokenRequest1 = new TokenRequest();
        tokenRequest1.setToken(token1);

        EntityExchangeResult<byte[]> registerResult2 = register(req).returnResult();
        String tokenJson2 = new String(registerResult2.getResponseBody(), "UTF-8");
        String token2 = parser.parseMap(tokenJson2).get("token").toString();
        TokenRequest tokenRequest2 = new TokenRequest();
        tokenRequest2.setToken(token2);

        confirm(tokenRequest1).jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("Token not found")
                .jsonPath("$.error").isEqualTo("TNF");

        confirm(tokenRequest2)
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.user").exists()
                .jsonPath("$.user.email").isEqualTo(req.getEmail())
                .jsonPath("$.user.id").isNumber()
                .jsonPath("$.user.created").isNumber();
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
