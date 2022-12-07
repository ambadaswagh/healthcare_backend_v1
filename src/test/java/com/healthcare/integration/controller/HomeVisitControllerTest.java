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
import com.healthcare.model.entity.HomeVisit;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.HomeVisitService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@Transactional
public class HomeVisitControllerTest {
	private MockMvc mockMvc;

	public static final String API_HOMEVISIT = "/api/homeVisit";
	
	@MockBean
	private HomeVisitService homeVisitService;

	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testSaveHomeVisit() throws Exception {
		HomeVisit HomeVisit = new HomeVisit();
		Mockito.when(homeVisitService.save(HomeVisit)).thenReturn(HomeVisit);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(HomeVisit);
		this.mockMvc.perform(post(API_HOMEVISIT).contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetHomeVisit() throws Exception {
		Mockito.when(homeVisitService.findById(1L)).thenReturn(new HomeVisit());
		this.mockMvc.perform(get(API_HOMEVISIT)).andExpect(status().isOk());
	}

	@Test
	public void testFindAllHomeVisit() throws Exception {
        ArrayList<HomeVisit> list = new ArrayList<>();
        Pageable pageable = new CustomPageRequest();
        HomeVisit homeVisit = new HomeVisit();
        PageImpl<HomeVisit> page = new PageImpl<HomeVisit>(list, pageable, list.size());
		Mockito.when(homeVisitService.findAll(homeVisit, pageable)).thenReturn(page);
		this.mockMvc.perform(get(API_HOMEVISIT)).andExpect(status().isOk());
	}

	@Test
	public void testUpdateHomeVisit() throws Exception {
		HomeVisit HomeVisit = new HomeVisit();
		Mockito.when(homeVisitService.save(HomeVisit)).thenReturn(HomeVisit);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(HomeVisit);
		this.mockMvc.perform(put(API_HOMEVISIT).contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testDeleteHomeVisit() throws Exception {
		Mockito.when(homeVisitService.deleteById(1L)).thenReturn(1L);
		this.mockMvc.perform(delete(API_HOMEVISIT+"/1")).andExpect(status().isOk());
	}
}
