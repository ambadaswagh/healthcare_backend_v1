package com.healthcare.integration.service;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.healthcare.EntityFactory;
import com.healthcare.model.entity.InsuranceBrokerCompany;
import com.healthcare.model.enums.EntityStatusEnum;
import com.healthcare.service.InsuranceBrokerCompanyService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class InsuranceBrokerCompanyServiceTest extends EntityFactory {
	@Autowired
	private InsuranceBrokerCompanyService insuranceBrokerCompanyService;

	@Test
	public void shouldSaveAnInsuranceBrokerCompany() {
		InsuranceBrokerCompany insuranceBrokerCompany = insuranceBrokerCompanyService
				.save(createNewInsuranceBrokerCompany());
		Assert.assertNotNull(insuranceBrokerCompany.getId());
	}

	@Test
	public void shouldGetAnInsuranceBrokerCompany() {
		InsuranceBrokerCompany insuranceBrokerCompany = insuranceBrokerCompanyService
				.save(createNewInsuranceBrokerCompany());
		Assert.assertNotNull(insuranceBrokerCompany.getId());
		InsuranceBrokerCompany insuranceBrokerCompanySaved = insuranceBrokerCompanyService
				.findById(insuranceBrokerCompany.getId());
		Assert.assertNotNull(insuranceBrokerCompanySaved);
	}

	@Test
	public void shouldUpdateAnInsuranceBrokerCompany() {
		InsuranceBrokerCompany insuranceBrokerCompany = insuranceBrokerCompanyService
				.save(createNewInsuranceBrokerCompany());
		Assert.assertNotNull(insuranceBrokerCompany.getId());
		InsuranceBrokerCompany insuranceBrokerCompanySaved = insuranceBrokerCompanyService
				.findById(insuranceBrokerCompany.getId());
		Assert.assertNotNull(insuranceBrokerCompanySaved);
		String newCode = "QBE-A";
		insuranceBrokerCompanySaved.setCode(newCode);
		insuranceBrokerCompanyService.save(insuranceBrokerCompanySaved);
		InsuranceBrokerCompany insuranceBrokerCompanyUpdated = insuranceBrokerCompanyService
				.findById(insuranceBrokerCompany.getId());
		Assert.assertEquals(newCode, insuranceBrokerCompanyUpdated.getCode());
	}

	@Test
	public void shouldDeleteAnInsuranceBrokerCompany() {
		InsuranceBrokerCompany insuranceBrokerCompany = insuranceBrokerCompanyService
				.save(createNewInsuranceBrokerCompany());
		Assert.assertNotNull(insuranceBrokerCompany.getId());
		insuranceBrokerCompanyService.deleteById(insuranceBrokerCompany.getId());
		Assert.assertNull(insuranceBrokerCompanyService.findById(insuranceBrokerCompany.getId()));
	}

	@Test
	public void shouldDisabledAnInsuranceBrokerCompany() {
		InsuranceBrokerCompany insuranceBrokerCompany = insuranceBrokerCompanyService
				.save(createNewInsuranceBrokerCompany());
		Assert.assertNotNull(insuranceBrokerCompany.getId());
		InsuranceBrokerCompany insuranceBrokerCompanySaved = insuranceBrokerCompanyService.save(insuranceBrokerCompany);
		Assert.assertNotNull(insuranceBrokerCompanySaved);
		Long disableVisitId = insuranceBrokerCompanyService.disableById(insuranceBrokerCompany.getId());
		Assert.assertNotNull(disableVisitId);
		InsuranceBrokerCompany disableInsuranceBrokerCompany = insuranceBrokerCompanyService.findById(disableVisitId);
		Assert.assertEquals(EntityStatusEnum.DISABLE.getValue(), disableInsuranceBrokerCompany.getStatus().intValue());
	}

}
