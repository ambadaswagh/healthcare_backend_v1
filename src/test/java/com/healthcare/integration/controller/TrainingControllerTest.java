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
import com.healthcare.model.entity.Training;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.TrainingService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@Transactional
public class TrainingControllerTest {
	private MockMvc mockMvc;

	@MockBean
	private TrainingService trainingService;

	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testSaveTraining() throws Exception {
		Training training = new Training();
		Mockito.when(trainingService.save(training)).thenReturn(training);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(training);
		this.mockMvc.perform(post("/api/training").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetTraining() throws Exception {
		Mockito.when(trainingService.findById(1L)).thenReturn(new Training());
		this.mockMvc.perform(get("/api/training/1")).andExpect(status().isOk());
	}

	@Test
	public void testFindAllTraining() throws Exception {
        List<Training> list = new ArrayList<>();
        Pageable pageable = new CustomPageRequest();
        Training training = new Training();
        PageImpl<Training> page = new PageImpl<Training>(list, pageable, list.size());
		Mockito.when(trainingService.findAll(training, pageable)).thenReturn(page);
		this.mockMvc.perform(get("/api/training")).andExpect(status().isOk());
	}

	@Test
	public void testUpdateTraining() throws Exception {
		Training training = new Training();
		Mockito.when(trainingService.save(training)).thenReturn(training);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(training);
		this.mockMvc.perform(put("/api/training").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testDeleteTraining() throws Exception {
		Mockito.when(trainingService.deleteById(1L)).thenReturn(1L);
		this.mockMvc.perform(get("/api/training/1")).andExpect(status().isOk());
	}

	@Test
	public void testDisableTraining() throws Exception {
		Mockito.when(trainingService.disableById(1L)).thenReturn(1L);
		this.mockMvc.perform(put("/api/training/1/disable")).andExpect(status().isOk());
	}
	
	@Test
	public void testFindByTrainee() throws Exception {
		Mockito.when(trainingService.findByTraineeId("1")).thenReturn(new ArrayList<>());
		this.mockMvc.perform(get("/api/training/trainee/1")).andExpect(status().isOk());
	}
}
