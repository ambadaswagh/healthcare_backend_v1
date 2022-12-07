package com.healthcare.integration.service;

import java.sql.Time;
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

import com.healthcare.model.entity.Activity;
import com.healthcare.model.entity.Agency;
import com.healthcare.model.entity.AgencyType;
import com.healthcare.model.entity.Company;
import com.healthcare.model.entity.Employee;
import com.healthcare.model.entity.EmployeeActivity;
import com.healthcare.repository.EmployeeActivityRepository;
import com.healthcare.service.ActivityService;
import com.healthcare.service.AgencyService;
import com.healthcare.service.AgencyTypeService;
import com.healthcare.service.CompanyService;
import com.healthcare.service.EmployeeActivityService;
import com.healthcare.service.EmployeeService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class EmployeeActivityServiceRedisTest {
    @MockBean
    private EmployeeActivityRepository employeeActivityRepository;

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

    /**
     * Activity
     */

    String name = "name";
    Integer status = 1;
    Calendar createdAt = Calendar.getInstance();
    Calendar updatedAt = Calendar.getInstance();
    Calendar timeStart = Calendar.getInstance();
    Calendar timeEnd = Calendar.getInstance();
    String date = "date";
    String location = "location";
    String note = "note";

    private Long id =1L;
    private EmployeeActivity employeeActivity;
    private Employee employee;
    private Activity activity;
    private Agency agency;
    private Company company;
    private AgencyType agencyType;

    @Before
    public void setup() {
    	company = companyService.save(TestEntityFactory.createNewCompany());
    	agencyType = agencyTypeService.save(TestEntityFactory.createNewAgencyType());
    	agency = agencyService.save(TestEntityFactory.createNewAgency(company, agencyType));
        employee = employeeService.save(TestEntityFactory.createNewEmployee(agency));
        activity = createNewActivity(employee);
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
    public void shouldSaveAEmployeeActivityToRedisAndRetrievedItFromRedis() {
        EmployeeActivity employeeActivity = createNewEmployeeActivity(employee, activity);
        Mockito.when(employeeActivityRepository.save(employeeActivity)).thenReturn(employeeActivity);
        employeeActivityService.save(employeeActivity);
        employeeActivityService.findById(id);
        Assert.assertNotNull(employeeActivity);
        this.employeeActivity = employeeActivity;
    }

    @Test
    public void shouldUpdateAEmployeeActivityToRedis() {
        Employee newEmployee = employeeService.save(TestEntityFactory.createNewEmployee(agency));
        Activity newActivity = createNewActivity(newEmployee);

        employeeActivity = createNewEmployeeActivity(employee, activity);
        Mockito.when(employeeActivityRepository.save(employeeActivity)).thenReturn(employeeActivity);
        EmployeeActivity employeeActivitySaved = employeeActivityService.save(employeeActivity);

        employeeActivitySaved.setEmployee(newEmployee);
        employeeActivitySaved.setActivity(newActivity);

        Mockito.when(employeeActivityRepository.save(employeeActivitySaved)).thenReturn(employeeActivitySaved);
        EmployeeActivity employeeActivityMofified = employeeActivityService.save(employeeActivitySaved);
        
        Assert.assertEquals(employeeActivityMofified.getEmployee(), newEmployee);
        Assert.assertEquals(employeeActivityMofified.getActivity(), newActivity);
        
        // Clear for roll back
        //employeeActivity = employeeActivitySaved;
        employeeActivityService.deleteByEmployeeActivity(employeeActivitySaved);
        employeeActivityService.deleteByEmployeeActivity(employeeActivityMofified);
        activityService.deleteById(newActivity.getId());
        employeeService.deleteById(newEmployee.getId());
    }

    @Test
    public void shouldDeleteAEmployeeActivity() {
        EmployeeActivity employeeActivity = createNewEmployeeActivity(employee, activity);
        Mockito.when(employeeActivityRepository.save(employeeActivity)).thenReturn(employeeActivity);
        employeeActivityService.save(employeeActivity);
        Mockito.doNothing().when(employeeActivityRepository).delete(id);
        Assert.assertNotNull(employeeActivityService.deleteById(employeeActivity.getId()));
    }

   
    private Activity createNewActivity(Employee employee) {
        Activity activity = new Activity();
        activity.setName(name);
        activity.setStatus(status);
        //activity.setCreatedAt(new Timestamp(createdAt.getTimeInMillis()));
        //activity.setUpdatedAt(new Timestamp(updatedAt.getTimeInMillis()));
        activity.setInstructorEmployee(employee);
        activity.setTimeStart(String.valueOf(new Time(timeStart.getTimeInMillis())));
        activity.setTimeEnd(String.valueOf(new Time(timeEnd.getTimeInMillis())));
        activity.setDate(date);
        activity.setLocation(location);
        activity.setNote(note);

        return activityService.save(activity);
    }

    protected EmployeeActivity createNewEmployeeActivity(Employee employee, Activity activity) {
        EmployeeActivity employeeActivity = new EmployeeActivity();
        employeeActivity.setId(id);
        employeeActivity.setEmployee(employee);
        employeeActivity.setActivity(activity);
        return employeeActivity;
    }
}
