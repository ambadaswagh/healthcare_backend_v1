package com.healthcare.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.model.entity.User;
import com.healthcare.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@Transactional
public class AnalyticControllerTest {
	private MockMvc mockMvc;
	private User user;

	@Autowired
	private WebApplicationContext wac;
	@MockBean
	private UserService userService;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		user = new User();
	}

	@Test
	public void testGetUserBirthdayToday() throws Exception {
		this.mockMvc.perform(get("/api/analytic/birthday")).andExpect(status().isOk());
		this.mockMvc.perform(get("/api/analytic/birthday/")).andExpect(status().is(200));
	}
	
	@Test
	public void testGetUserBirthdayOnDate() throws Exception {
		this.mockMvc.perform(get("/api/analytic/birthday/2017-07-05")).andExpect(status().isOk());
	}
	
}
