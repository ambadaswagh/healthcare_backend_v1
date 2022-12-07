package com.healthcare.integration.service;

import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

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

import com.healthcare.model.entity.Agency;
import com.healthcare.model.entity.AgencyType;
import com.healthcare.model.entity.CareGiver;
import com.healthcare.model.entity.Company;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.AgencyService;
import com.healthcare.service.AgencyTypeService;
import com.healthcare.service.CareGiverService;
import com.healthcare.service.CompanyService;
import com.healthcare.service.EmployeeService;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CareGiverServiceTest extends TestEntityFactory {

    @Autowired
    private CareGiverService careGiverService;

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private AgencyTypeService agencyTypeService;

	@Value("${spring.data.rest.default-page-size}")
	private int defaultMaxSize;

    private CareGiver careGiver;
    private Agency agency;
    private Company company;
    private AgencyType agencyType;
    
    
    @Before
    public void setup() {
    	company = companyService.save(TestEntityFactory.createNewCompany());
    	agencyType = agencyTypeService.save(TestEntityFactory.createNewAgencyType());
    	agency = agencyService.save(TestEntityFactory.createNewAgency(company, agencyType));
    	careGiver = null;
    }

	@After
	public void rollback() {
		if(careGiver!=null)
			careGiverService.deleteById(careGiver.getId());
		
        agencyService.deleteById(agency.getId());
        agencyTypeService.deleteById(agencyType.getId());
        companyService.deleteById(company.getId());
	}

    /**
     * CareGiver
     */
    Long careGiverType = 1L;
    String language = "pt-BR";
    String secondaryPhone = "99999999";
    String verificationCode = "123";
    String profilePhoto = "profile photo";
    String certificate = "certificate";
    Integer statusCareGiver = 1;
    Calendar vacationStart = Calendar.getInstance();
    Calendar vacationEnd = Calendar.getInstance();


    @Test
    public void shouldSaveACareGiver() {
        careGiver = createNewCareGiver(company,agency);
        careGiver = careGiverService.save(careGiver);
        Assert.assertNotNull(careGiver);
    }

    @Test
    public void shouldGetACareGiver() {
        careGiver = createNewCareGiver(company,agency);
        careGiver = careGiverService.save(careGiver);
        Assert.assertNotNull(careGiverService.findById(careGiver.getId()));
    }

    @Test
    public void shouldUpdateACareGiver() {
        String newLanguage = "en-US";
        String newSecondaryPhone = "888 88888";

        careGiver = createNewCareGiver(company,agency);
        careGiverService.save(careGiver);
        Assert.assertEquals(careGiver.getLanguage(), language);
        Assert.assertEquals(careGiver.getSecondaryPhone(), secondaryPhone);

        CareGiver careGiverSaved = careGiverService.save(careGiver);
        careGiverSaved.setLanguage(newLanguage);
        careGiverSaved.setSecondaryPhone(newSecondaryPhone);
        careGiverService.save(careGiverSaved);

        CareGiver careGiverMofified = careGiverService.findById(careGiverSaved.getId());
        Assert.assertEquals(careGiverMofified.getLanguage(), newLanguage);
        Assert.assertEquals(careGiverMofified.getSecondaryPhone(), newSecondaryPhone);
    }

    @Test
    public void shouldDeleteACareGiver() {
        CareGiver careGiver = createNewCareGiver(company,agency);
        careGiver = careGiverService.save(careGiver);
        Assert.assertNotNull(careGiver);
        careGiverService.deleteById(careGiver.getId());
        Assert.assertNull(careGiverService.findById(careGiver.getId()));
    }

  
    private CareGiver createNewCareGiver(Company company,Agency agency) {
        CareGiver caregiver = new CareGiver();
        caregiver.setCompany(company);
        caregiver.setAgency(agency);
        caregiver.setCareGiverType(careGiverType);
        caregiver.setUsername(username);
        caregiver.setPassword(password);
        caregiver.setFirstName(firstName);
        caregiver.setMiddleName(middleName);
        caregiver.setLastName(lastName);
        caregiver.setGender(gender);
        caregiver.setLanguage(language);
        caregiver.setSocialSecurityNumber(socialSecurityNumber);
        caregiver.setDateOfBirth(new Timestamp(dateOfBirth.getTimeInMillis()));
        caregiver.setEmail(email);
        caregiver.setPhone(phone);
        caregiver.setSecondaryPhone(secondaryPhone);
        caregiver.setVerificationCode(verificationCode);
        caregiver.setAddressType(addressType);
        caregiver.setAddressOne(addressOne);
        caregiver.setAddressTwo(addressTwo);
        caregiver.setCity(city);
        caregiver.setState(state);
        caregiver.setZipcode(zipcode);
        caregiver.setProfilePhoto(null);
        caregiver.setCertificate(certificate);
        caregiver.setCertificateStart(new Timestamp(certificateStart.getTimeInMillis()));
        caregiver.setCertificateEnd(new Timestamp(certificateEnd.getTimeInMillis()));
        caregiver.setStatus(statusCareGiver);
        caregiver.setVacationNote(vacationNote);
        caregiver.setVacationStart(new Timestamp(vacationStart.getTimeInMillis()));
        caregiver.setVacationEnd(new Timestamp(vacationEnd.getTimeInMillis()));

        return caregiver;
    }
    
	@Test
	public void testFindAllPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			CareGiver obj = careGiverService.save(createNewCareGiver(company, agency));
			listIds.add(obj.getId());
		}

		// PAGE 0
		// given
		Pageable pageable = new CustomPageRequest();
		// when
		Page<CareGiver> result = careGiverService.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == defaultMaxSize);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 0);

		// PAGE 2
		pageable = new PageRequest(1, 5);
		// when
		result = careGiverService.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == 5);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 1);

		// delete created data
		for (Long id : listIds) {
			careGiverService.deleteById(id);
		}
	}

	@Test
	public void testSearchWithPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			CareGiver obj = careGiverService.save(createNewCareGiver(company, agency));
			listIds.add(obj.getId());
		}
		// SEARCH 1 : FIND BY ID
		// given
		Pageable pageable = new PageRequest(0, 25);
		CareGiver obj = new CareGiver();
		obj.setId(listIds.get(0));
		obj.setStatus(1);

		// when
		Page<CareGiver> result = careGiverService.findAll(obj, pageable);
		// then
		assertTrue(result.getContent().size() == 1);
		assertTrue(result.getTotalElements() >= 1);
		assertTrue(result.getTotalPages() >= 1);
		assertTrue(result.getNumber() == 0);

		// SEARCH 2
		// given
		pageable = new PageRequest(3, 5);
		obj = new CareGiver();
		obj.setStatus(1);

		// when
		result = careGiverService.findAll(obj, pageable);
		// then
		assertTrue(result.getContent().size() == 5);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 3);

		// Delete created data
		for (Long id : listIds) {
			careGiverService.deleteById(id);
		}
	}

}
