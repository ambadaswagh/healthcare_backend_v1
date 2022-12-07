package com.healthcare.integration.service;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.healthcare.model.entity.Document;
import com.healthcare.model.entity.HealthInsuranceClaim;
import com.healthcare.model.entity.User;
import com.healthcare.repository.HealthInsuranceClaimRepository;
import com.healthcare.service.HealthInsuranceClaimService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class HealthInsuranceClaimServiceRedisTest {
	@Autowired
	private HealthInsuranceClaimService healthInsuranceClaimService;

	@MockBean
	private HealthInsuranceClaimRepository healthInsuranceClaimRepository;
	
	private HealthInsuranceClaim healthInsuranceClaim;
	private User user =null;
	private Document document =null;
	
	
	@Autowired
	private TestEntityServiceFactory testEntitySericeFactory;
	Long id = 1L;
	
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
		healthInsuranceClaim.setId(id);
		Mockito.when(healthInsuranceClaimRepository.save(healthInsuranceClaim)).thenReturn(healthInsuranceClaim);
		healthInsuranceClaim = healthInsuranceClaimService.save(healthInsuranceClaim);
		
		HealthInsuranceClaim healthInsuranceClaimSaved = healthInsuranceClaimService.findById(healthInsuranceClaim.getId());
		Assert.assertNotNull(healthInsuranceClaimSaved);
	}

	@Test
	public void testUpdateHealthInsuranceClaim() {
		String old = "serviceFacilityLocationInformationA";
		String newValue  = "serviceFacilityLocationInformationA updated";

		healthInsuranceClaim = TestEntityFactory.createNewHealthInsuraceClaim(user, document);
		healthInsuranceClaim.setId(id);
		Mockito.when(healthInsuranceClaimRepository.save(healthInsuranceClaim)).thenReturn(healthInsuranceClaim);
		healthInsuranceClaim = healthInsuranceClaimService.save(healthInsuranceClaim);
		
		HealthInsuranceClaim hicSaved = healthInsuranceClaimService.findById(healthInsuranceClaim.getId());
		hicSaved.setServiceFacilityLocationInformationA(newValue);
		Mockito.when(healthInsuranceClaimService.save(hicSaved)).thenReturn(hicSaved);
		hicSaved = healthInsuranceClaimService.save(hicSaved);
		
		HealthInsuranceClaim hicModified = healthInsuranceClaimService.findById(healthInsuranceClaim.getId());
		Assert.assertEquals(hicModified.getServiceFacilityLocationInformationA(), newValue);
	}

	@Test
	public void testDeleteHealthInsuranceClaim() {
		healthInsuranceClaim = TestEntityFactory.createNewHealthInsuraceClaim(user, document);
		healthInsuranceClaim.setId(id);
		Mockito.when(healthInsuranceClaimRepository.save(healthInsuranceClaim)).thenReturn(healthInsuranceClaim);
		healthInsuranceClaim = healthInsuranceClaimService.save(healthInsuranceClaim);
		Mockito.doNothing().when(healthInsuranceClaimRepository).delete(id);
		Assert.assertNotNull(healthInsuranceClaimService.deleteById(healthInsuranceClaim.getId()));
	}

}
