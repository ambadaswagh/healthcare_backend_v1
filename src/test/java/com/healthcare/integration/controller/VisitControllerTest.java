package com.healthcare.integration.controller;

import static com.healthcare.api.common.HealthcareConstants.AUTHENTICATED_ADMIN;
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
import com.healthcare.EntityFactory;
import com.healthcare.api.model.VisitReportRequest;
import com.healthcare.api.model.VisitRequest;
import com.healthcare.dto.BasicReportFilterDTO;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.Agency;
import com.healthcare.model.entity.AgencyType;
import com.healthcare.model.entity.Company;
import com.healthcare.model.entity.Role;
import com.healthcare.model.entity.User;
import com.healthcare.model.entity.Visit;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.AgencyService;
import com.healthcare.service.AgencyTypeService;
import com.healthcare.service.CompanyService;
import com.healthcare.service.UserService;
import com.healthcare.service.VisitService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@Transactional
public class VisitControllerTest extends EntityFactory {
	private MockMvc mockMvc;
	private Visit visit;
	private VisitRequest visitRequest;
	private VisitReportRequest visitReportRequest;

	@Autowired
	private WebApplicationContext wac;
	@MockBean
	private VisitService visitService;
	
	@Autowired
	private AgencyTypeService agencyTypeService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private AgencyService agencyService;
	
	@Autowired
	private UserService userService;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		AgencyType agencyType = createNewAgencyType();
		agencyTypeService.save(agencyType);
		Company company = createNewCompany();
		companyService.save(company);
		Agency agency = createNewAgency(agencyType, company);
		agencyService.save(agency);
		User user = createNewUser(agency);
		userService.save(user);
		visit = new Visit();
		visit.setPin(user.getPin());
		visit.setAgency(agency);
		visitRequest = new VisitRequest();
		visitReportRequest = new VisitReportRequest();
	}

	@Test
	public void shouldAcceptSaveVisitRequest() throws Exception {
		Mockito.when(visitService.save(visit)).thenReturn(visit);

		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(visit);

		this.mockMvc.perform(post("/api/visit").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void shouldAcceptGetVisitRequest() throws Exception {
		Mockito.when(visitService.findById(1L)).thenReturn(visit);
		this.mockMvc.perform(get("/api/visit/1")).andExpect(status().isOk());
	}

	@Test
	public void shouldAcceptFindAllByServicePlanIdRequest() throws Exception {
		Mockito.when(visitService.findAllByServicePlanId(1L)).thenReturn(new ArrayList<Visit>());
		this.mockMvc.perform(get("/api/visit/serviceplan/1")).andExpect(status().isOk());
	}

	@Test
	public void shouldAcceptUpdateVisitRequest() throws Exception {
		Mockito.when(visitService.save(visit)).thenReturn(visit);

		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(visit);

		this.mockMvc.perform(put("/api/visit").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void shouldAcceptDeleteVisitRequest() throws Exception {
		Mockito.when(visitService.deleteById(1L)).thenReturn(1L);
		this.mockMvc.perform(get("/api/visit/1")).andExpect(status().isOk());
	}

	@Test
	public void shouldCheckInVisitRequest() throws Exception {
		visitRequest.setId(11L);
		visitRequest.setCustomPin("0000-Fernando Paz");
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(visitRequest);

		this.mockMvc.perform(put("/api/visit/checkin").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void shouldNotCheckInVisitRequest() throws Exception {
		visitRequest.setId(null);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(visitRequest);

		this.mockMvc.perform(put("/api/visit/checkin").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldCheckoutVisitRequest() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		visitRequest.setId(11L);
		visitRequest.setCustomPin("0000-Fernando Paz");
		String jsonInString = mapper.writeValueAsString(visitRequest);

		this.mockMvc.perform(put("/api/visit/checkout").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void shouldNotCheckOutVisitRequest() throws Exception {
		visitRequest.setId(null);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(visitRequest);

		this.mockMvc.perform(put("/api/visit/checkout").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testFindAllVisit() throws Exception {
        List<Visit> list = new ArrayList<>();
        Pageable pageable = new CustomPageRequest();
        Visit visit = new Visit();
        PageImpl<Visit> page = new PageImpl<Visit>(list, pageable, list.size());
		Mockito.when(visitService.findAll(visit, pageable)).thenReturn(page);
		this.mockMvc.perform(get("/api/visit")).andExpect(status().isOk());
	}

	@Test
	public void testGetAllVisitReport() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		visitReportRequest.setStartDate(new Date());
		visitReportRequest.setEndDate(new Date());
		String jsonInString = mapper.writeValueAsString(visitReportRequest);

		this.mockMvc.perform(post("/api/visit/report").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testDisableVisit() throws Exception {
		Mockito.when(visitService.disableById(1L)).thenReturn(1L);
		this.mockMvc.perform(put("/api/visit/1/disable")).andExpect(status().isOk());
	}

	@Test
	public void testGenerateReport() throws Exception {
		BasicReportFilterDTO visitReportReq = new BasicReportFilterDTO();
        visitReportReq.setStartDate(new Date());

        Admin admin = new Admin();
        Role role = new Role();
        role.setLevel(1);
        admin.setRole(role);

        ObjectMapper mapper = new ObjectMapper();

		String jsonInString = mapper.writeValueAsString(visitReportReq);

        this.mockMvc.perform(post("/api/visit/visit_report").requestAttr(AUTHENTICATED_ADMIN,admin).contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void shouldBillVisit() throws Exception {
		String visitId = "1";
		String billingCode = "1239us";

		Mockito.when(visitService.billVisit(billingCode, Long.valueOf(visitId))).thenReturn(visit);

		this.mockMvc.perform(put("/api/visit/bill")
				.param("visiId", visitId)
				.param("billingCode", billingCode))
				.andExpect(status().isOk());
	}
}
