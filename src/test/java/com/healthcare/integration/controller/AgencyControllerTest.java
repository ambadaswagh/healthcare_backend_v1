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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.dto.UserDto;
import com.healthcare.model.entity.Agency;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.AgencyService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@Transactional
public class AgencyControllerTest {
	private MockMvc mockMvc;

	@MockBean
	private AgencyService agencyService;

	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testSaveAgency() throws Exception {
		Agency agency = new Agency();
		Mockito.when(agencyService.save(agency)).thenReturn(agency);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(agency);
		this.mockMvc.perform(post("/api/agency").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetAgency() throws Exception {
		Mockito.when(agencyService.findById(1L)).thenReturn(new Agency());
		this.mockMvc.perform(get("/api/agency/1")).andExpect(status().isOk());
	}

	@Test
	public void testFindAllAgency() throws Exception {
        ArrayList<Agency> list = new ArrayList<>();
        Pageable pageable = new CustomPageRequest();
        Agency agecy = new Agency();
        PageImpl<Agency> page = new PageImpl<Agency>(list, pageable, list.size());
		Mockito.when(agencyService.findAll(agecy, pageable)).thenReturn(page);
		this.mockMvc.perform(get("/api/agency")).andExpect(status().isOk());
	}

	@Test
	public void testUpdateAgency() throws Exception {
		Agency agency = new Agency();
		Mockito.when(agencyService.save(agency)).thenReturn(agency);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(agency);
		this.mockMvc.perform(put("/api/agency").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testDeleteAgency() throws Exception {
		Mockito.when(agencyService.deleteById(1L)).thenReturn(1L);
		this.mockMvc.perform(get("/api/agency/1")).andExpect(status().isOk());
	}
	
	@Test
	public void testGenerateUserStats() throws Exception {
		Mockito.when(agencyService.generateUserStats(1L)).thenReturn(new UserDto());
		this.mockMvc.perform(get("/api/agency/stats/1")).andExpect(status().isOk());
	}

	@Test
	public void testDisableAgency() throws Exception {
		Mockito.when(agencyService.disableById(1L)).thenReturn(1L);
		this.mockMvc.perform(put("/api/agency/1/disable")).andExpect(status().isOk());
	}
}
