package com.healthcare.integration.service;

import static org.junit.Assert.assertTrue;

import java.sql.Time;
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

import com.healthcare.model.entity.Company;
import com.healthcare.model.enums.StateEnum;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.CompanyService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CompanyServiceTest {
	@Autowired
	private CompanyService companyService;

	@Value("${spring.data.rest.default-page-size}")
	private int defaultMaxSize;

	String username = "username";
	String password = "password";
	String firstName = "John";
	String middleName = "B";
	String lastName = "Watson";
	String phone = "1234560000";
	String email = "firstname@yahoo.com";
	String ip = "127.0.0.1";
	String secondaryPhone = "1234560001";
	String profilePhoto = "XXXXXXXXXX";
	String deviceAddress = "City ABC";
	String rememberToken = "00000";

	String licenseNo = "12D31";
	int trackingMode = 1;
	String contactPerson = "Joe";
	String addressOne = "20, Green St";
	String addressTwo = "A st";
	String city = "Orlando";
	String state = StateEnum.FLORIDA.name();
	String zipcode = "2122";
	String timezone = "UTC";
	String holiday = "12";
	String fax = "12212444";

	String federalTax = "federalTax";
	Calendar federalTaxStart = Calendar.getInstance();
	Calendar federalTaxExpire = Calendar.getInstance();
	String stateTax = "stateTax";
	Calendar stateTaxStart = Calendar.getInstance();
	Calendar stateTaxExpire = Calendar.getInstance();
	Calendar worktimeStart = Calendar.getInstance();
	Calendar worktimeEnd = Calendar.getInstance();

	private Company company;
	@Before
	public void setup() {
		company=null;
	}
	
	@After
	public void rollback() {
		if(company!=null)
			companyService.deleteById(company.getId());
	}

	@Test
	public void testSaveCompany() {
		company = createNewCompany();
		companyService.save(company);
		Assert.assertNotNull(company.getId());
	}

	@Test
	public void testGetCompany() {
		company = createNewCompany();
		companyService.save(company);
		Assert.assertNotNull(companyService.findById(company.getId()));
	}

	@Test
	public void testUpdateCompany() {
		String newAddressOne = "25, Green St";

		company = createNewCompany();
		companyService.save(company);
		Assert.assertEquals(company.getAddressOne(), addressOne);
		Company savedCompany = companyService.findById(company.getId());
		savedCompany.setAddressOne(newAddressOne);
		companyService.save(savedCompany);
		Company modifiedCompany = companyService.findById(company.getId());
		Assert.assertEquals(modifiedCompany.getAddressOne(), newAddressOne);
	}

	@Test
	public void testDeleteCompany() {
		Company company = createNewCompany();
		companyService.save(company);
		Assert.assertNotNull(company.getId());
		companyService.deleteById(company.getId());
		Assert.assertNull(companyService.findById(company.getId()));
	}

	@Test
	public void testDisableCompany() {
		Company company = createNewCompany();
		companyService.save(company);
		Assert.assertNotNull(company.getId());
		companyService.disableById(company.getId());
		Company disableCompany = companyService.findById(company.getId());
		Assert.assertNotNull(disableCompany.getId());
		Assert.assertEquals(0, disableCompany.getStatus());
	}

	private Company createNewCompany() {
		Company company = new Company();
		company.setAddressOne(addressOne);
		company.setAddressTwo(addressTwo);
		company.setCity(city);
		company.setEmail(email);
		company.setFax(fax);
		company.setFederalTax(federalTax);
		company.setFederalTaxExpire(new Timestamp(federalTaxExpire.getTimeInMillis()));
		company.setFederalTaxStart(new Timestamp(federalTaxStart.getTimeInMillis()));
		company.setFederalTaxStatus(1);
		company.setLicenseNo(licenseNo);
		company.setName("Company Name");
		company.setPhone(phone);
		company.setState(state);
		company.setStateTax(stateTax);
		company.setStateTaxExpire(new Timestamp(stateTaxExpire.getTimeInMillis()));
		company.setStateTaxStart(new Timestamp(stateTaxStart.getTimeInMillis()));
		company.setStateTaxStatus(1);
//		company.setType("type 1");
		company.setStatus(1);
		company.setWorktimeEnd(new Time(worktimeEnd.getTimeInMillis()));
		company.setWorktimeStart(new Time(worktimeStart.getTimeInMillis()));
		company.setZipcode(zipcode);
		return company;
	}
	
    
	@Test
	public void testFindAllPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			Company obj = companyService.save(createNewCompany());
			listIds.add(obj.getId());
		}

		// PAGE 0
		// given
		Pageable pageable = new CustomPageRequest();
		// when
		Page<Company> result = companyService.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == defaultMaxSize);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 0);

		// PAGE 2
		pageable = new PageRequest(1, 5);
		// when
		result = companyService.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == 5);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 1);

		// delete created data
		for (Long id : listIds) {
			companyService.deleteById(id);
		}
	}

	@Test
	public void testSearchWithPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			Company obj = companyService.save(createNewCompany());
			listIds.add(obj.getId());
		}
		// SEARCH 1 : FIND BY ID
		// given
		Pageable pageable = new PageRequest(0, 25);
		Company obj = new Company();
		obj.setId(listIds.get(0));
		obj.setStatus(1);
		obj.setFederalTaxStatus(1);
		obj.setStateTaxStatus(1);

		// when
		Page<Company> result = companyService.findAll(obj, pageable);
		// then
		assertTrue(result.getContent().size() == 1);
		assertTrue(result.getTotalElements() >= 1);
		assertTrue(result.getTotalPages() >= 1);
		assertTrue(result.getNumber() == 0);

		// SEARCH 2 : TODO : type of attributes of company must be not primitive
		// given
//		pageable = new PageRequest(3, 5);
//		obj = new Company();
//		obj.setStatus(1);
//		obj.setFederalTaxStatus(1);
//		obj.setStateTaxStatus(1);
//
//		// when
//		result = companyService.findAll(obj, pageable);
//		// then
//		assertTrue(result.getContent().size() == 5);
//		assertTrue(result.getTotalElements() >= 25);
//		assertTrue(result.getNumber() == 3);
//
//		// Delete created data
//		for (Long id : listIds) {
//			companyService.deleteById(id);
//		}
	}
}
