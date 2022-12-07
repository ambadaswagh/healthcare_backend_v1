package com.healthcare.integration.service;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.transaction.Transactional;

import com.healthcare.model.entity.*;
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

import com.healthcare.model.enums.StateEnum;
import com.healthcare.repository.RoleRepository;
import com.healthcare.service.AgencyService;
import com.healthcare.service.AgencyTypeService;
import com.healthcare.service.CompanyService;
import com.healthcare.service.RoleService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class RoleServiceRedisTest {
	@Autowired
	private RoleService roleService;

	@MockBean
	private RoleRepository roleRepository;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private AgencyService agencyService;

	@Autowired
	private AgencyTypeService agencyTypeService;

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
	String levelName = "Level Name";
	long level = 1;
	long status = 1;

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

	Agency agency;

	private Long id = 7L;
	private Company company;
	private AgencyType agencyType;

	@Before
	public void setup() {
		company = createNewCompany();
    	agencyType = createNewAgencyType();
    	agency = createNewAgency(company,agencyType);
	}

	@After
	public void rollback() {
		roleService.deleteById(id);
        agencyService.deleteById(agency.getId());
        agencyTypeService.deleteById(agencyType.getId());
        companyService.deleteById(company.getId());
	}


	@Test
	public void testSaveRole() {
		Role role = createNewRole(level);
		role.setId(id);
		Mockito.when(roleRepository.save(role)).thenReturn(role);
		role = roleService.save(role);
		Role savedRole = roleService.findById(role.getId());
		Assert.assertNotNull(savedRole);
	}

	@Test
	public void testUpdateRole() {
		String newLevelName = "new level name";
		Role role = createNewRole(level);
		role.setId(id);
		Mockito.when(roleRepository.save(role)).thenReturn(role);
		role = roleService.save(role);
		Role roleSaved = roleService.findById(role.getId());
		roleSaved.setLevelName(newLevelName);
		Mockito.when(roleRepository.save(roleSaved)).thenReturn(roleSaved);
		roleService.save(roleSaved);
		Role roleMofified = roleService.findById(role.getId());
		Assert.assertEquals(roleMofified.getLevelName(), newLevelName);
	}

	@Test
	public void testDeleteRole() {
		Role role = createNewRole(level);
		role.setId(id);
		Mockito.when(roleRepository.save(role)).thenReturn(role);
		role = roleService.save(role);
		Mockito.doNothing().when(roleRepository).delete(role.getId());
		Assert.assertNotNull(roleService.deleteById(role.getId()));
	}

	@Test
	public void testDisableRole() {
		Role role = createNewRole(level);
		role.setId(id);
		Mockito.when(roleRepository.save(role)).thenReturn(role);
		role = roleService.save(role);
		Role savedRole = roleService.findById(role.getId());
		savedRole.setStatus(0);
		Mockito.when(roleRepository.save(savedRole)).thenReturn(savedRole);
		Long disableRoleId = roleService.disableById(savedRole.getId());
		Assert.assertNotNull(disableRoleId);
		Role disableRole = roleService.findById(disableRoleId);
		Assert.assertEquals(0, disableRole.getStatus());
	}

	private Role createNewRole(long level) {
		Role role = new Role();
		role.setLevel(level);
		role.setLevelName(levelName);
		role.setStatus(status);
		return role;
	}

	private Agency createNewAgency(Company company,AgencyType agencyType) {
		Agency agency = new Agency();
		agency.setAddressOne(addressOne);
		agency.setAddressTwo(addressTwo);
		agency.setAgencyType(agencyType);
		agency.setCity(city);
		agency.setCompany(company);
		agency.setContactPerson(contactPerson);
		agency.setEmail(email);
		agency.setFax(fax);
		agency.setHoliday(holiday);
		agency.setLicenseNo(licenseNo);
		agency.setName("Agency Name");
		agency.setPhone(phone);
		agency.setState(state);
		agency.setTimezone(timezone);
		agency.setTrackingMode(trackingMode);
		agency.setZipcode(zipcode);
		return agencyService.save(agency);
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
		return companyService.save(company);
	}

	private AgencyType createNewAgencyType() {
		AgencyType agencyType = new AgencyType();
		agencyType.setName("Agency Type Name");
		agencyType.setStatus(1);
		return agencyTypeService.save(agencyType);
	}
}
