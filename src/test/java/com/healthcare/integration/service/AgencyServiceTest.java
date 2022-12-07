package com.healthcare.integration.service;

import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
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

import com.healthcare.dto.UserDto;
import com.healthcare.model.entity.Agency;
import com.healthcare.model.entity.AgencyType;
import com.healthcare.model.entity.Company;
import com.healthcare.model.entity.User;
import com.healthcare.model.entity.Visit;
import com.healthcare.model.enums.StatusEnum;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.AgencyService;
import com.healthcare.service.AgencyTypeService;
import com.healthcare.service.CompanyService;
import com.healthcare.service.UserService;
import com.healthcare.service.VisitService;
import com.healthcare.util.DateUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AgencyServiceTest extends TestEntityFactory {
	@Autowired
	private AgencyService agencyService;

	@Autowired
	public CompanyService companyService;
	
	@Autowired
	private VisitService visitService;
	
	@Autowired
	private UserService userService;

	@Autowired
	public AgencyTypeService agencyTypeService;

	@Value("${spring.data.rest.default-page-size}")
	private int defaultMaxSize;

	private Company company;
	private AgencyType agencyType;
	private Agency agency =null;
	
	String username = "username";
	String password = "password";
	String firstName = "John";
	String middleName = "B";
	String lastName = "Watson";
	String ip = "127.0.0.1";
	String secondaryPhone = "1234560001";
	String profilePhoto = "XXXXXXXXXX";
	String deviceAddress = "City ABC";
	String rememberToken = "00000";
	String levelName = "Level Name";
	long level = 1;
	long status = 1;

	@Before
	public void setup() {
		company = companyService.save(TestEntityFactory.createNewCompany());
		agencyType = agencyTypeService.save(TestEntityFactory.createNewAgencyType());
		agency =null;
	}

	@After
	public void rollback() {
		if (agency != null)
			agencyService.deleteById(agency.getId());
		
		agencyTypeService.deleteById(agencyType.getId());
		companyService.deleteById(company.getId());
	}

	@Test
	public void testSaveAgency() {
		agency = createNewAgency(company,agencyType);
		agency = agencyService.save(agency);
		Assert.assertNotNull(agency.getId());
	}

	@Test
	public void testGetAgency() {
		agency = createNewAgency( company,agencyType);
		agency = agencyService.save(agency);
		Assert.assertNotNull(agencyService.findById(agency.getId()));
	}

	@Test
	public void testUpdateAgency() {
		String newAddressOne = "25, Green St";
		agency = createNewAgency(company,agencyType );
		agency = agencyService.save(agency);
		Assert.assertEquals(agency.getAddressOne(), addressOne);
		Agency savedAgency = agencyService.findById(agency.getId());
		savedAgency.setAddressOne(newAddressOne);
		agencyService.save(savedAgency);
		Agency modifiedAgency = agencyService.findById(agency.getId());
		Assert.assertEquals(modifiedAgency.getAddressOne(), newAddressOne);
	}

	@Test
	public void testDeleteAgency() {
		Agency agency = createNewAgency( company,agencyType);
		agency = agencyService.save(agency);
		Assert.assertNotNull(agency.getId());
		agencyService.deleteById(agency.getId());
		Assert.assertNull(agencyService.findById(agency.getId()));
	}

	@Test
	public void testDisableAgency() {
		Agency agency = createNewAgency( company,agencyType);
		agency = agencyService.save(agency);
		Assert.assertNotNull(agency.getId());
		agencyService.disableById(agency.getId());
		Agency disableAgency = agencyService.findById(agency.getId());
		Assert.assertNotNull(disableAgency.getId());
		Assert.assertEquals(0, disableAgency.getStatus());
	}
	
	@Test
	public void testGenerateUserStats(){
		
		//Given
		agency = TestEntityFactory.createNewAgency(company, agencyType);
		agency = agencyService.save(agency);
		
		User user1 = createNewUser(agency);// should be not checkIn 
		User user2 = createNewUser(agency);// checkin
		User user3 = createNewUser(agency);// checkedIn
		User user4 = createNewUser(agency);// checkIn but not Active
		user4.setStatus(StatusEnum.REGISTERED);
		
		userService.save(user1);
		userService.save(user2);
		userService.save(user3);
		userService.save(user4);
		
		
		// set visits to user 1
		Visit visit1 = TestEntityFactory.createNewVisit(user1, agency, null);
		visit1.setCheckInTime(new Timestamp(DateUtils.stringToDate("yyyy-MM-dd", "2016-07-14").getTime()));
		visit1.setCheckOutTime(new Timestamp(DateUtils.stringToDate("yyyy-MM-dd", "2016-07-14").getTime()));
		visitService.save(visit1);
		
		//set visits to user 2
		Visit visit2 = TestEntityFactory.createNewVisit(user2, agency, null);
		visit2.setCheckOutTime(new Timestamp(DateUtils.stringToDate("yyyy-MM-dd hh:mm:ss", "2020-07-14 18:00:00").getTime()));
		visitService.save(visit2);
		
		
		//set visits to user 3
		Visit visit3 = TestEntityFactory.createNewVisit(user3, agency, null);
		visit3.setCheckOutTime(new Timestamp(DateUtils.stringToDate("yyyy-MM-dd hh:mm:ss", "2020-07-14 18:00:00").getTime()));
		visitService.save(visit3);
		
		//set visits to user 4
		Visit visit4 = TestEntityFactory.createNewVisit(user4, agency, null);
		visit4.setCheckOutTime(new Timestamp(DateUtils.stringToDate("yyyy-MM-dd hh:mm:ss", "2020-07-14 18:00:00").getTime()));
		visitService.save(visit4);
		
		//when
		UserDto userDto = agencyService.generateUserStats(agency.getId());
		
		//then
		Assert.assertTrue(userDto.getTotalActiveSeniorsAmongCheckedIn()>0);
		Assert.assertTrue(userDto.getTotalCheckedInSeniors()>0);
		Assert.assertTrue(userDto.getTotalRegisteredSeniors()>0);
		
		visitService.deleteById(visit1.getId());
		visitService.deleteById(visit2.getId());
		visitService.deleteById(visit3.getId());
		visitService.deleteById(visit4.getId());
		
		userService.deleteById(user1.getId());
		userService.deleteById(user2.getId());
		userService.deleteById(user3.getId());
		userService.deleteById(user4.getId());

	}
	

	@Test
	public void testFindAllPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			Agency agency = agencyService.save(createNewAgency(company,agencyType));
			listIds.add(agency.getId());
		}

		// PAGE 0
		// given
		Pageable pageable = new CustomPageRequest();
		// when
		Page<Agency> result = agencyService.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == defaultMaxSize);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 0);

		// PAGE 2
		pageable = new PageRequest(1, 5);
		// when
		result = agencyService.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == 5);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 1);

		// delete created data
		for (Long id : listIds) {
			agencyService.deleteById(id);
		}
	}

	@Test
	public void testSearchWithPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			Agency agency = agencyService.save(createNewAgency(company,agencyType));
			listIds.add(agency.getId());
		}
		// SEARCH 1 : FIND BY ID
		// given
		Pageable pageable = new PageRequest(0, 25);
		Agency agency = new Agency();
		agency.setId(listIds.get(0));
		agency.setStatus(1);
		agency.setTrackingMode(1);

		// when
		Page<Agency> result = agencyService.findAll(agency, pageable);
		// then
		assertTrue(result.getContent().size() == 1);
		assertTrue(result.getTotalElements() >= 1);
		assertTrue(result.getTotalPages() >= 1);
		assertTrue(result.getNumber() == 0);

		// SEARCH 2
		// given
		pageable = new PageRequest(3, 5);
		agency = new Agency();
		agency.setStatus(1);
		agency.setTrackingMode(1);

		// when
		result = agencyService.findAll(agency, pageable);
		// then
		assertTrue(result.getContent().size() == 5);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 3);

		// Delete created data
		for (Long id : listIds) {
			agencyService.deleteById(id);
		}
	}
}
