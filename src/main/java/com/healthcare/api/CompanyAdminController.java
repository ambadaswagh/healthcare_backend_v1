package com.healthcare.api;

import javax.servlet.http.HttpServletRequest;

import com.healthcare.exception.UserException;
import com.healthcare.model.entity.Company;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.api.auth.AbstractBasedAPI;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.AdminAgencyCompanyOrganization;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.AdminAgencyCompanyOrganizationService;
import com.healthcare.service.AdminService;
import com.healthcare.service.impl.CustomUserValidator;
import com.healthcare.util.PasswordUtils;
import com.healthcare.validator.AdminAgencyCompanyOrganizationValidator;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.ArrayList;
import java.util.List;

import static com.healthcare.api.common.HealthcareConstants.AUTHENTICATED_ADMIN;
import static com.healthcare.api.common.HealthcareConstants.NOT_AUTHORIZED;
import static com.healthcare.api.common.HealthcareUtil.isEmpty;
import static com.healthcare.api.common.RoleUtil.isAgencyAdmin;
import static com.healthcare.api.common.RoleUtil.isCompanyAdmin;
import static com.healthcare.api.common.RoleUtil.isSuperAdmin;

@CrossOrigin
@RestController(value = "CompanyAdminRestAPI")
@RequestMapping(value = "/api/companyAdmin")
public class CompanyAdminController extends AbstractBasedAPI {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("companyAdminServiceImpl")
	private AdminService adminService;

	@Autowired
	private AdminAgencyCompanyOrganizationService adminAgencyCompanyOrganizationService; 

	
	@Autowired
	CustomUserValidator customUserValidator;

