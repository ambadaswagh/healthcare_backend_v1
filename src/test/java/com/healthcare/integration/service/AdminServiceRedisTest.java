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

import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.Role;
import com.healthcare.model.enums.GenderEnum;
import com.healthcare.repository.AdminRepository;
import com.healthcare.service.AdminService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AdminServiceRedisTest {
	@Autowired
	private AdminService adminService;

	@MockBean
	private AdminRepository adminRepository;

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
	
	private Long id = 7L;
	private Admin admin;
	private Role role;
	
	@Autowired
	private TestEntityServiceFactory testEntitySericeFactory;
	
	
	@Before
	public void setup() {
		testEntitySericeFactory.saveData = true;
		role = testEntitySericeFactory.createRole(1L);
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
		admin.setId(id);
		Mockito.when(adminRepository.save(admin)).thenReturn(admin);
		adminService.save(admin);
		Admin savedAdmin = adminService.findById(admin.getId());
		Assert.assertNotNull(savedAdmin);
	}

	@Test
	public void testUpdateAdmin() {
		String newPhone = "5967897788";
		String newEmail = "firstname2@yahoo.com";

		admin = createNewAdmin();
		admin.setId(id);
		Mockito.when(adminRepository.save(admin)).thenReturn(admin);
		adminService.save(admin);
		Admin adminSaved = adminService.findById(admin.getId());
		adminSaved.setPhone(newPhone);
		adminSaved.setEmail(newEmail);
		Mockito.when(adminRepository.save(adminSaved)).thenReturn(adminSaved);
		adminService.save(adminSaved);
		Admin adminMofified = adminService.findById(admin.getId());
		Assert.assertEquals(adminMofified.getPhone(), newPhone);
		Assert.assertEquals(adminMofified.getEmail(), newEmail);
	}

	@Test
	public void testDeleteAdmin() {
		admin = createNewAdmin();
		admin.setId(id);
		Mockito.when(adminRepository.save(admin)).thenReturn(admin);
		adminService.save(admin);
		Mockito.doNothing().when(adminRepository).delete(admin.getId());
		Assert.assertNotNull(adminService.deleteById(admin.getId()));
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

}
