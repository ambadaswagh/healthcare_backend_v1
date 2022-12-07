package com.healthcare.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.model.entity.EmployeeActivity;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.EmployeeActivityService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@Transactional
public class EmployeeActivityControllerTest {
	private MockMvc mockMvc;

	@MockBean
	private EmployeeActivityService employeeActivityService;

	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testSaveEmployeeActivity() throws Exception {
		EmployeeActivity employeeActivity = new EmployeeActivity();
		Mockito.when(employeeActivityService.save(employeeActivity)).thenReturn(employeeActivity);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(employeeActivity);
		this.mockMvc.perform(post("/api/employee_activity").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetEmployeeActivity() throws Exception {
		Mockito.when(employeeActivityService.findById(1L)).thenReturn(new EmployeeActivity());
		this.mockMvc.perform(get("/api/employee_activity/")).andExpect(status().isOk());
	}

	@Test
	public void testFindAllEmployeeActivity() throws Exception {
        ArrayList<EmployeeActivity> list = new ArrayList<>();
        Pageable pageable = new CustomPageRequest();
        EmployeeActivity empActivity = new EmployeeActivity();
        PageImpl<EmployeeActivity> page = new PageImpl<EmployeeActivity>(list, pageable, list.size());
		Mockito.when(employeeActivityService.findAll(empActivity, pageable)).thenReturn(page);
		this.mockMvc.perform(get("/api/employee_activity")).andExpect(status().isOk());
	}

	@Test
	public void testUpdateEmployeeActivity() throws Exception {
		EmployeeActivity employeeActivity = new EmployeeActivity();
		Mockito.when(employeeActivityService.save(employeeActivity)).thenReturn(employeeActivity);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(employeeActivity);
		this.mockMvc.perform(put("/api/employee_activity").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testDeleteEmployeeActivity() throws Exception {
		Mockito.when(employeeActivityService.deleteById(1L)).thenReturn(1L);
		this.mockMvc.perform(delete("/api/employee_activity/1")).andExpect(status().isOk());
	}
}
