package com.healthcare.integration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.Agency;
import com.healthcare.model.entity.AgencyType;
import com.healthcare.model.entity.Company;
import com.healthcare.model.entity.Document;
import com.healthcare.model.entity.Employee;
import com.healthcare.model.entity.HealthInsuranceClaim;
import com.healthcare.model.entity.Role;
import com.healthcare.model.entity.User;
import com.healthcare.service.ActivityService;
import com.healthcare.service.AdminService;
import com.healthcare.service.AgencyService;
import com.healthcare.service.AgencyTypeService;
import com.healthcare.service.CompanyService;
import com.healthcare.service.DocumentService;
import com.healthcare.service.EmployeeService;
import com.healthcare.service.HealthInsuranceClaimService;
import com.healthcare.service.RoleService;
import com.healthcare.service.ServicePlanService;
import com.healthcare.service.UserService;
import com.healthcare.service.WorkItemService;

@ComponentScan
@Service
public class TestEntityServiceFactory {
	
	@Autowired
	@Qualifier("adminServiceImpl")
	private AdminService adminService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private AgencyService agencyService;

	@Autowired
	private AgencyTypeService agencyTypeService;

	@Autowired 
	private EmployeeService employeeSerice;
	
	
	@Autowired 
	private UserService userService;
	
	@Autowired 
	private ActivityService activityService;
	
	@Autowired 
	private ServicePlanService servicePlan;

	@Autowired 
	private WorkItemService workItemService;


	@Autowired 
	private DocumentService documentService;
	

	@Autowired 
	private HealthInsuranceClaimService healthInsuranceClaimService;

	@Autowired
	RedisTemplate redisTemplate;
	
	public boolean saveData = false;

	public Company createCompany(){
		return saveData ? companyService.save(TestEntityFactory.createNewCompany()) : TestEntityFactory.createNewCompany();
	}
	
	public AgencyType createAgencyType(){
		return saveData ? agencyTypeService.save(TestEntityFactory.createNewAgencyType()) : TestEntityFactory.createNewAgencyType();
	}

	public Agency createAgency(){
		return saveData ? agencyService.save(TestEntityFactory.createNewAgency(createCompany(),createAgencyType())) :
			TestEntityFactory.createNewAgency(createCompany(),createAgencyType());
	}
	
	public Employee createEmployee(){
		return saveData ? employeeSerice.save(TestEntityFactory.createNewEmployee(createAgency())) : TestEntityFactory.createNewEmployee(createAgency());
	}
	
	public Role createRole(long level){
		return saveData ? roleService.save(TestEntityFactory.createNewRole(createAgency(), createEmployee(), level)) : TestEntityFactory.createNewRole(createAgency(), createEmployee(), level);
	}
	
	public Admin createAdmin(){
		return saveData ? adminService.save(TestEntityFactory.createNewAdmin(createRole(1L))) : TestEntityFactory.createNewAdmin(createRole(1L));
	}
	
	public User createUser(){
		return saveData ? userService.save(TestEntityFactory.createNewUser()) : TestEntityFactory.createNewUser() ;
	}
	
	public Document creatDocument(){
		return saveData ? documentService.save(TestEntityFactory.createNewDocument()) : TestEntityFactory.createNewDocument() ;
	}
	
	public HealthInsuranceClaim creatHealthClaim(){
		return saveData ? healthInsuranceClaimService.save(TestEntityFactory.createNewHealthInsuraceClaim(createUser(), creatDocument())) : TestEntityFactory.createNewHealthInsuraceClaim(createUser(), creatDocument()) ;
	}
	
	public void redisCleanUp(){
		redisTemplate.delete(HealthInsuranceClaim.class.getSimpleName());
		redisTemplate.delete(User.class.getSimpleName());
		redisTemplate.delete(Role.class.getSimpleName());
		redisTemplate.delete(Admin.class.getSimpleName());
		redisTemplate.delete(Employee.class.getSimpleName());
		redisTemplate.delete(Agency.class.getSimpleName());
		redisTemplate.delete(AgencyType.class.getSimpleName());
		redisTemplate.delete(Company.class.getSimpleName());
		redisTemplate.delete(Document.class.getSimpleName());
	}
}