	@ApiOperation(value = "save company admin", notes = "save company admin")
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@ApiParam(name = "admin", value = "company admin to save", required = true)
	@PostMapping()
	public ResponseEntity<AdminAgencyCompanyOrganization> create(
			@RequestBody AdminAgencyCompanyOrganization adminAgencyCompanyOrganization, 
			HttpServletRequest req) {
		DataBinder binder = new DataBinder(adminAgencyCompanyOrganization);
		binder.setValidator(new AdminAgencyCompanyOrganizationValidator());
		binder.validate();
		BindingResult result = binder.getBindingResult();
		if (adminService.isAdminExisted(adminAgencyCompanyOrganization.getAdmin().getUsername())) {
			result.addError(new ObjectError("username", "Username existed"));
		}
		if (result.hasErrors()) {
			return new ResponseEntity(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		customUserValidator.validateAccess(req, adminAgencyCompanyOrganization.getAdmin());
		adminService.addNew(adminAgencyCompanyOrganization.getAdmin());
		
		AdminAgencyCompanyOrganization saved = adminAgencyCompanyOrganizationService.save(
				adminAgencyCompanyOrganization);
		return new ResponseEntity<>(saved, HttpStatus.OK);
	}

	@ApiOperation(value = "get company admin by id", notes = "get company admin by id")
	@ApiImplicitParam(name = "id", value = "company admin id", required = true, dataType = "Long", paramType = "path")
	@GetMapping("/{id}")
	public AdminAgencyCompanyOrganization read(@PathVariable Long id) {
		return adminAgencyCompanyOrganizationService.findByAdminId(id);
	}

	@ApiOperation(value = "get all company admin", notes = "get all company admin")
	@GetMapping()
	public ResponseEntity<Page<Admin>> readAll(HttpServletRequest req, @RequestParam MultiValueMap<String, String> attributes) {

		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if (isEmpty(admin)) {
			throw new UserException(NOT_AUTHORIZED);
		}

		if (isSuperAdmin(admin)) {
			MultiValueMapConverter<Admin> converter = new MultiValueMapConverter<>(attributes, Admin.class);
			return ResponseEntity.ok(adminService.findAll(converter.getPageable()));
		}else if (isCompanyAdmin(admin)) {
			Admin companyAdmin = adminService.findById(admin.getId());

			List<Admin> admins = new ArrayList<Admin>();

			if (companyAdmin != null) {
				admins.add(companyAdmin);
				return ResponseEntity.ok(new PageImpl<Admin>(admins));
			}
		}

		return null;
	}

	@ApiOperation(value = "get all company admin", notes = "get all company admin")
	@GetMapping("/company/{companyId}")
	public ResponseEntity<Page<Admin>> getAdminByCompany(HttpServletRequest req, @RequestParam MultiValueMap<String, String> attributes, @PathVariable("companyId") Long companyId) {

		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if (isEmpty(admin)) {
			throw new UserException(NOT_AUTHORIZED);
		}

		if (isSuperAdmin(admin) && companyId==0) {
			MultiValueMapConverter<Admin> converter = new MultiValueMapConverter<>(attributes, Admin.class);
			return ResponseEntity.ok(adminService.findAll(converter.getPageable()));
		}else if(isSuperAdmin(admin) && companyId!=0){
			MultiValueMapConverter<Admin> converter = new MultiValueMapConverter<>(attributes, Admin.class);
			return ResponseEntity.ok(adminAgencyCompanyOrganizationService.findAdminByCompanyId(companyId,converter.getPageable()));
		} else if (isCompanyAdmin(admin)) {
			Admin companyAdmin = adminService.findById(admin.getId());
			AdminAgencyCompanyOrganization adminCompany =  adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
			if(adminCompany != null)
				companyAdmin.setCompany(adminCompany.getCompany());
			List<Admin> admins = new ArrayList<Admin>();

			if (companyAdmin != null) {
				admins.add(companyAdmin);
				return ResponseEntity.ok(new PageImpl<Admin>(admins));
			}
		}

		return null;
	} 

	@ApiOperation(value = "update company admin", notes = "update company admin")
	@ApiParam(name = "admin", value = "company admin to update", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@PutMapping()
	public ResponseEntity update(@RequestBody AdminAgencyCompanyOrganization adminAgencyCompanyOrganization, 
			HttpServletRequest req) {
	    customUserValidator.validateAccess(req, adminAgencyCompanyOrganization.getAdmin());
	    
		DataBinder binder = new DataBinder(adminAgencyCompanyOrganization);
		binder.setValidator(new AdminAgencyCompanyOrganizationValidator());
		binder.validate();
		BindingResult result = binder.getBindingResult();
		if (result.hasErrors()) {
			return new ResponseEntity(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		Admin dbAdmin = adminService.findById(adminAgencyCompanyOrganization.getAdmin().getId());
		if (dbAdmin == null) {
			result.addError(new ObjectError("id", "Record not found"));
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		} else {
	        // copy all properties from admin to dbAdmin Object except password
	        // property
	        BeanUtils.copyProperties(adminAgencyCompanyOrganization.getAdmin(), dbAdmin, "password");
	        if (StringUtils.isNotBlank(adminAgencyCompanyOrganization.getAdmin().getPassword())) {
	            String hashPassword = PasswordUtils.hashPassword(adminAgencyCompanyOrganization.getAdmin().
	            		getPassword());
	            dbAdmin.setPassword(hashPassword);
	        }
		}
		
		adminService.save(dbAdmin);
		
		AdminAgencyCompanyOrganization saved = adminAgencyCompanyOrganizationService.save(
				adminAgencyCompanyOrganization);
		return new ResponseEntity<AdminAgencyCompanyOrganization>(saved, HttpStatus.OK);
	}

	@ApiOperation(value = "delete company admin", notes = "delete company admin")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "company admin id", required = true, dataType = "Long", paramType = "path"),
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header") })
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id, HttpServletRequest req) {
		customUserValidator.validateAccess(req, id);
		adminService.deleteById(id);
	}

	@ApiOperation(value = "disable company admin", notes = "disable company admin")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "company admin id", required = true, dataType = "Long", paramType = "path"),
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header") })
	@PutMapping("/{id}/disable")
	public void disable(@PathVariable("id") Long id, HttpServletRequest req) {
		customUserValidator.validateAccess(req, id);
		logger.info("id : " + id);
		adminService.disableById(id);
	}
}
