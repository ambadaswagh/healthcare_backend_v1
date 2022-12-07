package com.healthcare.integration.service;

import java.sql.Timestamp;

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

import com.healthcare.model.entity.Agency;
import com.healthcare.model.entity.CareGiver;
import com.healthcare.model.entity.Company;
import com.healthcare.repository.CareGiverRepository;
import com.healthcare.service.CareGiverService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CareGiverServiceRedisTest {
    @MockBean
    private CareGiverRepository careGiverRepository;

    @Autowired
    private CareGiverService careGiverService;

    
    
    
    private Long id = 100L;
    private CareGiver careGiver;
    @Before
    public void setup() {
    	careGiver=null;
    }
    
    @After
    public void rollback(){
  		if(careGiver!=null)
  			careGiverService.deleteById(careGiver.getId());
    }
    
    @Test
    public void saveACareGiverToRedisAndRetrievedItFromRedis() {
    	careGiver = getCareGiver();
        careGiver.setId(id);
        Mockito.when(careGiverRepository.save(careGiver)).thenReturn(careGiver);
        careGiverService.save(careGiver);
        CareGiver careGiverSaved = careGiverService.findById(id);
        Assert.assertNotNull(careGiverSaved);
    }

   @Test
    public void updateACareGiverToRedis() {
	   	String firstName = "first name updated";
    	String lastName = "last name updated";
    	
        careGiver = getCareGiver();
        careGiver.setId(id);
        Mockito.when(careGiverRepository.save(careGiver)).thenReturn(careGiver);
        careGiverService.save(careGiver);
        
        CareGiver careGiverSaved = careGiverService.findById(careGiver.getId());
        careGiverSaved.setFirstName(firstName);
        careGiverSaved.setLastName(lastName);
        
        Mockito.when(careGiverRepository.save(careGiverSaved)).thenReturn(careGiverSaved);
        careGiverService.save(careGiverSaved);

        CareGiver careGiverModified = careGiverService.findById(careGiver.getId());
        Assert.assertEquals(careGiverModified.getFirstName(), firstName);
        Assert.assertEquals(careGiverModified.getLastName(), lastName);
    }

    @Test
    public void deleteACareGiverFromRedis() {
        CareGiver careGiver = getCareGiver();
        careGiver.setId(id);
        Mockito.when(careGiverRepository.save(careGiver)).thenReturn(careGiver);
        careGiverService.save(careGiver);
        Mockito.doNothing().when(careGiverRepository).delete(id);
        Assert.assertNotNull(careGiverService.deleteById(careGiver.getId()));
    }

    private CareGiver getCareGiver() {
		final CareGiver careGiver = new CareGiver();
        careGiver.setFirstName("First Name");
        careGiver.setLastName("Last Name");
        careGiver.setCareGiverType(1L);
        careGiver.setUsername("testUser");
        careGiver.setPassword("password");
        careGiver.setPhone("1234567890");
        careGiver.setCertificate("Medical Certificate");
        careGiver.setCertificateStart(Timestamp.valueOf("2007-09-23 10:10:10.0"));
        careGiver.setCertificateEnd(Timestamp.valueOf("2007-10-23 10:10:10.0"));
        careGiver.setAgency(getAgency());
        careGiver.setCompany(getCompany());
		return careGiver;
	}

	private Agency getAgency() {
		final Long agencyId = 100L;
        final Agency agency = new Agency();
        agency.setId(agencyId);
		return agency;
	}

	private Company getCompany() {
		final Long companyId = 100L;
        final Company company = new Company();
        company.setId(companyId);
		return company;
	}

}
