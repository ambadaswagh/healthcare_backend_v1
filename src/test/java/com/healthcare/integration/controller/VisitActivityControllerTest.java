package com.healthcare.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import com.healthcare.model.entity.VisitActivity;
import com.healthcare.model.entity.VisitActivityPK;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.VisitActivityService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@Transactional
public class VisitActivityControllerTest {
	private MockMvc mockMvc;

	@MockBean
	private VisitActivityService visitActivityService;

	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testSaveVisitActivity() throws Exception {
		VisitActivity visitActivity = getVisitActivity();

		Mockito.when(visitActivityService.save(visitActivity)).thenReturn(visitActivity);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(visitActivity);
		this.mockMvc.perform(put("/api/visitActivity").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testUpdateVisitActivity() throws Exception {
		VisitActivity visitActivity = getVisitActivity();
		VisitActivity visitActivityModified = visitActivity;
		visitActivityModified.setTableName("table name updated");

		Mockito.when(visitActivityService.save(visitActivityModified)).thenReturn(visitActivityModified);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(visitActivity);
		this.mockMvc.perform(put("/api/visitActivity").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk()).andExpect(jsonPath("tableName").value("table name updated"));
	}

	private VisitActivity getVisitActivity() {
		VisitActivity visitActivity = new VisitActivity();
		visitActivity.setActivityId(1L);
		visitActivity.setVisitId(1L);
		visitActivity.setTableName("table name");
		return visitActivity;
	}

	@Test
	public void testGetVisitActivity() throws Exception {
		VisitActivityPK pk = getPk();

		VisitActivity visitActivity = getVisitActivity();
		Mockito.when(visitActivityService.findById(pk)).thenReturn(visitActivity);
		this.mockMvc.perform(get("/api/visitActivity/visit/1/activity/1")).andExpect(status().isOk())
				.andExpect(jsonPath("tableName").value("table name"));
	}

	private VisitActivityPK getPk() {
		return new VisitActivityPK(1L, 1L);
	}

	@Test
	public void testDeleteVisitActivity() throws Exception {
		VisitActivityPK pk = getPk();
		Mockito.when(visitActivityService.deleteById(pk)).thenReturn(1L);
		this.mockMvc.perform(delete("/api/visitActivity/visit/1/activity/1")).andExpect(status().isOk());
	}

	@Test
	public void testFindAllVisitActivityByActivityId() throws Exception {
        List<VisitActivity> list = new ArrayList<>();
        Pageable pageable = new CustomPageRequest();
        PageImpl<VisitActivity> page = new PageImpl<VisitActivity>(list, pageable, list.size());
		Mockito.when(visitActivityService.findVisitActivityByActivityId(1L, pageable)).thenReturn(page);
		this.mockMvc.perform(get("/api/visitActivity/activity/1")).andExpect(status().isOk());
	}

	@Test
	public void testFindAllVisitActivityByVisitId() throws Exception {
        List<VisitActivity> list = new ArrayList<>();
        Pageable pageable = new CustomPageRequest();
        PageImpl<VisitActivity> page = new PageImpl<VisitActivity>(list, pageable, list.size());
		Mockito.when(visitActivityService.findVisitActivityByActivityId(1L, pageable)).thenReturn(page);
		this.mockMvc.perform(get("/api/visitActivity/visit/1")).andExpect(status().isOk());
	}
}
