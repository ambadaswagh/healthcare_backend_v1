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
import com.healthcare.model.entity.CareGiver;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.CareGiverService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@Transactional
public class CareGiverControllerTest {
	private MockMvc mockMvc;

	@MockBean
	private CareGiverService careGiverService;

	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testSaveCareGiver() throws Exception {
		CareGiver CareGiver = new CareGiver();
		Mockito.when(careGiverService.save(CareGiver)).thenReturn(CareGiver);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(CareGiver);
		this.mockMvc.perform(post("/api/caregiver").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetCareGiver() throws Exception {
		Mockito.when(careGiverService.findById(1L)).thenReturn(new CareGiver());
		this.mockMvc.perform(get("/api/caregiver/")).andExpect(status().isOk());
	}

	@Test
	public void testFindAllCareGiver() throws Exception {
        ArrayList<CareGiver> list = new ArrayList<>();
        Pageable pageable = new CustomPageRequest();
        CareGiver admin = new CareGiver();
        PageImpl<CareGiver> page = new PageImpl<CareGiver>(list, pageable, list.size());
		Mockito.when(careGiverService.findAll(admin, pageable)).thenReturn(page);
		this.mockMvc.perform(get("/api/caregiver")).andExpect(status().isOk());
	}

	@Test
	public void testUpdateCareGiver() throws Exception {
		CareGiver CareGiver = new CareGiver();
		Mockito.when(careGiverService.save(CareGiver)).thenReturn(CareGiver);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(CareGiver);
		this.mockMvc.perform(put("/api/caregiver").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testDeleteCareGiver() throws Exception {
		Mockito.when(careGiverService.deleteById(1L)).thenReturn(1L);
		this.mockMvc.perform(delete("/api/caregiver/1")).andExpect(status().isOk());
	}
}
