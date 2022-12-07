package com.healthcare.integration.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.healthcare.dto.BasicReportFilterDTO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.healthcare.model.entity.Document;
import com.healthcare.model.entity.HealthInsuranceClaim;
import com.healthcare.model.entity.HealthInsuranceClaim;
import com.healthcare.model.entity.User;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.HealthInsuranceClaimService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class HealthInsuranceClaimServiceTest {
	@Autowired
	private HealthInsuranceClaimService healthInsuranceClaimService;
	
	@Value("${spring.data.rest.default-page-size}")
	private int defaultMaxSize;

	Calendar postDate = Calendar.getInstance();
	String postText = "This is post text";

	private HealthInsuranceClaim healthInsuranceClaim;
	private User user =null;
	private Document document =null;
	
	
	@Autowired
	private TestEntityServiceFactory testEntitySericeFactory;
	
	
	
	@Before
	public void setup() {
		testEntitySericeFactory.saveData = true;
		user = testEntitySericeFactory.createUser();
		document = testEntitySericeFactory.creatDocument();
		healthInsuranceClaim = null;
	}
	
	@After
	public void rollback() {
		if(healthInsuranceClaim!=null){
			healthInsuranceClaimService.deleteById(healthInsuranceClaim.getId());
		}
		testEntitySericeFactory.redisCleanUp();
	}

	@Test
	public void testSaveHealthInsuranceClaim() {
		healthInsuranceClaim = TestEntityFactory.createNewHealthInsuraceClaim(user, document);
		healthInsuranceClaim = healthInsuranceClaimService.save(healthInsuranceClaim);
		Assert.assertNotNull(healthInsuranceClaim.getId());
	}


	public void testGenerateReport() {
		healthInsuranceClaim = TestEntityFactory.createNewHealthInsuraceClaim(user, document);
		healthInsuranceClaim = healthInsuranceClaimService.save(healthInsuranceClaim);

		BasicReportFilterDTO bReq = new BasicReportFilterDTO();
		bReq.setStartDate(new Date());
		bReq.setEndDate(new Date());

		List<HealthInsuranceClaim> responseList = healthInsuranceClaimService.findHealthInsuranceReport(bReq);

		Assert.assertNotNull(responseList);
        Assert.assertEquals(1,responseList.size());
	}

	@Test
	public void testGetHealthInsuranceClaim() {
		healthInsuranceClaim = TestEntityFactory.createNewHealthInsuraceClaim(user, document);
		healthInsuranceClaimService.save(healthInsuranceClaim);
		Assert.assertNotNull(healthInsuranceClaimService.findById(healthInsuranceClaim.getId()));
	}

	@Test
	public void testUpdateHealthInsuranceClaim() {
		String old = "serviceFacilityLocationInformationA";
		String newValue  = "serviceFacilityLocationInformationA updated";

		healthInsuranceClaim = TestEntityFactory.createNewHealthInsuraceClaim(user, document);
		healthInsuranceClaimService.save(healthInsuranceClaim);
		Assert.assertEquals(healthInsuranceClaim.getServiceFacilityLocationInformationA(), old);
		
		HealthInsuranceClaim adminPostSaved = healthInsuranceClaimService.findById(healthInsuranceClaim.getId());
		adminPostSaved.setServiceFacilityLocationInformationA(newValue);
		healthInsuranceClaimService.save(adminPostSaved);
		HealthInsuranceClaim adminPostModified = healthInsuranceClaimService.findById(healthInsuranceClaim.getId());
		Assert.assertEquals(adminPostModified.getServiceFacilityLocationInformationA(), newValue);
	}

	@Test
	public void testDeleteHealthInsuranceClaim() {
		healthInsuranceClaim = TestEntityFactory.createNewHealthInsuraceClaim(user, document);
		healthInsuranceClaim = healthInsuranceClaimService.save(healthInsuranceClaim);
		Assert.assertNotNull(healthInsuranceClaim.getId());
		healthInsuranceClaimService.deleteById(healthInsuranceClaim.getId());
		Assert.assertNull(healthInsuranceClaimService.findById(healthInsuranceClaim.getId()));
		healthInsuranceClaim =null;
	}
	@Test
	public void testFindAllPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			HealthInsuranceClaim obj = healthInsuranceClaimService.save(TestEntityFactory.createNewHealthInsuraceClaim(user, document));
			listIds.add(obj.getId());
		}

		// PAGE 0
		// given
		Pageable pageable = new CustomPageRequest();
		// when
		Page<HealthInsuranceClaim> result = healthInsuranceClaimService.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == defaultMaxSize);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 0);

		// PAGE 2
		pageable = new PageRequest(1, 5);
		// when
		result = healthInsuranceClaimService.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == 5);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 1);

		// delete created data
		for (Long id : listIds) {
			healthInsuranceClaimService.deleteById(id);
		}
	}

	@Test
	public void testSearchWithPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			HealthInsuranceClaim obj = healthInsuranceClaimService.save(TestEntityFactory.createNewHealthInsuraceClaim(user, document));
			listIds.add(obj.getId());
		}
		// SEARCH 1 : FIND BY ID
		// given
		Pageable pageable = new PageRequest(0, 25);
		HealthInsuranceClaim obj = new HealthInsuranceClaim();
		obj.setId(listIds.get(0));
		obj.setBalanceDue(new Double(9.11));
		obj.setAmountPaid(new Double(112.55));
		obj.setCharges(new Double(10.11));
		obj.setTotalCharge(new Double(10.11));

		// when
		Page<HealthInsuranceClaim> result = healthInsuranceClaimService.findAll(obj, pageable);
		// then
		assertTrue(result.getContent().size() == 1);
		assertTrue(result.getTotalElements() >= 1);
		assertTrue(result.getTotalPages() >= 1);
		assertTrue(result.getNumber() == 0);

		// SEARCH 2
		// given
		pageable = new PageRequest(3, 5);
		obj = new HealthInsuranceClaim();
		obj.setTotalCharge(new Double(10.11));
		obj.setBalanceDue(new Double(9.11));
		obj.setAmountPaid(new Double(112.55));
		obj.setCharges(new Double(10.11));

		// when
		result = healthInsuranceClaimService.findAll(obj, pageable);
		// then
		assertTrue(result.getContent().size() == 5);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 3);

		// Delete created data
		for (Long id : listIds) {
			healthInsuranceClaimService.deleteById(id);
		}
	}
}
