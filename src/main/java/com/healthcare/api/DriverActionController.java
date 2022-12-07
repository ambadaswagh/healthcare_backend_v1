package com.healthcare.api;

import com.healthcare.api.common.HealthcareUtil;
import com.healthcare.dto.ReminderDTO;
import com.healthcare.exception.UserException;
import com.healthcare.model.entity.*;
import com.healthcare.service.AdminAgencyCompanyOrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;

import com.healthcare.api.auth.AbstractBasedAPI;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.DriverService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.healthcare.api.common.HealthcareConstants.AUTHENTICATED_ADMIN;
import static com.healthcare.api.common.HealthcareConstants.NOT_AUTHORIZED;
import static com.healthcare.api.common.HealthcareUtil.isEmpty;
import static com.healthcare.api.common.RoleUtil.isAgencyAdmin;
import static com.healthcare.api.common.RoleUtil.isCompanyAdmin;
import static com.healthcare.api.common.RoleUtil.isSuperAdmin;

/**
 * Created by Jean Antunes on 11/05/17.
 */
@CrossOrigin
@RestController(value = "DriverRestAPI")
@RequestMapping(value = "/api/driver")
public class DriverActionController extends AbstractBasedAPI {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DriverService driverService;

	@Autowired
	private AdminAgencyCompanyOrganizationService adminAgencyCompanyOrganizationService;

	
	@ApiOperation(value = "get all driver", notes = "get all driver")
	@GetMapping()
	public ResponseEntity<Page<Driver>> readAll(HttpServletRequest req, @RequestParam MultiValueMap<String, String> attributes) {
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if(isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}

		MultiValueMapConverter<Driver> converter = new MultiValueMapConverter<>(attributes, Driver.class);

		if(isSuperAdmin(admin)){
			Page<Driver> driverPage = driverService.findAll(converter.getData(), converter.getPageable());
			return ResponseEntity.ok(driverPage);
		} else if (isCompanyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null) {
				return ResponseEntity.ok(driverService.findByCompanyId(adminAgencyCompanyOrganization.getCompany().getId(), converter.getPageable()));
			}

		} else if (isAgencyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null) {
				return ResponseEntity.ok(
						driverService.findByCompanyIdAndAgency(
								adminAgencyCompanyOrganization.getCompany().getId(),
								adminAgencyCompanyOrganization.getAgency().getId(),
								converter.getPageable()));
			}
		}

		return ResponseEntity.ok(new PageImpl<Driver>(new ArrayList<Driver>()));
	}

	@ApiOperation(value = "get all driver", notes = "get all driver")
	@GetMapping("/getAllDriver")
	public ResponseEntity<Page<Driver>> getAllDriver(HttpServletRequest req, @RequestParam MultiValueMap<String, String> attributes) {
		//return ResponseEntity.ok(driverService.findAll());
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if(isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}

		MultiValueMapConverter<Driver> converter = new MultiValueMapConverter<>(attributes, Driver.class);

		if(isSuperAdmin(admin)){
			Page<Driver> driverPage = driverService.findAll(converter.getData(), converter.getPageable());
			return ResponseEntity.ok(driverPage);
		} else if (isCompanyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null) {
				return ResponseEntity.ok(driverService.findByCompanyId(adminAgencyCompanyOrganization.getCompany().getId(), converter.getPageable()));
			}

		} else if (isAgencyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null) {
				return ResponseEntity.ok(
						driverService.findByCompanyIdAndAgency(
								adminAgencyCompanyOrganization.getCompany().getId(),
								adminAgencyCompanyOrganization.getAgency().getId(),
								converter.getPageable()));
			}
		}

		return ResponseEntity.ok(new PageImpl<Driver>(new ArrayList<Driver>()));
	}
	
	

	@ApiOperation(value = "save driver", notes = "save driver")
	@ApiParam(name = "driver", value = "driver to save", required = true)
	@PostMapping()
	public ResponseEntity<Driver> create(@RequestBody Driver driver) {
		DataBinder binder = new DataBinder(driver);
		/*binder.setValidator(saveDriverValidator);
		binder.validate();*/
		BindingResult result = binder.getBindingResult();
		if (result.hasErrors()) {
			return new ResponseEntity(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		driver = driverService.save(driver);
		return new ResponseEntity<Driver>(driver, HttpStatus.OK);
	}
	
	@ApiOperation(value = "get driver by id", notes = "get driver by id")
	@ApiImplicitParam(name = "id", value = "driver id", required = true, dataType = "Long", paramType = "path")
	@GetMapping("/{id}")
	public Driver read(@PathVariable Long id) {
		System.out.println("the value of id is:"+id);
		logger.info("id : " + id);
		return driverService.findById(id);
	}
	
	@ApiOperation(value = "update driver", notes = "update driver")
	@ApiParam(name = "driver", value = "driver to update", required = true)
	@PutMapping()
	public ResponseEntity<Driver> update(@RequestBody Driver driver) {
		DataBinder binder = new DataBinder(driver);
		/*binder.setValidator(updateEmployeeValidator);
		binder.validate();*/
		BindingResult result = binder.getBindingResult();
		if (result.hasErrors()) {
			return new ResponseEntity(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		driver = driverService.save(driver);
		return new ResponseEntity<Driver>(driver, HttpStatus.OK);
	}


	@ApiOperation(value = "get driver by company id", notes = "get driver by company id")
	@ApiImplicitParam(name = "id", value = "company id", required = true, dataType = "Long", paramType = "path")
	@GetMapping("/company/{companyId}")
	public ResponseEntity<Page<Driver>> getAllByCompany(@PathVariable Long companyId, @RequestParam MultiValueMap<String, String> attributes) {

		MultiValueMapConverter<Driver> converter = new MultiValueMapConverter<>(attributes, Driver.class);

		return ResponseEntity.ok(driverService.findByCompanyId(companyId, converter.getPageable()));
	}

	@ApiOperation(value = "get driver by company id and agency", notes = "get driver by company id agency")
	@ApiImplicitParam(name = "id", value = "company id, angency id", required = true, dataType = "Long", paramType = "path")
	@GetMapping("/company/{companyId}/agency/{agencyId}")
	public ResponseEntity<Page<Driver>> getAllByCompanyAgency(@PathVariable Long companyId, @PathVariable Long agencyId, @RequestParam MultiValueMap<String, String> attributes) {

		MultiValueMapConverter<Driver> converter = new MultiValueMapConverter<>(attributes, Driver.class);

		return ResponseEntity.ok(driverService.findByCompanyIdAndAgency(companyId, agencyId, converter.getPageable()));
	}

	@ApiOperation(value = "get all driver", notes = "get all driver")
	@GetMapping("/typeahead")
	public List<Driver> findAllForTypeahead(HttpServletRequest req, @RequestParam("search") String search) {
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if(isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}

		if(isSuperAdmin(admin)){
			return driverService.searchByFirstName(search);
		} else if (isCompanyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null) {
				return driverService.searchByFirstNameByCompany(adminAgencyCompanyOrganization.getCompany().getId(), search);
			}

		} else if (isAgencyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null) {
				return driverService.searchByFirstNameByCompanyAgency(adminAgencyCompanyOrganization.getCompany().getId(), adminAgencyCompanyOrganization.getAgency().getId(), search);
			}
		}

		return new ArrayList<Driver>();
	}

	@ApiOperation(value = "List Driver have driver licences end next few days", notes = "Default will be 10 days")
	@ApiImplicitParam(name = "days", value = "how many next day. Default value is 10", dataType = "Integer" ,paramType = "path")
	@RequestMapping(value = "/status/driver-license-expire/{days}", method = RequestMethod.GET)
	public ResponseEntity<?> getDriverLicenseEndFewDays(HttpServletRequest req, @PathVariable Integer days, @RequestParam MultiValueMap<String, String> attributes) {

		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
		if (HealthcareUtil.isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}

		Integer realDay = days == null ? 0 : days;
		if(realDay < 0){
			return ResponseEntity.badRequest().body("days must be larger than 0");
		}

		ReminderDTO dto = new ReminderDTO().fromMap(attributes);

		return ResponseEntity.ok(driverService.getDriverLicenseEndNextDays(realDay, admin,
				new PageRequest(dto.getPage(), dto.getSize()), dto.getCompanyId(), dto.getAgencyId()));
	}

	@ApiOperation(value = "List Driver have driver licences end next few days", notes = "Default will be 10 days")
	@ApiImplicitParam(name = "days", value = "how many next day. Default value is 10", dataType = "Integer" ,paramType = "path")
	@RequestMapping(value = "/status/driver-tlc-fhv-license-expire/{days}", method = RequestMethod.GET)
	public ResponseEntity<?> getDriverTlcFhvLicenseEndFewDays(HttpServletRequest req, @PathVariable Integer days, @RequestParam MultiValueMap<String, String> attributes) {

		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
		if (HealthcareUtil.isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}

		Integer realDay = days == null ? 0 : days;
		if(realDay < 0){
			return ResponseEntity.badRequest().body("days must be larger than 0");
		}

		ReminderDTO dto = new ReminderDTO().fromMap(attributes);
		return ResponseEntity.ok(driverService.getDriverTlcFhvLicenseEndNextDays(realDay, admin,
				new PageRequest(dto.getPage(), dto.getSize()), dto.getCompanyId(), dto.getAgencyId()));
	}

	@ApiOperation(value = "List Driver have driver background check end next few days", notes = "Default will be 10 days")
	@ApiImplicitParam(name = "days", value = "how many next day. Default value is 10", dataType = "Integer" ,paramType = "path")
	@RequestMapping(value = "/status/background-check-expire/{days}", method = RequestMethod.GET)
	public ResponseEntity<?> getDriverBackgroundCheckEndFewDays(HttpServletRequest req, @PathVariable Integer days, @RequestParam MultiValueMap<String, String> attributes) {

		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
		if (HealthcareUtil.isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}

		Integer realDay = days == null ? 0 : days;
		if(realDay < 0){
			return ResponseEntity.badRequest().body("days must be larger than 0");
		}

		ReminderDTO dto = new ReminderDTO().fromMap(attributes);
		return ResponseEntity.ok(driverService.getDriverBackgroundCheckEndNextDays(realDay, admin,
				new PageRequest(dto.getPage(), dto.getSize()), dto.getCompanyId(), dto.getAgencyId()));
	}

	@ApiOperation(value = "List Driver have driver driving record end next few days", notes = "Default will be 10 days")
	@ApiImplicitParam(name = "days", value = "how many next day. Default value is 10", dataType = "Integer" ,paramType = "path")
	@RequestMapping(value = "/status/driving-record-expire/{days}", method = RequestMethod.GET)
	public ResponseEntity<?> getDriverDrivingRecordEndFewDays(HttpServletRequest req, @PathVariable Integer days, @RequestParam MultiValueMap<String, String> attributes) {

		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
		if (HealthcareUtil.isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}

		Integer realDay = days == null ? 0 : days;
		if(realDay < 0){
			return ResponseEntity.badRequest().body("days must be larger than 0");
		}

		ReminderDTO dto = new ReminderDTO().fromMap(attributes);
		return ResponseEntity.ok(driverService.getDriverDrivingRecordEndNextDays(realDay, admin,
				new PageRequest(dto.getPage(), dto.getSize()), dto.getCompanyId(), dto.getAgencyId()));
	}

	@ApiOperation(value = "List Driver have driver drug screen end next few days", notes = "Default will be 10 days")
	@ApiImplicitParam(name = "days", value = "how many next day. Default value is 10", dataType = "Integer" ,paramType = "path")
	@RequestMapping(value = "/status/drug-screen-expire/{days}", method = RequestMethod.GET)
	public ResponseEntity<?> getDriverDrugScreenEndFewDays(HttpServletRequest req, @PathVariable Integer days, @RequestParam MultiValueMap<String, String> attributes) {

		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
		if (HealthcareUtil.isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}

		Integer realDay = days == null ? 0 : days;
		if(realDay < 0){
			return ResponseEntity.badRequest().body("days must be larger than 0");
		}

		ReminderDTO dto = new ReminderDTO().fromMap(attributes);
		return ResponseEntity.ok(driverService.getDriverDrugScreenEndNextDays(realDay, admin,
				new PageRequest(dto.getPage(), dto.getSize()), dto.getCompanyId(), dto.getAgencyId()));
	}
	
}
