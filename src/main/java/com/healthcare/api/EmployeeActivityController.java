package com.healthcare.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
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
import com.healthcare.model.entity.EmployeeActivity;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.EmployeeActivityService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Created by jean on 14/06/17.
 */
@CrossOrigin
@RestController(value = "EmployeeActivityRestAPI")
@RequestMapping(value = "/api/	")
public class EmployeeActivityController extends AbstractBasedAPI {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EmployeeActivityService employeeActivityService;

	@ApiOperation(value = "save employee activity", notes = "save employee activity")
	@ApiParam(name = "employee activity", value = "employee activity to save", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@PostMapping()
	public ResponseEntity<EmployeeActivity> create(@RequestBody EmployeeActivity employeeActivity) {
		employeeActivity = employeeActivityService.save(employeeActivity);
		return new ResponseEntity<EmployeeActivity>(employeeActivity, HttpStatus.OK);
	}

	@ApiOperation(value = "get employee activity by id", notes = "get employee activity by id")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "employee activity id", required = true, dataType = "Long", paramType = "path"),
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header") })
	@GetMapping("/{id}")
	public EmployeeActivity read(@PathVariable("id") Long id) {
		return employeeActivityService.findById(id);
	}

	@ApiOperation(value = "get all employee activity", notes = "get all employee activity")
	@GetMapping()
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	public ResponseEntity<Page<EmployeeActivity>> readAll(@RequestParam MultiValueMap<String, String> attributes) {
		MultiValueMapConverter<EmployeeActivity> converter = new MultiValueMapConverter<>(attributes,
				EmployeeActivity.class);
		return ResponseEntity.ok(employeeActivityService.findAll(converter.getData(), converter.getPageable()));
	}

	@ApiOperation(value = "update employee activity", notes = "update employee activity")
	@ApiParam(name = "employee activity", value = "employee activity to update", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@PutMapping()
	public ResponseEntity<EmployeeActivity> update(@RequestBody EmployeeActivity employeeActivity) {
		employeeActivity = employeeActivityService.save(employeeActivity);
		return new ResponseEntity<EmployeeActivity>(employeeActivity, HttpStatus.OK);
	}

	@ApiOperation(value = "delete employee activity", notes = "delete employee activity")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "employee activity id", required = true, dataType = "Long", paramType = "path"),
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header") })

	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		employeeActivityService.deleteById(id);
	}

}
