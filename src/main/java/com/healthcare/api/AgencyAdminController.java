package com.healthcare.api;

import javax.servlet.http.HttpServletRequest;

import com.healthcare.exception.UserException;
import com.healthcare.validator.AdminAgencyCompanyOrganizationValidator;
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
@RestController(value = "AgencyAdminRestAPI")
@RequestMapping(value = "/api/agencyAdmin")
public class AgencyAdminController extends AbstractBasedAPI {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("agencyAdminServiceImpl")
	private AdminService adminService;
	
	@Autowired
	AdminAgencyCompanyOrganizationService adminAgencyCompanyOrganizationService;
	
	@Autowired
	CustomUserValidator customUserValidator;


	@ApiOperation(value = "save agency admin", notes = "save agency admin")
	@ApiParam(name = "admin", value = "agency admin to save", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType= "header")
	@PostMapping()
	public ResponseEntity<AdminAgencyCompanyOrganization> create(@RequestBody AdminAgencyCompanyOrganization entity, 
			HttpServletRequest req) {

		DataBinder binder = new DataBinder(entity);
		binder.setValidator(new AdminAgencyCompanyOrganizationValidator());
		binder.validate();
		BindingResult result = binder.getBindingResult();
		if (adminService.isAdminExisted(entity.getAdmin().getUsername())) {
			result.addError(new ObjectError("username", "Username existed"));
		}
		if (result.hasErrors()) {
			return new ResponseEntity(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		customUserValidator.validateAccess(req, entity.getAdmin());
		adminService.addNew(entity.getAdmin());

		AdminAgencyCompanyOrganization saved = adminAgencyCompanyOrganizationService.save(
				entity);
		return new ResponseEntity<>(saved, HttpStatus.OK);
	}

	@ApiOperation(value = "get agency admin by id", notes = "get agency admin by id")
	@ApiImplicitParam(name = "id", value = "agency admin id", required = true, dataType = "Long",paramType = "path")
	@GetMapping("/{id}")
	public AdminAgencyCompanyOrganization read(@PathVariable Long id) {
		logger.info("id : " + id);
		return adminAgencyCompanyOrganizationService.findByAdminId(id);
	}

	@ApiOperation(value = "get all agency admin", notes = "get all agency admin")
	@GetMapping()
	public ResponseEntity<Page<Admin>> readAll(HttpServletRequest req, @RequestParam MultiValueMap<String, String> attributes) {
		/*MultiValueMapConverter<Admin> converter = new MultiValueMapConverter<>(attributes, Admin.class);
		return ResponseEntity.ok(adminService.findAll(converter.getData(), converter.getPageable()));*/
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if (isEmpty(admin)) {
			throw new UserException(NOT_AUTHORIZED);
		}

		MultiValueMapConverter<Admin> converter = new MultiValueMapConverter<>(attributes, Admin.class);
		if (isSuperAdmin(admin)) {
			return ResponseEntity.ok(adminService.findAll(converter.getData(), converter.getPageable()));
		} else if (isCompanyAdmin(admin)) {

			// get current admin agency company
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null) {
				List<Long> adminIds = adminAgencyCompanyOrganizationService.findAdminIdByAdminCompanyId(adminAgencyCompanyOrganization.getCompany().getId());
				if (adminIds != null && adminIds.size() > 0) {
					return ResponseEntity.ok(adminService.findAllAdminByAdminIds(adminIds, converter.getPageable()));
				}
			}
		}

		else if (isAgencyAdmin(admin)) {
			Admin agencyAdmin = adminService.findById(admin.getId());

			List<Admin> admins = new ArrayList<Admin>();

			if (agencyAdmin != null) {
				admins.add(agencyAdmin);
				return ResponseEntity.ok(new PageImpl<Admin>(admins));
			}
		}

		return null;
	}


	@ApiOperation(value = "update agency admin", notes = "update agency admin")
	@ApiParam(name = "admin", value = "agency admin to update", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType= "header")
	@PutMapping()
	public ResponseEntity update(@RequestBody AdminAgencyCompanyOrganization entity,
			HttpServletRequest req) {
		
		Admin admin = entity.getAdmin();
		customUserValidator.validateAccess(req, admin);

		DataBinder binder = new DataBinder(entity);
		binder.setValidator(new AdminAgencyCompanyOrganizationValidator());
		binder.validate();
		BindingResult result = binder.getBindingResult();
		if (result.hasErrors()) {
			return new ResponseEntity(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		Admin dbAdmin = adminService.findById(admin.getId());
		if (dbAdmin == null) {
			result.addError(new ObjectError("id", "Record not found"));
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		} else {
			// copy all properties from admin to dbAdmin Object except password
			// property
			BeanUtils.copyProperties(admin, dbAdmin, "password");
			if (StringUtils.isNotBlank(admin.getPassword())) {
				String hashPassword = PasswordUtils.hashPassword(admin.
						getPassword());
				dbAdmin.setPassword(hashPassword);
			}
		}

		adminService.save(dbAdmin);

		AdminAgencyCompanyOrganization saved = adminAgencyCompanyOrganizationService.save(
				entity);
		
		return new ResponseEntity<AdminAgencyCompanyOrganization>(saved, HttpStatus.OK);
	}

	@ApiOperation(value = "delete agency admin", notes = "delete agency admin")
	@ApiImplicitParams(
			{@ApiImplicitParam(name = "id", value = "agency admin id", required = true, dataType = "Long",paramType = "path")
			,@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType= "header")}
	)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id, HttpServletRequest req) {
		customUserValidator.validateAccess(req, id);
		logger.info("id : " + id);
		adminService.deleteById(id);
	}

	@ApiOperation(value = "disable agency admin", notes = "disable agency admin")
	@ApiImplicitParams(
			{@ApiImplicitParam(name = "id", value = "agency admin id", required = true, dataType = "Long",paramType = "path")
					,@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType= "header")}
	)
	@PutMapping("/{id}/disable")
	public void disable(@PathVariable("id") Long id, HttpServletRequest req) {
		customUserValidator.validateAccess(req, id);
		logger.info("id : " + id);
		adminService.disableById(id);
	}

	@ApiOperation(value = "get all agency admin by company id", notes = "get all agency by company id")
	@GetMapping("/company/{companyId}")
	public ResponseEntity<Page<Admin>> getAgenciesByCompany(@PathVariable("companyId") Long companyId, @RequestParam MultiValueMap<String, String> attributes) {
		MultiValueMapConverter<Admin> converter = new MultiValueMapConverter<>(attributes, Admin.class);
		List<Long> adminIds = adminAgencyCompanyOrganizationService.findAdminIdByAdminCompanyId(companyId);
		if (adminIds != null && adminIds.size() > 0) {
			return ResponseEntity.ok(adminService.findAllAdminByAdminIds(adminIds, converter.getPageable()));
		}

		return ResponseEntity.ok(new PageImpl<Admin>(new ArrayList<Admin>()));
	}
}
