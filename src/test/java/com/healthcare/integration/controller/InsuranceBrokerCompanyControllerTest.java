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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.EntityFactory;
import com.healthcare.model.entity.InsuranceBrokerCompany;
import com.healthcare.service.InsuranceBrokerCompanyService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@Transactional
public class InsuranceBrokerCompanyControllerTest extends EntityFactory {
	private MockMvc mockMvc;
	private InsuranceBrokerCompany insuranceBrokerCompany;
	@Autowired
	private WebApplicationContext wac;
	@MockBean
	private InsuranceBrokerCompanyService insuranceBrokerCompanyService;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		insuranceBrokerCompany = createNewInsuranceBrokerCompany();
	}

	@Test
	public void shouldAcceptSaveInsuranceBrokerCompanyRequest() throws Exception {
		Mockito.when(insuranceBrokerCompanyService.save(insuranceBrokerCompany)).thenReturn(insuranceBrokerCompany);

		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(insuranceBrokerCompany);

		this.mockMvc.perform(
				post("/api/insuranceBrokerCompany").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isCreated());
	}

	@Test
	public void shouldAcceptGetInsuranceBrokerCompanyRequest() throws Exception {
		Mockito.when(insuranceBrokerCompanyService.findById(1L)).thenReturn(insuranceBrokerCompany);
		this.mockMvc.perform(get("/api/insuranceBrokerCompany/1")).andExpect(status().isOk());
	}

	@Test
	public void shouldAcceptUpdateInsuranceBrokerCompanyRequest() throws Exception {
		Mockito.when(insuranceBrokerCompanyService.save(insuranceBrokerCompany)).thenReturn(insuranceBrokerCompany);

		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(insuranceBrokerCompany);

		this.mockMvc.perform(
				put("/api/insuranceBrokerCompany").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void shouldAcceptDeleteInsuranceBrokerCompanyRequest() throws Exception {
		Mockito.when(insuranceBrokerCompanyService.deleteById(1L)).thenReturn(1L);
		this.mockMvc.perform(delete("/api/insuranceBrokerCompany/1")).andExpect(status().isOk());
	}

	@Test
	public void shouldAcceptFindAllInsuranceBrokerCompaniesRequest() throws Exception {
		Mockito.when(insuranceBrokerCompanyService.findAll()).thenReturn(new ArrayList<InsuranceBrokerCompany>());
		this.mockMvc.perform(get("/api/insuranceBrokerCompany")).andExpect(status().isOk());
	}

	@Test
	public void shouldAcceptDisableInsuranceBrokerCompany() throws Exception {
		Mockito.when(insuranceBrokerCompanyService.disableById(1L)).thenReturn(1L);
		this.mockMvc.perform(put("/api/insuranceBrokerCompany/1/disable")).andExpect(status().isOk());
	}
}
