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
import com.healthcare.model.entity.Report;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.ReportService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@Transactional
public class ReportControllerTest {
	private MockMvc mockMvc;

	@MockBean
	private ReportService reportService;

	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testSaveReport() throws Exception {
		Report report = new Report();
		Mockito.when(reportService.save(report)).thenReturn(report);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(report);
		this.mockMvc.perform(post("/api/report").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetReport() throws Exception {
		Mockito.when(reportService.findById(1L)).thenReturn(new Report());
		this.mockMvc.perform(get("/api/report/1")).andExpect(status().isOk());
	}

	@Test
	public void testFindAllReport() throws Exception {
        List<Report> list = new ArrayList<>();
        Pageable pageable = new CustomPageRequest();
        Report report = new Report();
        PageImpl<Report> page = new PageImpl<Report>(list, pageable, list.size());
		Mockito.when(reportService.findAll(report, pageable)).thenReturn(page);
		this.mockMvc.perform(get("/api/report")).andExpect(status().isOk());
	}

	@Test
	public void testUpdateReport() throws Exception {
		Report report = new Report();
		Mockito.when(reportService.save(report)).thenReturn(report);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(report);
		this.mockMvc.perform(put("/api/report").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testDeleteReport() throws Exception {
		Mockito.when(reportService.deleteById(1L)).thenReturn(1L);
		this.mockMvc.perform(get("/api/report/1")).andExpect(status().isOk());
	}
}
