package com.healthcare.api;

import static com.healthcare.api.common.HealthcareConstants.AUTHENTICATED_ADMIN;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@CrossOrigin
@RestController(value = "AdminRestAPI")
@RequestMapping(value = "/api/admin")
public class AdminController extends AbstractBasedAPI {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AdminService adminService;

	@Autowired
	CustomUserValidator customUserValidator;

	@Autowired
	public AdminController(AdminService adminService, CustomUserValidator customUserValidator) {
		this.adminService = adminService;
		this.customUserValidator = customUserValidator;
	}

	@ApiOperation(value = "save admin", notes = "save admin")
	@ApiParam(name = "admin", value = "admin to save", required = true)
	@PostMapping()
	public ResponseEntity<Admin> create(@RequestBody Admin admin, HttpServletRequest req) {
		DataBinder binder = new DataBinder(admin);
		binder.setValidator(new AdminValidator());
		binder.validate();
		BindingResult result = binder.getBindingResult();
		if (adminService.isAdminExisted(admin.getUsername())) {
			result.addError(new ObjectError("username", "Username existed"));
		}
		if (result.hasErrors()) {
			return new ResponseEntity(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		customUserValidator.validateAccess(req, admin);
		Admin saved = adminService.addNew(admin);
		return new ResponseEntity<>(saved, HttpStatus.OK);
	}

	@ApiOperation(value = "get admin by id", notes = "get admin by id")
	@ApiImplicitParam(name = "id", value = "admin id", required = true, dataType = "Long", paramType = "path")
	@GetMapping("/{id}")
	public Admin read(@PathVariable Long id) {
		logger.info("id : " + id);
		return adminService.findById(id);
	}

	@ApiOperation(value = "get all admin", notes = "get all admin")
	@GetMapping()
	public ResponseEntity<Page<Admin>> readAll(@RequestParam MultiValueMap<String, String> attributes) {
		MultiValueMapConverter<Admin> converter = new MultiValueMapConverter<>(attributes, Admin.class);
		return ResponseEntity.ok(adminService.findAll(converter.getData(), converter.getPageable()));
	}

	@ApiOperation(value = "update admin", notes = "update admin")
	@ApiParam(name = "admin", value = "admin to update", required = true)
	@PutMapping()
	public ResponseEntity update(@RequestBody Admin admin) {
		DataBinder binder = new DataBinder(admin);
		binder.setValidator(new AdminValidator());
		binder.validate();
		BindingResult result = binder.getBindingResult();
		if (result.hasErrors()) {
			return new ResponseEntity(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}

		Admin dbAdmin = adminService.findById(admin.getId());
		if (dbAdmin == null) {
			result.addError(new ObjectError("id", "Record not found"));
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}
		// copy all properties from admin to dbAdmin Object except password
		// property
		BeanUtils.copyProperties(admin, dbAdmin, "password");
		if (StringUtils.isNotBlank(admin.getPassword())) {
			String hashPassword = PasswordUtils.hashPassword(admin.getPassword());
			dbAdmin.setPassword(hashPassword);
		}
		return new ResponseEntity<Admin>(adminService.save(dbAdmin), HttpStatus.OK);
	}

	@ApiOperation(value = "delete admin", notes = "delete admin")
	@ApiImplicitParam(name = "id", value = "admin id", required = true, dataType = "Long", paramType = "path")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		adminService.deleteById(id);
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}
	
	@ApiOperation(value = "update theme by user id", notes = "update theme user id")
	@ApiParam(name = "employee", value = "update theme", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@PostMapping("/update_theme_admin/{themeseleted}")
	public boolean updateThemeUser(@PathVariable("themeseleted") String themeseleted,HttpServletRequest req) {
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
		long userId = admin.getId();
    	boolean updated = adminService.updateTheme(themeseleted,userId);
   		return updated;
	}
}
