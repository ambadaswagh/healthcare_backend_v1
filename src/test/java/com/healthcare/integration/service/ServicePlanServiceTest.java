package com.healthcare.integration.service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import javax.transaction.Transactional;

import org.hamcrest.collection.IsEmptyCollection;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.healthcare.EntityFactory;
import com.healthcare.model.entity.Agency;
import com.healthcare.model.entity.AgencyType;
import com.healthcare.model.entity.Company;
import com.healthcare.model.entity.Employee;
import com.healthcare.model.entity.ServicePlan;
import com.healthcare.model.entity.User;
import com.healthcare.model.enums.DayEnum;
import com.healthcare.service.AgencyService;
import com.healthcare.service.AgencyTypeService;
import com.healthcare.service.CompanyService;
import com.healthcare.service.EmployeeService;
import com.healthcare.service.ServicePlanService;
import com.healthcare.service.UserService;
import com.healthcare.util.DateUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ServicePlanServiceTest extends EntityFactory {
	@Autowired
	private ServicePlanService servicePlanService;

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

	@Value("${spring.data.rest.default-page-size}")
	private int defaultMaxSize;

	private User user;
	private Employee employee;
	private Company company;
	private AgencyType agencyType;
	private Agency agency;
	private ServicePlan servicePlan;
	
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
		servicePlan = null;
	}

	@After
	public void rollback() {
		if(servicePlan!=null)
			servicePlanService.deleteById(servicePlan.getId());
		employeeService.deleteById(employee.getId());
		userService.deleteById(user.getId());
        agencyService.deleteById(agency.getId());
        agencyTypeService.deleteById(agencyType.getId());
        companyService.deleteById(company.getId());
	}

	
	@Test
	public void testSaveServicePlan() {
		servicePlan = createNewServicePlan(user);
		servicePlanService.save(servicePlan);
		Assert.assertNotNull(servicePlan.getId());
	}

	@Test
	public void testGetServicePlan() {
		servicePlan = createNewServicePlan(user);
		servicePlanService.save(servicePlan);
		Assert.assertNotNull(servicePlanService.findById(servicePlan.getId()));
	}

	@Test
	public void testGetServiceCalendarPlan() {
		// days = MONDAY,THURSDAY
		// plan_start = 2017-06-01
		// plan_end = 2017-12-01
		servicePlan = createNewServicePlan(user);
		servicePlanService.save(servicePlan);
		List<String> serviceCalendar = servicePlanService.getServiceCalendar(servicePlan.getId());

		// expect valid service plan (not returning null)
		Assert.assertNotNull(servicePlan);

		// expect there are 53 days
		Assert.assertEquals(53, serviceCalendar.size());

		// expect the first day is on 2017-06-01
		Assert.assertTrue("2017-06-01".equals(serviceCalendar.get(0)));

		// expect the last day is on 2017-11-30
		Assert.assertTrue("2017-11-30".equals(serviceCalendar.get(serviceCalendar.size() - 1)));

		// update the period, plan_end before plan_start
		Timestamp temp = servicePlan.getPlanEnd();
		servicePlan.setPlanEnd(servicePlan.getPlanStart());
		servicePlan.setPlanStart(temp);
		ServicePlan result = servicePlanService.save(servicePlan);

		// expect return null cause of invalid period
		Assert.assertNull(result);

		// update the days, input any string that not in DayEnum
		ServicePlan servicePlan1 = createNewServicePlan(user);
		servicePlan1.setDays("MON,TUE");
		servicePlan1 = servicePlanService.save(servicePlan1);

		// expect return null cause of invalid days
		Assert.assertNull(servicePlan1);

		// update the days, input any day but not in period;
		ServicePlan servicePlanNew = createNewServicePlan(user);
		servicePlanNew.setDays(DayEnum.MONDAY.name() + "," + DayEnum.WEDNESDAY.name());
		servicePlanNew.setPlanEnd(new Timestamp(DateUtils.stringToDate("yyyy-MM-dd", "2017-06-04").getTime()));
		servicePlanService.save(servicePlanNew);

		// expect return empty array list
		Assert.assertTrue(servicePlanService.getServiceCalendar(servicePlanNew.getId()).isEmpty());
		
		//Cleanup
		servicePlanService.deleteById(servicePlanNew.getId());
	}

	@Test
	public void testUpdateServicePlan() {
		String newDocUrl = "/doc/new/a";
		servicePlan = createNewServicePlan(user);
		servicePlanService.save(servicePlan);
//		Assert.assertEquals(servicePlan.getDocUrl(), docUrl);
		ServicePlan savedServicePlan = servicePlanService.findById(servicePlan.getId());
//		savedServicePlan.setDocUrl(newDocUrl);
		servicePlanService.save(savedServicePlan);
		ServicePlan modifiedServicePlan = servicePlanService.findById(servicePlan.getId());
//		Assert.assertEquals(modifiedServicePlan.getDocUrl(), newDocUrl);
	}

	@Test
	public void testDeleteServicePlan() {
		servicePlan = createNewServicePlan(user);
		servicePlanService.save(servicePlan);
		Assert.assertNotNull(servicePlan.getId());
		servicePlanService.deleteById(servicePlan.getId());
		servicePlan = servicePlanService.findById(servicePlan.getId());
		Assert.assertNull(servicePlan);
	}
	
    @Test
    public void testserviceCalendarGeneration(){
    	// given
    	List<Date> expectedList = Arrays.asList(new GregorianCalendar(2017, Calendar.JUNE, 06).getTime(), new GregorianCalendar(2017, Calendar.JUNE, 13).getTime());
    	//when
    	servicePlan = servicePlanService.save(createNewServicePlan(user));
    	List<Date> result = servicePlanService.serviceCalendarGeneration(servicePlan.getId());
    	
    	//then
    	//assert not null
    	assertThat(result, notNullValue());
    	//assert is expected
    	assertThat(result, is(not(expectedList)));
        //assert has item
        assertThat(result, hasItems(new GregorianCalendar(2017, Calendar.JUNE, 01).getTime()));
        //assert size
        assertThat(result, hasSize(53));	
        //check empty list
        assertThat(result, not(IsEmptyCollection.empty()));	
        //assert random list item betwen start and end plan date
        assertTrue(result.get(0).before(servicePlan.getPlanEnd()));
        assertTrue(result.get(1).after(servicePlan.getPlanStart()));
    }
	
}