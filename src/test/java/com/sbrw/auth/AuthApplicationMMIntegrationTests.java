package com.sbrw.auth;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * MockMVC
 * не получилось использовать с reactive-web
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthApplicationMMIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	//Работает нормально, т.к. запрос возвращает не реактивный результат
	@Test
	public void testGetUsual() throws Exception {
		mockMvc.perform(get("/usual")).andExpect(content().json("{\"success\":true,\"message\":\"hello\"}"));
	}

	//Не работает
	@Test
	@Ignore
	public void testGetReactive() throws Exception {
		mockMvc.perform(get("/reactive")).andExpect(content().json("{\"success\":true,\"message\":\"hello\"}"));
	}

}
