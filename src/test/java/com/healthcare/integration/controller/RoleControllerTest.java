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
import com.healthcare.model.entity.Role;
import com.healthcare.service.RoleService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@Transactional
public class RoleControllerTest {
	private MockMvc mockMvc;

	@MockBean
	private RoleService roleService;

	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testSaveRole() throws Exception {
		Role role = new Role();
		Mockito.when(roleService.save(role)).thenReturn(role);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(role);
		this.mockMvc.perform(post("/api/role").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetRole() throws Exception {
		Mockito.when(roleService.findById(1L)).thenReturn(new Role());
		this.mockMvc.perform(get("/api/role/1")).andExpect(status().isOk());
	}

	@Test
	public void testFindAllRole() throws Exception {
		Mockito.when(roleService.findAll()).thenReturn(new ArrayList<Role>());
		this.mockMvc.perform(get("/api/role")).andExpect(status().isOk());
	}

	@Test
	public void testUpdateRole() throws Exception {
		Role role = new Role();
		Mockito.when(roleService.save(role)).thenReturn(role);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(role);
		this.mockMvc.perform(put("/api/role").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testDeleteAdmin() throws Exception {
		Mockito.when(roleService.deleteById(1L)).thenReturn(1L);
		this.mockMvc.perform(get("/api/role/1")).andExpect(status().isOk());
	}

	@Test
	public void testDisableRole() throws Exception {
		Mockito.when(roleService.disableById(1L)).thenReturn(1L);
		this.mockMvc.perform(put("/api/role/1/disable")).andExpect(status().isOk());
	}

	@Test
	public void testFindRoleByLevel() throws Exception {
		Mockito.when(roleService.findByLevel(1L)).thenReturn(new Role());
		this.mockMvc.perform(get("/api/role/level/1")).andExpect(status().isOk());
	}

	@Test
	public void testFindRoleByStatus() throws Exception {
		Mockito.when(roleService.findByStatus(1L)).thenReturn(new ArrayList<Role>());
		this.mockMvc.perform(get("/api/role/status/1")).andExpect(status().isOk());
	}
}
