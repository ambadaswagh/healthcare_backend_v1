package com.healthcare.integration.service;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

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

import com.healthcare.dto.UserDto;
import com.healthcare.model.entity.Agency;
import com.healthcare.model.entity.AgencyType;
import com.healthcare.model.entity.Company;
import com.healthcare.model.entity.User;
import com.healthcare.model.entity.Visit;
import com.healthcare.model.enums.*;
import com.healthcare.repository.AgencyRepository;
import com.healthcare.repository.UserRepository;
import com.healthcare.repository.VisitRepository;
import com.healthcare.service.AgencyService;
import com.healthcare.service.AgencyTypeService;
import com.healthcare.service.CompanyService;
import com.healthcare.service.UserService;
import com.healthcare.service.VisitService;
import com.healthcare.util.DateUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AgencyServiceRedisTest extends TestEntityFactory{
	@Autowired
	private AgencyService agencyService;
	
	@MockBean
	private AgencyRepository agencyRepository;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private AgencyTypeService agencyTypeService;
	
	@MockBean
	private UserRepository userRepository;
	
	@MockBean 
	private VisitRepository visitRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private VisitService visitService;

	private Company company;
	private AgencyType agencyType;
	private Agency agency =null;
	private Long id = 7L;

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
		agency.setId(id);
		Mockito.when(agencyRepository.save(agency)).thenReturn(agency);
		agency = agencyService.save(agency);
		Agency savedAgency = agencyService.findById(agency.getId());
		Assert.assertNotNull(savedAgency);
	}

	@Test
	public void testUpdateAgency() {
		String newAddressOne = "25, Green St";
		agency = createNewAgency(company,agencyType);
		agency.setId(id);
		Mockito.when(agencyRepository.save(agency)).thenReturn(agency);
		agency = agencyService.save(agency);
		Agency savedAgency = agencyService.findById(agency.getId());
		savedAgency.setAddressOne(newAddressOne);
		Mockito.when(agencyRepository.save(savedAgency)).thenReturn(savedAgency);
		agencyService.save(savedAgency);
		Agency modifiedAgency = agencyService.findById(agency.getId());
		Assert.assertEquals(modifiedAgency.getAddressOne(), newAddressOne);
	}

	@Test
	public void testDeleteAgency() {
		Agency agency = createNewAgency(company,agencyType);
		agency.setId(id);
		Mockito.when(agencyRepository.save(agency)).thenReturn(agency);
		agency = agencyService.save(agency);
		Mockito.doNothing().when(agencyRepository).delete(agency.getId());
		Assert.assertNotNull(agencyService.deleteById(agency.getId()));
	}

	@Test
	public void testDisableAgency() {
		Agency agency = createNewAgency(company,agencyType);
		agency.setId(id);
		Mockito.when(agencyRepository.save(agency)).thenReturn(agency);
		agency = agencyService.save(agency);
		Agency savedAgency = agencyService.findById(agency.getId());
		savedAgency.setStatus(0);
		Mockito.when(agencyRepository.save(savedAgency)).thenReturn(savedAgency);
		agencyService.disableById(savedAgency.getId());
		Agency disableAgency = agencyService.findById(agency.getId());
		Assert.assertEquals(0, disableAgency.getStatus());
	}

	@Test
	public void testGenerateUserStats(){
		
		//Given
		agency = TestEntityFactory.createNewAgency(company, agencyType);
		agency.setId(id);
		given(agencyRepository.save(any(Agency.class))).willReturn(agency);
		agency = agencyService.save(agency);
		
		User user1 = createNewUser(agency);// should be not checkIn 
		user1.setId(1L);
		User user2 = createNewUser(agency);// checkin
		user2.setId(2L);
		User user3 = createNewUser(agency);// checkedIn
		user3.setId(3L);
		User user4 = createNewUser(agency);// checkIn but not Active
		user4.setId(4L);
		user4.setStatus(StatusEnum.REGISTERED);
		
		given(userRepository.save(any(User.class))).willReturn(user1);
		userService.save(user1);
		given(userRepository.save(any(User.class))).willReturn(user2);
		userService.save(user2);
		given(userRepository.save(any(User.class))).willReturn(user3);
		userService.save(user3);
		given(userRepository.save(any(User.class))).willReturn(user4);
		userService.save(user4);
		
		
		// set visits to user 1
		Visit visit1 = TestEntityFactory.createNewVisit(user1, agency, null);
		visit1.setId(1L);
		visit1.setCheckInTime(new Timestamp(DateUtils.stringToDate("yyyy-MM-dd", "2016-07-14").getTime()));
		visit1.setCheckOutTime(new Timestamp(DateUtils.stringToDate("yyyy-MM-dd", "2016-07-14").getTime()));
		given(visitRepository.save(any(Visit.class))).willReturn(visit1);
		visitService.save(visit1);
		
		//set visits to user 2
		Visit visit2 = TestEntityFactory.createNewVisit(user2, agency, null);
		visit2.setId(2L);
		visit2.setCheckOutTime(new Timestamp(DateUtils.stringToDate("yyyy-MM-dd hh:mm:ss", "2020-07-14 18:00:00").getTime()));
		given(visitRepository.save(any(Visit.class))).willReturn(visit2);
		visitService.save(visit2);
		
		
		//set visits to user 3
		Visit visit3 = TestEntityFactory.createNewVisit(user3, agency, null);
		visit3.setId(3L);
		visit3.setCheckOutTime(new Timestamp(DateUtils.stringToDate("yyyy-MM-dd hh:mm:ss", "2020-07-14 18:00:00").getTime()));
		given(visitRepository.save(any(Visit.class))).willReturn(visit3);
		visitService.save(visit3);
		
		//set visits to user 4
		Visit visit4 = TestEntityFactory.createNewVisit(user4, agency, null);
		visit4.setId(4L);
		visit4.setCheckOutTime(new Timestamp(DateUtils.stringToDate("yyyy-MM-dd hh:mm:ss", "2020-07-14 18:00:00").getTime()));
		given(visitRepository.save(any(Visit.class))).willReturn(visit4);
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

}
