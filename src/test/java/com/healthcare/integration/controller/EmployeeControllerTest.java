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
import com.healthcare.api.model.EmployeeClockingReportRequest;
import com.healthcare.api.model.EmployeeClockingReportResponse;
import com.healthcare.api.model.TimeClockRequest;
import com.healthcare.model.entity.Employee;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.EmployeeClockingService;
import com.healthcare.service.EmployeeService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@Transactional
public class EmployeeControllerTest {
	private MockMvc mockMvc;

	@MockBean
	private EmployeeService employeeService;
	
	@MockBean
	private EmployeeClockingService employeeClockingService;

	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testSaveEmployee() throws Exception {
		Employee employee = new Employee();
		Mockito.when(employeeService.save(employee)).thenReturn(employee);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(employee);
		this.mockMvc.perform(post("/api/employee").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetEmployee() throws Exception {
		Mockito.when(employeeService.findById(1L)).thenReturn(new Employee());
		this.mockMvc.perform(get("/api/employee/")).andExpect(status().isOk());
	}

	@Test
	public void testFindAllEmployee() throws Exception {
        ArrayList<Employee> list = new ArrayList<>();
        Pageable pageable = new CustomPageRequest();
        Employee emp = new Employee();
        PageImpl<Employee> page = new PageImpl<Employee>(list, pageable, list.size());
		Mockito.when(employeeService.findAll(emp, pageable)).thenReturn(page);
		this.mockMvc.perform(get("/api/employee")).andExpect(status().isOk());
	}

	@Test
	public void testUpdateEmployee() throws Exception {
		Employee employee = new Employee();
		Mockito.when(employeeService.save(employee)).thenReturn(employee);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(employee);
		this.mockMvc.perform(put("/api/employee").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testDeleteEmployee() throws Exception {
		Mockito.when(employeeService.deleteById(1L)).thenReturn(1L);
		this.mockMvc.perform(delete("/api/employee/1")).andExpect(status().isOk());
	}

	@Test
	public void testDisableEmployee() throws Exception {
		Mockito.when(employeeService.disableById(1L)).thenReturn(1L);
		this.mockMvc.perform(put("/api/employee/1/disable")).andExpect(status().isOk());
	}
	
	@Test
	public void testTimeClock() throws Exception {
		TimeClockRequest timeClockRequest = new TimeClockRequest();
		Employee employee = new Employee();
		Mockito.when(employeeClockingService.timeClock(timeClockRequest)).thenReturn(employee);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(timeClockRequest);
		this.mockMvc.perform(post("/api/employee/timeclock").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testGenerateReport() throws Exception {
		EmployeeClockingReportRequest request = new EmployeeClockingReportRequest();
		EmployeeClockingReportResponse response = new EmployeeClockingReportResponse();
		Mockito.when(employeeClockingService.generateEmployeeClockingReport(request)).thenReturn(response);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(request);
		this.mockMvc.perform(post("/api/employee/report").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}
}
