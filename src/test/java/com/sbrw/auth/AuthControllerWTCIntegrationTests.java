package com.sbrw.auth;

import com.sbrw.auth.controllers.AuthController;
import com.sbrw.auth.controllers.ExceptionHandlingController;
import com.sbrw.auth.data.Token;
import com.sbrw.auth.data.in.AuthReq;
import com.sbrw.auth.data.in.TokenReq;
import com.sbrw.auth.data.out.TokenResponse;
import com.sbrw.auth.model.RegistrationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * WebTestClient
 * для тестирование reactive web
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = AuthApplication.class)
public class AuthControllerWTCIntegrationTests {

	private JacksonJsonParser parser;

	@Autowired
	private AuthController authController;

	@Autowired
	private ExceptionHandlingController exceptionHandlingController;

//	@Autowired
	private WebTestClient webTestClient;

//	@Bean
//	private RegistrationService registrationService;
//
//	@Bean
//	private AuthRepository authRepository;

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

	@Test
	public void testRegister() throws Exception {
		AuthReq req = new AuthReq();
		req.setEmail("testRegister@mail.com");
		req.setPassword("123456");
		webTestClient.post().uri("/register")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(Mono.just(req), AuthReq.class)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBody()
				.jsonPath("$.token").exists();
	}

	@Test
	public void testRegisterLogin() throws Exception {
		AuthReq req = new AuthReq();
		req.setEmail("testRegisterLogin@mail.com");
		req.setPassword("123456");
		webTestClient.post().uri("/register")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(Mono.just(req), AuthReq.class)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBody()
				.jsonPath("$.token").exists();
		webTestClient.post().uri("/login")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(Mono.just(req), AuthReq.class)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBody()
				.jsonPath("$.success").isEqualTo(true)
				.jsonPath("$.user").exists()
				.jsonPath("$.user.email").isEqualTo(req.getEmail())
				.jsonPath("$.user.id").isNumber()
				.jsonPath("$.user.created").isNumber();
	}

	@Test
	public void testRegisterConfirmLogin() throws Exception {
		AuthReq req = new AuthReq();
		req.setEmail("testRegisterConfirmLogin@mail.com");
		req.setPassword("123456");

		EntityExchangeResult<byte[]> registerResult = webTestClient.post().uri("/register")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(Mono.just(req), AuthReq.class)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBody().returnResult();

		String tokenJson = new String(registerResult.getResponseBody(), "UTF-8");
		String token = parser.parseMap(tokenJson).get("token").toString();
		TokenReq tokenReq = new TokenReq();
		tokenReq.setToken(token);

		EntityExchangeResult<byte[]> confirmResult = webTestClient.post().uri("/confirm")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(Mono.just(tokenReq), TokenReq.class)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBody()
				.jsonPath("$.success").isEqualTo(true)
				.jsonPath("$.user").exists()
				.jsonPath("$.user.email").isEqualTo(req.getEmail())
				.jsonPath("$.user.id").isNumber()
				.jsonPath("$.user.created").isNumber()
				.returnResult();

		String userConfirmJson = new String(confirmResult.getResponseBody(), "UTF-8");

		EntityExchangeResult<byte[]> loginResult = webTestClient.post().uri("/login")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(Mono.just(req), AuthReq.class)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBody()
				.jsonPath("$.success").isEqualTo(true)
				.jsonPath("$.user").exists()
				.jsonPath("$.user.email").isEqualTo(req.getEmail())
				.jsonPath("$.user.id").isNumber()
				.jsonPath("$.user.created").isNumber()
				.returnResult();

		String userLoginJson = new String(loginResult.getResponseBody(), "UTF-8");

		assertEquals(userConfirmJson, userLoginJson);
	}

}
