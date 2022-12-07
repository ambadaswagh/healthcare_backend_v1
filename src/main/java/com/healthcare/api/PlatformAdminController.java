package com.healthcare.api;

import javax.servlet.http.HttpServletRequest;

import com.healthcare.model.entity.AdminAgencyCompanyOrganization;
import com.healthcare.service.AdminAgencyCompanyOrganizationService;
import com.healthcare.validator.AdminAgencyCompanyOrganizationValidator;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
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
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.AdminService;
import com.healthcare.service.impl.CustomUserValidator;
import com.healthcare.util.PasswordUtils;
import com.healthcare.validator.AdminValidator;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@CrossOrigin
@RestController(value = "PlatFormAdminRestAPI")
@RequestMapping(value = "/api/platformAdmin")
public class PlatformAdminController extends AbstractBasedAPI {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("platformAdminServiceImpl")
	private AdminService adminService;

	@Autowired
	AdminAgencyCompanyOrganizationService adminAgencyCompanyOrganizationService;

	@Autowired
	CustomUserValidator customUserValidator;

	@ApiOperation(value = "save platform admin", notes = "save platform admin")
	@ApiParam(name = "admin", value = "platform admin to save", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@PostMapping()
	public ResponseEntity<AdminAgencyCompanyOrganization> create(@RequestBody AdminAgencyCompanyOrganization entity, HttpServletRequest req) {

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

	@ApiOperation(value = "get platform admin by id", notes = "get platform admin by id")
	@ApiImplicitParam(name = "id", value = "platform admin id", required = true, dataType = "Long", paramType = "path")
	@GetMapping("/{id}")
	public Admin read(@PathVariable Long id) {
		logger.info("id : " + id);
		return adminService.findById(id);
	}

	@ApiOperation(value = "get all platform admin", notes = "get all platform admin")
	@GetMapping()
	public ResponseEntity<Page<Admin>> readAll(@RequestParam MultiValueMap<String, String> attributes) {
		MultiValueMapConverter<Admin> converter = new MultiValueMapConverter<>(attributes, Admin.class);
		return ResponseEntity.ok(adminService.findAll(converter.getData(), converter.getPageable()));
	}

	@ApiOperation(value = "update platform admin", notes = "update platform admin")
	@ApiParam(name = "admin", value = "platform admin to update", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@PutMapping()
	public ResponseEntity update(@RequestBody AdminAgencyCompanyOrganization entity, HttpServletRequest req) {

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

	@ApiOperation(value = "delete platform admin", notes = "delete platform admin")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "platform admin id", required = true, dataType = "Long", paramType = "path"),
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header") })
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id, HttpServletRequest req) {
		customUserValidator.validateAccess(req, id);

		logger.info("id : " + id);
		adminService.deleteById(id);
	}

	@ApiOperation(value = "disable platform admin", notes = "disable platform admin")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "platform admin id", required = true, dataType = "Long", paramType = "path"),
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header") })
	@PutMapping("/{id}/disable")
	public void disable(@PathVariable("id") Long id, HttpServletRequest req) {
		customUserValidator.validateAccess(req, id);
		adminService.disableById(id);
	}
}
