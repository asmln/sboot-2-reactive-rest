package com.sbrw.auth;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * MockMVC
 * не подходит для теститрование reactive-web
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthControllerMMTests {

	static private final String USER_TEST1_EMAIL = "test1@test.com";
	static private final String USER_TEST1_PASS = "12345";
	static private final String USER_TEST1_JSON = "{" +
			"\"email\": \"" + USER_TEST1_EMAIL + "\"," +
			"\"password\": \"" + USER_TEST1_PASS + "\"" +
			"}";

	static private final String USER_TEST2_EMAIL = "test2@test.ru";
	static private final String USER_TEST2_PASS = "qwerty";
	static private final String USER_TEST2_JSON = "{\n" +
			"'email': '" + USER_TEST2_EMAIL + "',\n" +
			"'password': '" + USER_TEST2_PASS + "'\n" +
			"}";

	private JacksonJsonParser parser;

	@Autowired
	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		parser = new JacksonJsonParser();
	}

	//Работает нормально, т.к. запрос возвращает не реактивный результат
	@Test
	public void testGetUsual() throws Exception {
		mockMvc.perform(get("/usual")).andExpect(content().json("{\"success\":true,\"message\":\"hello\"}"));
	}

	//Не работает
	@Test
	public void testGetReactive() throws Exception {
		mockMvc.perform(get("/reactive")).andExpect(content().json("{\"success\":true,\"message\":\"hello\"}"));
	}

}
