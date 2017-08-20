package com.sbrw.auth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * MockMVC
 * не подходит для теститрование reactive-web
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthApplicationMMIntegrationTests {

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
