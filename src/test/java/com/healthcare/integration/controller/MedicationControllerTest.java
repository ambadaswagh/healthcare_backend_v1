package com.healthcare.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import com.healthcare.api.MedicationController;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.Medication;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.MedicationService;
import com.healthcare.service.impl.CustomUserValidator;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@Transactional
public class MedicationControllerTest {
	private MockMvc mockMvc;

	@Mock
	private MedicationService medicationService;

	String MEDICATION_API = "/api/medication";

	@Before
	public void setUp() {
		MedicationController controller = new MedicationController(medicationService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

	}

	@Test
	public void testSaveMedication() throws Exception {
		Medication medication = new Medication();
		Mockito.when(medicationService.save(medication)).thenReturn(medication);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(medication);
		this.mockMvc.perform(post(MEDICATION_API).contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetMedication() throws Exception {
		Mockito.when(medicationService.findById(1L)).thenReturn(new Medication());
		this.mockMvc.perform(get(MEDICATION_API + "/1")).andExpect(status().isOk());
	}

	@Test
	public void testFindAllMedication() throws Exception {
		List<Medication> list = new ArrayList<>();
		Pageable pageable = new CustomPageRequest();
		Medication medication = new Medication();
		PageImpl<Medication> page = new PageImpl<Medication>(list, pageable, list.size());
		Mockito.when(medicationService.findAll(medication, pageable)).thenReturn(page);
		this.mockMvc.perform(get(MEDICATION_API)).andExpect(status().isOk());
	}

	@Test
	public void testUpdateMedication() throws Exception {
		Medication medication = new Medication();
		Mockito.when(medicationService.save(medication)).thenReturn(medication);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(medication);
		this.mockMvc.perform(put(MEDICATION_API).contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testDeleteMedication() throws Exception {
		Mockito.when(medicationService.deleteById(1L)).thenReturn(1L);
		this.mockMvc.perform(delete(MEDICATION_API + "/1")).andExpect(status().isOk());
	}
}
