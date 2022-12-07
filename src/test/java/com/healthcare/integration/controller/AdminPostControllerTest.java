package com.healthcare.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.model.entity.AdminPost;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.AdminPostService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@Transactional
public class AdminPostControllerTest {
	private MockMvc mockMvc;

	@MockBean
	private AdminPostService adminPostService;

	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testSaveAdminPost() throws Exception {
		AdminPost adminPost = new AdminPost();
		Mockito.when(adminPostService.save(adminPost)).thenReturn(adminPost);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(adminPost);
		this.mockMvc.perform(post("/api/adminpost").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetAdminPost() throws Exception {
		Mockito.when(adminPostService.findById(1L)).thenReturn(new AdminPost());
		this.mockMvc.perform(get("/api/adminpost/1")).andExpect(status().isOk());
	}

	@Test
	public void testFindAllAdminPost() throws Exception {
        List<AdminPost> list = new ArrayList<>();
        Pageable pageable = new CustomPageRequest();
        AdminPost admin = new AdminPost();
        PageImpl<AdminPost> page = new PageImpl<AdminPost>(list, pageable, list.size());
		Mockito.when(adminPostService.findAll(admin, pageable)).thenReturn(page);
		this.mockMvc.perform(get("/api/adminpost")).andExpect(status().isOk());
	}

	@Test
	public void testUpdateAdminPost() throws Exception {
		AdminPost adminPost = new AdminPost();
		Mockito.when(adminPostService.save(adminPost)).thenReturn(adminPost);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(adminPost);
		this.mockMvc.perform(put("/api/adminpost").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testDeleteAdminPost() throws Exception {
		Mockito.when(adminPostService.deleteById(1L)).thenReturn(1L);
		this.mockMvc.perform(get("/api/adminpost/1")).andExpect(status().isOk());
	}
}
