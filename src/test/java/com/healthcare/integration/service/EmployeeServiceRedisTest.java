package com.healthcare.integration.service;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

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

import com.healthcare.model.entity.Agency;
import com.healthcare.model.entity.AgencyType;
import com.healthcare.model.entity.Company;
import com.healthcare.model.entity.Employee;
import com.healthcare.model.enums.EmployeeTypeEnum;
import com.healthcare.repository.EmployeeRepository;
import com.healthcare.service.AgencyService;
import com.healthcare.service.AgencyTypeService;
import com.healthcare.service.CompanyService;
import com.healthcare.service.EmployeeService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class EmployeeServiceRedisTest {
    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private AgencyTypeService agencyTypeService;


    private Employee employee;
    private Agency agency;
    private Company company;
    private AgencyType agencyType;
    private Long id = 1L;

    @Before
    public void setup() {
    	company = companyService.save(TestEntityFactory.createNewCompany());
    	agencyType = agencyTypeService.save(TestEntityFactory.createNewAgencyType());
    	agency = agencyService.save(TestEntityFactory.createNewAgency(company, agencyType));
        employee = null;
    }

	@After
	public void rollback() {
		if(employee!=null){
			employeeService.deleteById(employee.getId());
		}
        agencyService.deleteById(agency.getId());
        agencyTypeService.deleteById(agencyType.getId());
        companyService.deleteById(company.getId());
	}


   @Test
    public void shouldSaveAEmployeeToRedisAndRetrievedItFromRedis() {
        employee = createNewEmployee();
        employee.setId(id);
        Mockito.when(employeeRepository.save(employee)).thenReturn(employee);
        employeeService.save(employee);
        Employee employeeSaved = employeeService.findById(id);
        Assert.assertNotNull(employeeSaved);
    }

   @Test
    public void shouldUpdateATrainingToRedis() {
        String newManager = "manager2";
        String newPosition = "position2";

        employee = createNewEmployee();
        employee.setId(id);
        Mockito.when(employeeRepository.save(employee)).thenReturn(employee);
        employeeService.save(employee);
        Employee employeeSaved = employeeService.findById(employee.getId());
//        employeeSaved.setManager(newManager);
        employeeSaved.setPosition(newPosition);
        Mockito.when(employeeRepository.save(employeeSaved)).thenReturn(employeeSaved);
        employeeService.save(employeeSaved);

        Employee employeeMofified = employeeService.findById(employee.getId());
        Assert.assertEquals(employeeMofified.getManager(), newManager);
        Assert.assertEquals(employeeMofified.getPosition(), newPosition);
    }

   @Test
    public void shouldDeleteAEmployee() {
        employee = createNewEmployee();
        employee.setId(id);
        Mockito.when(employeeRepository.save(employee)).thenReturn(employee);
        employeeService.save(employee);
        Mockito.doNothing().when(employeeRepository).delete(id);
        Assert.assertNotNull(employeeService.deleteById(employee.getId()));
    }

    @Test
    public void shouldDisableAEmployee() {
        employee = createNewEmployee();
        employee.setId(id);
        Mockito.when(employeeRepository.save(employee)).thenReturn(employee);
        employeeService.save(employee);
        Employee savedEmployee = employeeService.findById(employee.getId());
//        savedEmployee.setActive(0);
        Mockito.when(employeeRepository.save(savedEmployee)).thenReturn(savedEmployee);
        Long disableEmployeeId = employeeService.disableById(savedEmployee.getId());
        Assert.assertNotNull(disableEmployeeId);
        Employee disableEmployee = employeeService.findById(disableEmployeeId);
//        Assert.assertEquals(0, disableEmployee.getActive());
    }

   @Test
    public void testFindByCampanyIdAndAgencyId(){
    	List<Employee> employeeList = null;
    	Employee employee1 = TestEntityFactory.createNewEmployee(agency,1L);
    	Mockito.when(employeeRepository.save(employee1)).thenReturn(employee1);
    	employee = employeeService.save(employee1);

    	employeeList = employeeService.findByCampanyIdAndAgencyId(company.getId(), agency.getId());

    	Assert.assertNotNull(employeeList);
    	Assert.assertTrue(employeeList.size()==1);
    	Assert.assertEquals(employee.getId(),employeeList.get(0).getId());
    	verify(employeeRepository, never()).findByCompany(company.getId(), agency.getId());


    	// Create 2nd employee with new agency and new company
    	employeeList = null;
    	Company companyNew = companyService.save(TestEntityFactory.createNewCompany());
    	Agency agencyNew = agencyService.save(TestEntityFactory.createNewAgency(companyNew, agencyType));
    	Employee employee2 = TestEntityFactory.createNewEmployee(agencyNew,2L);
    	Mockito.when(employeeRepository.save(employee2)).thenReturn(employee2);
    	employee2 = employeeService.save(employee2);

        employeeList = employeeService.findByCampanyIdAndAgencyId(companyNew.getId(), agencyNew.getId());

        Assert.assertNotNull(employeeList);
    	Assert.assertTrue(employeeList.size()==1);
    	Assert.assertEquals(employee2.getId(),employeeList.get(0).getId());
    	verify(employeeRepository, never()).findByCompany(companyNew.getId(), agencyNew.getId());


    	// Create employee 3 to 2nd company and agency
    	employeeList = null;
    	Employee employee3 = TestEntityFactory.createNewEmployee(agencyNew,3L);
    	Mockito.when(employeeRepository.save(employee3)).thenReturn(employee3);
    	employee3 = employeeService.save(employee3);

    	employeeList = employeeService.findByCampanyIdAndAgencyId(companyNew.getId(), agencyNew.getId());
    	Assert.assertNotNull(employeeList);
    	Assert.assertTrue(employeeList.size()==2);
    	verify(employeeRepository, never()).findByCompany(companyNew.getId(), agencyNew.getId());


    	//CleanUp
		cleanup(companyNew, agencyNew, employee2, employee3);
    }

    private void cleanup(Company companyNew, Agency agencyNew, Employee employee2, Employee employee3) {
		employeeService.deleteById(employee3.getId());
		employeeService.deleteById(employee2.getId());
        agencyService.deleteById(agencyNew.getId());
        companyService.deleteById(companyNew.getId());
	}

   @Test
    public void testFindByCompany()
    {
    	Company company1 = new Company();
    	company1.setId(88L);
    	Company company2 = new Company();
    	company2.setId(99L);
    	Agency agency1 = new Agency();
    	agency1.setId(44L);
    	agency1.setCompany(company1);
    	Agency agency2 = new Agency();
    	agency2.setId(55L);
    	agency2.setCompany(company1);

    	Agency agency3 = new Agency();
    	agency3.setId(55L);
    	agency3.setCompany(company2);

    	Employee e1 = createNewEmployee();
    	e1.setId(77L);
    	e1.setAgency(agency1);
    	Mockito.when(employeeRepository.save(e1)).thenReturn(e1);
    	e1 = employeeService.save(e1);

    	Employee e2 = createNewEmployee();
    	e2.setId(88L);
    	e2.setAgency(agency2);
    	Mockito.when(employeeRepository.save(e2)).thenReturn(e2);
    	e2 = employeeService.save(e2);

    	Employee e3 = createNewEmployee();
    	e3.setId(99L);
    	e3.setAgency(agency3);
    	Mockito.when(employeeRepository.save(e3)).thenReturn(e3);
    	e3 = employeeService.save(e3);

    	Assert.assertEquals(employeeService.findByCampanyIdAndAgencyId(company1.getId(), agency1.getId()).size(), 1);
    	Assert.assertEquals(employeeService.findByCampanyIdAndAgencyId(company1.getId(), null).size(), 2);
    	Assert.assertEquals(employeeService.findByCampanyIdAndAgencyId(company2.getId(), agency3.getId()).size(), 1);
    	Assert.assertEquals(employeeService.findByCampanyIdAndAgencyId(company2.getId(), null).size(), 1);

    	employeeService.deleteById(e1.getId());
    	employeeService.deleteById(e2.getId());
    	employeeService.deleteById(e3.getId());
    }

    private Employee createNewEmployee() {
        Employee employee = new Employee();
        employee.setId(id);
        String firstName = "firstName";
        employee.setFirstName(firstName);
        String lastName = "lastName";
        employee.setLastName(lastName);
        String gender = "gender";
        employee.setGender(gender);
        String socialSecurityNumber = "socialSecurityNumber";
        employee.setSocialSecurityNumber(socialSecurityNumber);
        Calendar dateOfBirth = Calendar.getInstance();
        employee.setDateOfBirth(new Timestamp(dateOfBirth.getTimeInMillis()));
        String physicalExam = "physicalExam";
        employee.setPhysicalExam(physicalExam);
        String certificateName = "certificateName";
        employee.setCertificateName(certificateName);
        Calendar certificateStart = Calendar.getInstance();
        employee.setCertificateStart(new Timestamp(certificateStart.getTimeInMillis()));
        Calendar certificateEnd = Calendar.getInstance();
        employee.setCertificateEnd(new Timestamp(certificateEnd.getTimeInMillis()));
        Calendar workStart = Calendar.getInstance();
        employee.setWorkStart(new Timestamp(workStart.getTimeInMillis()));
        Calendar workEnd = Calendar.getInstance();
        employee.setWorkEnd(new Timestamp(workEnd.getTimeInMillis()));
        String position = "position";
        employee.setPosition(position);
        String manager = "manager";
//        employee.setManager(manager);
        String type = "type";
        employee.setType(type);
        String status = "status";
//        employee.setStatus(status);
        String backgroundCheck = "backgroundCheck";
        employee.setBackgroundCheck(backgroundCheck);
        employee.setAgency(new Agency());
        employee.setPin("pin");
        employee.setEmployeeType(EmployeeTypeEnum.SALARY);

        return employee;
    }
}
