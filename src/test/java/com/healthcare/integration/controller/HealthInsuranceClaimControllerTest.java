package com.healthcare.integration.controller;

import static com.healthcare.api.common.HealthcareConstants.AUTHENTICATED_ADMIN;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;

import javax.transaction.Transactional;

import com.healthcare.dto.BasicReportFilterDTO;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.Role;
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
import com.healthcare.model.entity.HealthInsuranceClaim;
import com.healthcare.service.HealthInsuranceClaimService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@Transactional
public class HealthInsuranceClaimControllerTest {
	private MockMvc mockMvc;

	public static final String API_HEALTH_INSURANCE_CLAIM = "/api/healthInsuranceClaim";
	
	@MockBean
	private HealthInsuranceClaimService healthInsuranceClaimService;

	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testSaveHealthInsuranceClaim() throws Exception {
		HealthInsuranceClaim healthInsuranceClaim = new HealthInsuranceClaim();
		Mockito.when(healthInsuranceClaimService.save(any())).thenReturn(healthInsuranceClaim);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(healthInsuranceClaim);
		this.mockMvc.perform(post(API_HEALTH_INSURANCE_CLAIM).contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetHealthInsuranceClaim() throws Exception {
		Mockito.when(healthInsuranceClaimService.findById(1L)).thenReturn(new HealthInsuranceClaim());
		this.mockMvc.perform(get(API_HEALTH_INSURANCE_CLAIM)).andExpect(status().isOk());
	}

	@Test
	public void testFindAllHealthInsuranceClaim() throws Exception {
		Mockito.when(healthInsuranceClaimService.findAll()).thenReturn(new ArrayList<HealthInsuranceClaim>());
		this.mockMvc.perform(get(API_HEALTH_INSURANCE_CLAIM)).andExpect(status().isOk());
	}
	
	@Test
	public void testFindByUserIdHealthInsuranceClaim() throws Exception {
		Mockito.when(healthInsuranceClaimService.findByUserId(1L)).thenReturn(new ArrayList<HealthInsuranceClaim>());
		this.mockMvc.perform(get(API_HEALTH_INSURANCE_CLAIM+"/user/1")).andExpect(status().isOk());
	}

	@Test
	public void testUpdateHealthInsuranceClaim() throws Exception {
		HealthInsuranceClaim HealthInsuranceClaim = new HealthInsuranceClaim();
		Mockito.when(healthInsuranceClaimService.save(any())).thenReturn(HealthInsuranceClaim);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(HealthInsuranceClaim);
		this.mockMvc.perform(put(API_HEALTH_INSURANCE_CLAIM).contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testDeleteHealthInsuranceClaim() throws Exception {
		Mockito.when(healthInsuranceClaimService.deleteById(1L)).thenReturn(1L);
		this.mockMvc.perform(delete(API_HEALTH_INSURANCE_CLAIM+"/1")).andExpect(status().isOk());
	}

	@Test
	public void testGenerateReport() throws Exception {
		Admin admin = new Admin();
		Role role = new Role();
		role.setLevel(1);
		admin.setRole(role);

        BasicReportFilterDTO billingReq = new BasicReportFilterDTO();
        billingReq.setStartDate(new Date());

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(billingReq);

        this.mockMvc.perform(post(API_HEALTH_INSURANCE_CLAIM + "/billing_report")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInString)
                .requestAttr(AUTHENTICATED_ADMIN, admin))
                .andExpect(status().isOk());
    }
}
