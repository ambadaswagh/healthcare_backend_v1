package com.healthcare.integration.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
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
import com.healthcare.model.entity.ServicePlan;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.ServicePlanService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@Transactional
public class ServicePlanControllerTest {
	
	private static final String SERVICEPLAN_ENDPOINT= "/api/serviceplan";
	private MockMvc mockMvc;

	@MockBean
	private ServicePlanService servicePlanService;

	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testSaveServicePlan() throws Exception {
		ServicePlan servicePlan = new ServicePlan();
		Mockito.when(servicePlanService.save(servicePlan)).thenReturn(servicePlan);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(servicePlan);
		this.mockMvc.perform(post(SERVICEPLAN_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetServicePlan() throws Exception {
		Mockito.when(servicePlanService.findById(1L)).thenReturn(new ServicePlan());
		this.mockMvc.perform(get("/api/serviceplan/1")).andExpect(status().isOk());
	}

	@Test
	public void testGetServiceCalendar() throws Exception {
		Mockito.when(servicePlanService.getServiceCalendar(1L)).thenReturn(new ArrayList<String>());
		this.mockMvc.perform(get("/api/serviceplan/calendar/1")).andExpect(status().isOk());
	}

	@Test
	public void testFindAllServicePlan() throws Exception {
        List<ServicePlan> list = new ArrayList<>();
        Pageable pageable = new CustomPageRequest();
        ServicePlan servicePlan = new ServicePlan();
        PageImpl<ServicePlan> page = new PageImpl<ServicePlan>(list, pageable, list.size());
		Mockito.when(servicePlanService.findAll(servicePlan, pageable)).thenReturn(page);
		this.mockMvc.perform(get(SERVICEPLAN_ENDPOINT)).andExpect(status().isOk());
	}

	@Test
	public void testUpdateServicePlan() throws Exception {
		ServicePlan servicePlan = new ServicePlan();
		Mockito.when(servicePlanService.save(servicePlan)).thenReturn(servicePlan);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(servicePlan);
		this.mockMvc.perform(put(SERVICEPLAN_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testDeleteServicePlan() throws Exception {
		Mockito.when(servicePlanService.deleteById(1L)).thenReturn(1L);
		this.mockMvc.perform(get(SERVICEPLAN_ENDPOINT+"/1")).andExpect(status().isOk());
	}
	
	@Test
	public void testserviceCalendarGeneration() throws Exception {
		// when
		when(servicePlanService.serviceCalendarGeneration(1L)).thenReturn(new ArrayList<Date>());
		//perform
		this.mockMvc.perform(get(SERVICEPLAN_ENDPOINT+"/calendar/homevisit/1")).andExpect(status().isOk());
		//verifying exact number of invocations
		verify(servicePlanService, times(1)).serviceCalendarGeneration(1L);
		//make sure that nothing else was invoked on your mocks.
		verifyNoMoreInteractions(servicePlanService);
	}
}
