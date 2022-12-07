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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.healthcare.model.entity.Activity;
import com.healthcare.model.entity.Agency;
import com.healthcare.model.entity.AgencyType;
import com.healthcare.model.entity.Company;
import com.healthcare.model.entity.EmployeeActivity;
import com.healthcare.model.entity.Employee;
import com.healthcare.model.entity.EmployeeActivity;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.ActivityService;
import com.healthcare.service.AgencyService;
import com.healthcare.service.AgencyTypeService;
import com.healthcare.service.CompanyService;
import com.healthcare.service.EmployeeActivityService;
import com.healthcare.service.EmployeeService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class EmployeeActivityServiceTest extends TestEntityFactory {

    @Autowired
    private EmployeeActivityService employeeActivityService;

    @Autowired
    private ActivityService activityService;

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

    private Employee employee;
    private Activity activity;
    private Agency agency;
    private Company company;
    private AgencyType agencyType;
	private EmployeeActivity employeeActivity;
    private Long id = 7L;
	
	
	@Before
	public void setup() {
		company = companyService.save(TestEntityFactory.createNewCompany());
    	agencyType = agencyTypeService.save(TestEntityFactory.createNewAgencyType());
    	agency = agencyService.save(TestEntityFactory.createNewAgency(company,agencyType));
    	employee = employeeService.save(TestEntityFactory.createNewEmployee(agency));
    	activity = activityService.save(TestEntityFactory.createNewActivity(employee));
    	employeeActivity = null;
	}

	@After
	public void rollback() {
        if(employeeActivity!=null){
        	employeeActivityService.deleteById(employeeActivity.getId());
        }
        
		activityService.deleteById(activity.getId());
        employeeService.deleteById(employee.getId());
        agencyService.deleteById(agency.getId());
        agencyTypeService.deleteById(agencyType.getId());
        companyService.deleteById(company.getId());
	}

  
    @Test
    public void shouldSaveAEmployeeActivity() {
        employeeActivity = createNewEmployeeActivity(employee, activity);
        employeeActivity = employeeActivityService.save(employeeActivity);
        Assert.assertNotNull(employeeActivity);
    }

    @Test
    public void shouldGetAEmployeeActivity() {
        employeeActivity = createNewEmployeeActivity(employee, activity);
        employeeActivity = employeeActivityService.save(employeeActivity);
        Assert.assertNotNull(employeeActivityService.findByEmployeeActivity(employeeActivity));
    }

    @Test
    public void shouldUpdateAEmployeeActivity() {
        Employee newEmployee = createNewEmployee(agency);
        Activity newActivity = createNewActivity(employee);

        employeeActivity = createNewEmployeeActivity(employee, activity);
        employeeActivity = employeeActivityService.save(employeeActivity);
        Assert.assertEquals(employeeActivity.getEmployee(), employee);
        Assert.assertEquals(employeeActivity.getActivity(), activity);

        EmployeeActivity employeeActivitySaved = employeeActivityService.save(employeeActivity);
        employeeActivitySaved.setEmployee(newEmployee);
        employeeActivitySaved.setActivity(newActivity);
        employeeActivitySaved = employeeActivityService.save(employeeActivitySaved);

        EmployeeActivity employeeActiityMofified = employeeActivityService.findById(employeeActivitySaved.getId());
        Assert.assertEquals(employeeActiityMofified.getEmployee(), newEmployee);
        Assert.assertEquals(employeeActiityMofified.getActivity(), newActivity);
    }

    @Test
    public void shouldDeleteAEmployeeActivity() {
        EmployeeActivity employeeActivity = createNewEmployeeActivity(employee, activity);
        Assert.assertNotNull(employeeActivity);
        employeeActivityService.deleteByEmployeeActivity(employeeActivity);
        Assert.assertNull(employeeActivityService.findById(employeeActivity.getId()));
    }

    protected EmployeeActivity createNewEmployeeActivity(Employee employee, Activity activity) {
        EmployeeActivity employeeActivity = new EmployeeActivity();
        employeeActivity.setId(id);
        employeeActivity.setEmployee(employee);
        employeeActivity.setActivity(activity);
        return employeeActivity;
    }
    
	@Test
	public void testFindAllPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			EmployeeActivity obj = employeeActivityService.save(createNewEmployeeActivity(employee, activity));
			listIds.add(obj.getId());
		}

		// PAGE 0
		// given
		Pageable pageable = new CustomPageRequest();
		// when
		Page<EmployeeActivity> result = employeeActivityService.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == defaultMaxSize);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 0);

		// PAGE 2
		pageable = new PageRequest(1, 5);
		// when
		result = employeeActivityService.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == 5);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 1);

		// delete created data
		for (Long id : listIds) {
			employeeActivityService.deleteById(id);
		}
	}

	@Test
	public void testSearchWithPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			EmployeeActivity obj = employeeActivityService.save(createNewEmployeeActivity(employee, activity));
			listIds.add(obj.getId());
		}
		// SEARCH 1 : FIND BY ID
		// given
		Pageable pageable = new PageRequest(0, 25);
		EmployeeActivity obj = new EmployeeActivity();
		obj.setId(listIds.get(0));

		// when
		Page<EmployeeActivity> result = employeeActivityService.findAll(obj, pageable);
		// then
		assertTrue(result.getContent().size() == 1);
		assertTrue(result.getTotalElements() >= 1);
		assertTrue(result.getTotalPages() >= 1);
		assertTrue(result.getNumber() == 0);

		// SEARCH 2
		// given
		pageable = new PageRequest(3, 5);
		obj = new EmployeeActivity();

		// when
		result = employeeActivityService.findAll(obj, pageable);
		// then
		assertTrue(result.getContent().size() == 5);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 3);

		// Delete created data
		for (Long id : listIds) {
			employeeActivityService.deleteById(id);
		}
	}
}
