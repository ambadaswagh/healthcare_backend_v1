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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.api.AdminController;
import com.healthcare.model.entity.Admin;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.AdminService;
import com.healthcare.service.impl.CustomUserValidator;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@Transactional
public class AdminControllerTest {
	private MockMvc mockMvc;

	@Mock
	private AdminService adminService;
	
	@Mock
	CustomUserValidator customUserValidator;
	
	String ADMIN_API = "/api/admin";
    

	@Before
	public void setUp() {
		AdminController controller = new AdminController( adminService,customUserValidator);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
 
	}

	@Test
	public void testSaveAdmin() throws Exception {
		Admin admin = new Admin();
		Mockito.when(adminService.save(admin)).thenReturn(admin);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(admin);
		this.mockMvc.perform(post(ADMIN_API).contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetAdmin() throws Exception {
		Mockito.when(adminService.findById(1L)).thenReturn(new Admin());
		this.mockMvc.perform(get(ADMIN_API+"/1")).andExpect(status().isOk());
	}

	@Test
	public void testFindAllAdmin() throws Exception {
        List<Admin> list = new ArrayList<>();
        Pageable pageable = new CustomPageRequest();
        Admin admin = new Admin();
        PageImpl<Admin> page = new PageImpl<Admin>(list, pageable, list.size());
		Mockito.when(adminService.findAll(admin, pageable)).thenReturn(page);
		this.mockMvc.perform(get(ADMIN_API)).andExpect(status().isOk());
	}

	@Test
	public void testUpdateAdmin() throws Exception {
		Admin admin = new Admin();
		Mockito.when(adminService.save(admin)).thenReturn(admin);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(admin);
		this.mockMvc.perform(put(ADMIN_API).contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testDeleteAdmin() throws Exception {
		Mockito.when(adminService.deleteById(1L)).thenReturn(1L);
		this.mockMvc.perform(get(ADMIN_API+"/1")).andExpect(status().isOk());
	}
}
