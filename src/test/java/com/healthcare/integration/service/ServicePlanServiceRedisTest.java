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

import com.healthcare.EntityFactory;
import com.healthcare.model.entity.Agency;
import com.healthcare.model.entity.AgencyType;
import com.healthcare.model.entity.Company;
import com.healthcare.model.entity.Employee;
import com.healthcare.model.entity.ServicePlan;
import com.healthcare.model.entity.User;
import com.healthcare.repository.ServicePlanRepository;
import com.healthcare.service.AgencyService;
import com.healthcare.service.AgencyTypeService;
import com.healthcare.service.CompanyService;
import com.healthcare.service.EmployeeService;
import com.healthcare.service.ServicePlanService;
import com.healthcare.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ServicePlanServiceRedisTest extends EntityFactory {
	@Autowired
	private ServicePlanService servicePlanService;

	@MockBean
	private ServicePlanRepository servicePlanRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private AgencyService agencyService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private AgencyTypeService agencyTypeService;

	private User user;
	private Employee employee;
	private Company company;
	private AgencyType agencyType;
	private Agency agency;

	@Before
	public void setup() {
		init();
		user = createNewUser();
		userService.save(user);
		agencyType = createNewAgencyType();
		agencyTypeService.save(agencyType);
		company = createNewCompany();
		companyService.save(company);
		agency = createNewAgency(agencyType, company);
		agencyService.save(agency);
		employee = createNewEmployee(agency);
		employeeService.save(employee);
	}

	private Long id = 7L;
	@After
	public void rollback() {
		servicePlanService.deleteById(id);
		employeeService.deleteById(employee.getId());
		userService.deleteById(user.getId());
        agencyService.deleteById(agency.getId());
        agencyTypeService.deleteById(agencyType.getId());
        companyService.deleteById(company.getId());
	}

	@Test
	public void testSaveServicePlan() {
		ServicePlan servicePlan = createNewServicePlan(user);
		servicePlan.setId(id);
		Mockito.when(servicePlanRepository.save(servicePlan)).thenReturn(servicePlan);
		servicePlanService.save(servicePlan);
		ServicePlan savedServicePlan = servicePlanService.findById(servicePlan.getId());
		Assert.assertNotNull(savedServicePlan);
	}

	@Test
	public void testUpdateServicePlan() {
		String newDocUrl = "/doc/new/a";
		ServicePlan servicePlan = createNewServicePlan(user);
		servicePlan.setId(id);
		Mockito.when(servicePlanRepository.save(servicePlan)).thenReturn(servicePlan);
		servicePlanService.save(servicePlan);
		ServicePlan savedServicePlan = servicePlanService.findById(servicePlan.getId());
//		savedServicePlan.setDocUrl(newDocUrl);
		Mockito.when(servicePlanRepository.save(savedServicePlan)).thenReturn(savedServicePlan);
		servicePlanService.save(savedServicePlan);
		ServicePlan modifiedServicePlan = servicePlanService.findById(servicePlan.getId());
//		Assert.assertEquals(modifiedServicePlan.getDocUrl(), newDocUrl);
	}

	@Test
	public void testDeleteServicePlan() {
		ServicePlan servicePlan = createNewServicePlan(user);
		servicePlan.setId(id);
		Mockito.when(servicePlanRepository.save(servicePlan)).thenReturn(servicePlan);
		servicePlanService.save(servicePlan);
		Mockito.doNothing().when(servicePlanRepository).delete(servicePlan.getId());
		Assert.assertNotNull(servicePlanService.deleteById(servicePlan.getId()));
	}
}
