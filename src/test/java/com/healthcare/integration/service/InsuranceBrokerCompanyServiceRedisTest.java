package com.healthcare.integration.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.healthcare.EntityFactory;
import com.healthcare.model.entity.InsuranceBrokerCompany;
import com.healthcare.model.enums.EntityStatusEnum;
import com.healthcare.repository.InsuranceBrokerCompanyRepository;
import com.healthcare.service.InsuranceBrokerCompanyService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class InsuranceBrokerCompanyServiceRedisTest extends EntityFactory {
	private Long id = 10L;

	@MockBean
	private InsuranceBrokerCompanyRepository insuranceBrokerCompanyRepository;

	@Autowired
	private InsuranceBrokerCompanyService insuranceBrokerCompanyService;
	
	@Autowired
	private RedisTemplate<String, InsuranceBrokerCompanyRepository> redisTemplate;
	
	@Before
	public void setup(){
		redisTemplate.delete(InsuranceBrokerCompany.class.getSimpleName());
	}

	@Test
	public void shouldSaveAnInsuranceBrokerCompanyToRedis() {
		InsuranceBrokerCompany insuranceBrokerCompany = createNewInsuranceBrokerCompany();
		insuranceBrokerCompany.setId(id);
		Mockito.when(insuranceBrokerCompanyRepository.save(insuranceBrokerCompany)).thenReturn(insuranceBrokerCompany);
		insuranceBrokerCompanyService.save(insuranceBrokerCompany);
		InsuranceBrokerCompany insuranceBrokerCompanySaved = insuranceBrokerCompanyService.findById(id);
		Assert.assertNotNull(insuranceBrokerCompanySaved);
	}

	@Test
	public void shouldUpdateAnInsuranceBrokerCompanyToRedis() {
		InsuranceBrokerCompany insuranceBrokerCompany = createNewInsuranceBrokerCompany();
		insuranceBrokerCompany.setId(id);
		Mockito.when(insuranceBrokerCompanyRepository.save(insuranceBrokerCompany)).thenReturn(insuranceBrokerCompany);
		insuranceBrokerCompanyService.save(insuranceBrokerCompany);
		InsuranceBrokerCompany insuranceBrokerCompanySaved = insuranceBrokerCompanyService
				.findById(insuranceBrokerCompany.getId());
		String newCode = "QBE-A";
		insuranceBrokerCompanySaved.setCode(newCode);
		Mockito.when(insuranceBrokerCompanyRepository.save(insuranceBrokerCompanySaved))
				.thenReturn(insuranceBrokerCompanySaved);
		insuranceBrokerCompanyService.save(insuranceBrokerCompanySaved);
		InsuranceBrokerCompany insuranceBrokerCompanyMofified = insuranceBrokerCompanyService
				.findById(insuranceBrokerCompany.getId());
		Assert.assertEquals(newCode, insuranceBrokerCompanyMofified.getCode());
	}

	@Test
	public void shouldDeleteAnInsuranceBrokerCompanyFromRedis() {
		InsuranceBrokerCompany insuranceBrokerCompany = createNewInsuranceBrokerCompany();
		insuranceBrokerCompany.setId(id);
		Mockito.when(insuranceBrokerCompanyRepository.save(insuranceBrokerCompany)).thenReturn(insuranceBrokerCompany);
		insuranceBrokerCompanyService.save(insuranceBrokerCompany);
		Mockito.doNothing().when(insuranceBrokerCompanyRepository).delete(id);
		Assert.assertNotNull(insuranceBrokerCompanyService.deleteById(insuranceBrokerCompany.getId()));
	}

	@Test
	public void souldFindAllFromRedis() {
		InsuranceBrokerCompany insuranceBrokerCompany = createNewInsuranceBrokerCompany();
		insuranceBrokerCompany.setId(55L);
		Mockito.when(insuranceBrokerCompanyRepository.save(insuranceBrokerCompany)).thenReturn(insuranceBrokerCompany);
		insuranceBrokerCompany = insuranceBrokerCompanyService.save(insuranceBrokerCompany);

		InsuranceBrokerCompany insuranceBrokerCompany2 = createNewInsuranceBrokerCompany();
		insuranceBrokerCompany2.setId(56L);
		Mockito.when(insuranceBrokerCompanyRepository.save(insuranceBrokerCompany2))
				.thenReturn(insuranceBrokerCompany2);
		insuranceBrokerCompany2 = insuranceBrokerCompanyService.save(insuranceBrokerCompany2);

		InsuranceBrokerCompany insuranceBrokerCompany3 = createNewInsuranceBrokerCompany();
		insuranceBrokerCompany3.setId(57L);
		Mockito.when(insuranceBrokerCompanyRepository.save(insuranceBrokerCompany3))
				.thenReturn(insuranceBrokerCompany3);
		insuranceBrokerCompany3 = insuranceBrokerCompanyService.save(insuranceBrokerCompany3);

		List<InsuranceBrokerCompany> list = insuranceBrokerCompanyService.findAll();
		assertNotNull(list);
		assertEquals(3, list.size());

		// Clean Up redis
		insuranceBrokerCompanyService.deleteById(55L);
		insuranceBrokerCompanyService.deleteById(56L);
		insuranceBrokerCompanyService.deleteById(57L);
	}

	@Test
	public void shouldDisabledAnInsuranceBrokerCompanyFromRedis() {
		InsuranceBrokerCompany insuranceBrokerCompany = createNewInsuranceBrokerCompany();
		insuranceBrokerCompany.setId(id);
		Mockito.when(insuranceBrokerCompanyRepository.save(insuranceBrokerCompany)).thenReturn(insuranceBrokerCompany);
		
		InsuranceBrokerCompany insuranceBrokerCompanySaved = insuranceBrokerCompanyService.save(insuranceBrokerCompany);
		insuranceBrokerCompanySaved.setStatus(EntityStatusEnum.DISABLE.getValue());//debemos cambiar el estado para emular mock
		
		Mockito.when(insuranceBrokerCompanyRepository.findOne(id)).thenReturn(insuranceBrokerCompany);
		Mockito.when(insuranceBrokerCompanyRepository.save(insuranceBrokerCompanySaved)).thenReturn(insuranceBrokerCompanySaved);
		Long disableVisitId = insuranceBrokerCompanyService.disableById(id);
		Assert.assertNotNull(disableVisitId);
		InsuranceBrokerCompany disableInsuranceBrokerCompany = insuranceBrokerCompanyService.findById(disableVisitId);
		Assert.assertEquals(EntityStatusEnum.DISABLE.getValue(), disableInsuranceBrokerCompany.getStatus().intValue());
	}

}
