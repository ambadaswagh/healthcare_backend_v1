package com.healthcare.integration.service;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

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

import com.healthcare.model.entity.Company;
import com.healthcare.model.enums.StateEnum;
import com.healthcare.repository.CompanyRepository;
import com.healthcare.service.CompanyService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CompanyServiceRedisTest {
	@Autowired
	private CompanyService companyService;

	@MockBean
	private CompanyRepository companyRepository;

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

	@Before
	public void setup() {
	}


	private Long id = 7L;

	@After
	public void rollback() {
		companyService.deleteById(id);
	}

	@Test
	public void testSaveCompany() {
		Company company = createNewCompany();
		company.setId(id);
		Mockito.when(companyRepository.save(company)).thenReturn(company);
		companyService.save(company);
		Company savedCompany = companyService.findById(company.getId());
		Assert.assertNotNull(savedCompany);
	}

	@Test
	public void testUpdateCompany() {
		String newAddressOne = "25, Green St";

		Company menu = createNewCompany();
		menu.setId(id);
		Mockito.when(companyRepository.save(menu)).thenReturn(menu);
		companyService.save(menu);
		Company savedCompany = companyService.findById(menu.getId());
		savedCompany.setAddressOne(newAddressOne);
		Mockito.when(companyRepository.save(savedCompany)).thenReturn(savedCompany);
		companyService.save(savedCompany);
		Company modifiedCompany = companyService.findById(menu.getId());
		Assert.assertEquals(modifiedCompany.getAddressOne(), newAddressOne);
	}

	@Test
	public void testDeleteCompany() {
		Company company = createNewCompany();
		company.setId(id);
		Mockito.when(companyRepository.save(company)).thenReturn(company);
		companyService.save(company);
		Mockito.doNothing().when(companyRepository).delete(company.getId());
		Assert.assertNotNull(companyService.deleteById(company.getId()));
	}

	@Test
	public void testDisableCompany() {
		Company company = createNewCompany();
		company.setId(id);
		Mockito.when(companyRepository.save(company)).thenReturn(company);
		companyService.save(company);
		Company savedCompany = companyService.findById(company.getId());
		savedCompany.setStatus(0);
		Mockito.when(companyRepository.save(savedCompany)).thenReturn(savedCompany);
		companyService.disableById(company.getId());
		Company disableCompany = companyService.findById(savedCompany.getId());
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
		company.setStatus(1);
		company.setWorktimeEnd(new Time(worktimeEnd.getTimeInMillis()));
		company.setWorktimeStart(new Time(worktimeStart.getTimeInMillis()));
		company.setZipcode(zipcode);
		return company;
	}
}
