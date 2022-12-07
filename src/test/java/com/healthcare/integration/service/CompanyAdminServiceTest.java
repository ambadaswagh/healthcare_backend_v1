package com.healthcare.integration.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.Role;
import com.healthcare.model.enums.GenderEnum;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.AdminService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CompanyAdminServiceTest {
	@Autowired
	@Qualifier("companyAdminServiceImpl")
	private AdminService adminService;

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
	long status = 1;

	
	private Admin admin;
	private Role role;
	
	@Autowired
	private TestEntityServiceFactory testEntitySericeFactory;
	
	
	@Before
	public void setup() {
		testEntitySericeFactory.saveData = true;
		role  = testEntitySericeFactory.createRole(3L);
		admin = null;
	}

	
	@After
	public void rollback() {
		if(admin!=null){
			adminService.deleteById(admin.getId());
		}
		testEntitySericeFactory.redisCleanUp();
	}

	@Test
	public void testSaveAdmin() {
		admin = createNewAdmin();
		adminService.save(admin);
		Assert.assertNotNull(admin.getId());
	}

	@Test
	public void testGetAdmin() {
		admin = createNewAdmin();
		adminService.save(admin);
		Assert.assertNotNull(adminService.findById(admin.getId()));
	}

	@Test
	public void testUpdateAdmin() {
		String newPhone = "5967897788";
		String newEmail = "firstname2@yahoo.com";

		admin = createNewAdmin();
		adminService.save(admin);
		Assert.assertEquals(admin.getPhone(), phone);
		Assert.assertEquals(admin.getEmail(), email);
		Admin adminSaved = adminService.findById(admin.getId());
		adminSaved.setPhone(newPhone);
		adminSaved.setEmail(newEmail);
		adminService.save(adminSaved);
		Admin adminMofified = adminService.findById(admin.getId());
		Assert.assertEquals(adminMofified.getPhone(), newPhone);
		Assert.assertEquals(adminMofified.getEmail(), newEmail);
	}

	@Test
	public void testDeleteAdmin() {
		Admin admin = createNewAdmin();
		adminService.save(admin);
		Assert.assertNotNull(admin.getId());
		adminService.deleteById(admin.getId());
		Assert.assertNull(adminService.findById(admin.getId()));
	}

	@Test
	public void testDisableAdmin() {
		Admin admin = createNewAdmin();
		adminService.save(admin);
		Assert.assertNotNull(admin.getId());
		adminService.disableById(admin.getId());
		Admin disableAdmin = adminService.findById(admin.getId());
		Assert.assertNotNull(disableAdmin.getId());
		Assert.assertEquals(0, disableAdmin.getStatus());
	}

	private Admin createNewAdmin() {
		Admin admin = new Admin();
		admin.setUsername(username);
		admin.setPassword(password);
		admin.setFirstName(firstName);
		admin.setMiddleName(middleName);
		admin.setLastName(lastName);
		admin.setGender(GenderEnum.MAN.name());
		admin.setPhone(phone);
		admin.setEmail(email);
		admin.setDeviceAddress(deviceAddress);
		admin.setIp(ip);
		admin.setProfilePhoto(null);
		admin.setRememberToken(rememberToken);
		admin.setSecondaryPhone(secondaryPhone);
		admin.setStatus(status);
		admin.setRole(role);
		return admin;
	}
	

	@Test
	public void testFindAllPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			username = "username"+i;
			Admin admin = adminService.save(createNewAdmin());
			listIds.add(admin.getId());
		}

		// PAGE 0
		// given
		Pageable pageable = new CustomPageRequest();
		// when
		Page<Admin> result = adminService.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == defaultMaxSize);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 0);

		// PAGE 2
		pageable = new PageRequest(1, 5);
		// when
		result = adminService.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == 5);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 1);

		// delete created data
		for (Long id : listIds) {
			adminService.deleteById(id);
		}
	}

	@Test
	public void testSearchWithPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			username = "username"+i;
			Admin admin = adminService.save(createNewAdmin());
			listIds.add(admin.getId());
		}
		// SEARCH 1 : FIND BY ID
		// given
		Pageable pageable = new PageRequest(0, 25);
		Admin adminPostin = new Admin();
		adminPostin.setId(listIds.get(0));
		adminPostin.setStatus(1);

		// when
		Page<Admin> result = adminService.findAll(adminPostin, pageable);
		// then
		assertTrue(result.getContent().size() == 1);
		assertTrue(result.getTotalElements() >= 1);
		assertTrue(result.getTotalPages() >= 1);
		assertTrue(result.getNumber() == 0);

		// SEARCH 2
		// given
		pageable = new PageRequest(3, 5);
		adminPostin = new Admin();
		adminPostin.setStatus(1);

		// when
		result = adminService.findAll(adminPostin, pageable);
		// then
		assertTrue(result.getContent().size() == 5);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 3);

		// Delete created data
		for (Long id : listIds) {
			adminService.deleteById(id);
		}
	}
}
