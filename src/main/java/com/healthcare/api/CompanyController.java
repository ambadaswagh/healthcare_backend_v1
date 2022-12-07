package com.healthcare.api;

import static com.healthcare.api.common.HealthcareConstants.AUTHENTICATED_ADMIN;
import static com.healthcare.api.common.HealthcareConstants.NOT_AUTHORIZED;
import static com.healthcare.api.common.HealthcareUtil.isEmpty;
import static com.healthcare.api.common.RoleUtil.isAgencyAdmin;
import static com.healthcare.api.common.RoleUtil.isCompanyAdmin;
import static com.healthcare.api.common.RoleUtil.isSuperAdmin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.healthcare.repository.CompanyRepository;
import com.healthcare.model.entity.*;
import com.healthcare.service.AdminAgencyCompanyOrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.exception.UserException;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.AgencyService;
import com.healthcare.service.CompanyService;
import com.healthcare.service.EmployeeService;
import com.healthcare.util.DateUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/api/company")
public class CompanyController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CompanyService companyService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private AgencyService agencyService;
	@Autowired
	private AdminAgencyCompanyOrganizationService adminAgencyCompanyOrganizationService;

	@Autowired
	CompanyRepository companyRepository;

	@ApiOperation(value = "save company", notes = "save company")
	@ApiParam(name = "company", value = "company to save", required = true)
	@PostMapping()
	public ResponseEntity<Company> create(@RequestBody Company company) {
		company = companyService.save(company);
		return new ResponseEntity<Company>(company, HttpStatus.OK);
	}

	@ApiOperation(value = "get company by id", notes = "get company by id")
	@ApiImplicitParam(name = "id", value = "company id", required = true, dataType = "Long", paramType = "path")
	@GetMapping("/{id}")
	public Company read(HttpServletRequest req, @PathVariable Long id) {
		logger.info("id : " + id);
		Company company = companyService.findById(id);
        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
        if (!isEmpty(admin) && isAgencyAdmin(admin)) {
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
            if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
                String timezone = DateUtils.get_timezone(adminAgencyCompanyOrganization.getAgency().getTimezone());
            	company = get_object_with_local_times(company, timezone);
            }
        }
		return company;
	}

	@ApiOperation(value = "get all company", notes = "get all company")
	@GetMapping()
	public ResponseEntity<Page<Company>> readAll(HttpServletRequest req, @RequestParam MultiValueMap<String, String> attributes) {
		/*MultiValueMapConverter<Company> converter = new MultiValueMapConverter<>(attributes, Company.class);
		return ResponseEntity.ok(companyService.findAll(converter.getData(), converter.getPageable()));*/
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if (isEmpty(admin)) {
			throw new UserException(NOT_AUTHORIZED);
		}

		MultiValueMapConverter<Company> converter = new MultiValueMapConverter<>(attributes, Company.class);
		if (isSuperAdmin(admin)) {
			return ResponseEntity.ok(companyService.findAll(converter.getData(), converter.getPageable()));
		}
		else if (isCompanyAdmin(admin) || isAgencyAdmin(admin)){

			// get company id by admin id
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
			if (adminAgencyCompanyOrganization != null) {

				Company foundCompany = companyService.findById(adminAgencyCompanyOrganization.getCompany().getId());

				List<Company> companies = new ArrayList<Company>();

				if (foundCompany != null) {
					companies.add(foundCompany);
					return ResponseEntity.ok(new PageImpl<Company>(companies));
				}
			}
		}

		return null;
	}

	@ApiOperation(value = "get all Company", notes = "get all Company")
	@GetMapping("/getAllCompany")
	public ResponseEntity<List<Company>> getAllCompanyList(HttpServletRequest req) {
		List<Company> companies = companyRepository.findAll();
        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
        if (!isEmpty(admin) && isAgencyAdmin(admin)) {
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
            if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
                String timezone = DateUtils.get_timezone(adminAgencyCompanyOrganization.getAgency().getTimezone());
                for(Company company : companies){
                	company = get_object_with_local_times(company, timezone);
                }
                
            }
        }
		return ResponseEntity.ok(companies);
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "get all company", notes = "get all company")
	@GetMapping("/valid")
	public List<Company> readAllWithRole(HttpServletRequest req, @RequestParam MultiValueMap<String, String> attributes) {
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if (isEmpty(admin)) {
			throw new UserException(NOT_AUTHORIZED);
		}

		if (isSuperAdmin(admin)) {
			return companyService.findAll();
		} else if (isCompanyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
			if (adminAgencyCompanyOrganization != null) {
				return (List<Company>) Collections
						.singleton(companyService.findById(adminAgencyCompanyOrganization.getCompany().getId()));
			}
		}
		return null;

	}

	@ApiOperation(value = "update company", notes = "update company")
	@ApiParam(name = "company", value = "company to update", required = true)
	@PutMapping()
	public ResponseEntity<Company> update(@RequestBody Company company) {
		company = companyService.save(company);
		return new ResponseEntity<Company>(company, HttpStatus.OK);
	}

	@ApiOperation(value = "delete company", notes = "delete company")
	@ApiImplicitParam(name = "id", value = "company id", required = true, dataType = "Long", paramType = "path")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		companyService.deleteById(id);
	}

	@ApiOperation(value = "get employees by company id", notes = "get employees by company id")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "companyId", value = "company id", required = true, dataType = "Long", paramType = "path") })
	@ApiParam(name = "agencyId", value = "agency id", required = false)
	@GetMapping("/{companyId}/employees")
	public List<Employee> getEmployeesByCompany(@PathVariable("companyId") Long companyId,
			@RequestParam(value = "agencyId", required = false) Long agencyId) {
		return employeeService.findByCampanyIdAndAgencyId(companyId, agencyId);
	}

	@ApiOperation(value = "get agency by company id", notes = "get agency by company id")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "companyId", value = "company id", required = true, dataType = "Long", paramType = "path") })
	@ApiParam(name = "agencyId", value = "agency id", required = false)
	@GetMapping("/{companyId}/agency")
	public List<Agency> getAgencyByCompany(@PathVariable("companyId") Long companyId) {
		return agencyService.findByCompany(companyId);
	}

	@ApiOperation(value = "diable company", notes = "disable company")
	@ApiImplicitParam(name = "id", value = "company id", required = true, dataType = "Long", paramType = "path")
	@PutMapping("/{id}/disable")
	public void disable(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		companyService.disableById(id);
	}

	@ApiOperation(value = "check name for company", notes = "check name for company")
	@ApiParam(name = "name", value = "company name", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@PostMapping("/name/{name}")
	public ResponseEntity<Boolean> checkNameExisted(@PathVariable String name) {
		boolean isValid = companyService.isCompanyName(name);
		return new ResponseEntity<Boolean>(isValid, HttpStatus.OK);
	}
	

	
	public Page<Company> get_local_time(List<Company> companies, String target_time_zone){
        for(Company company : companies){
        	company = get_object_with_local_times(company, target_time_zone);
        }

        return new PageImpl<Company>(companies);
    }

    public Company get_object_with_local_times(Company company, String target_time_zone){
        if(company.getWorktimeStart() != null){
        	company.setWorktimeStart(DateUtils.get_local_time(company.getWorktimeStart(), target_time_zone));
        }
       if(company.getWorktimeEnd() != null){
    	   company.setWorktimeEnd(DateUtils.get_local_time(company.getWorktimeEnd(), target_time_zone));
       }
       return company;
}
}
