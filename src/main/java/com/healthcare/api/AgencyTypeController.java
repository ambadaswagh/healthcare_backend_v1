package com.healthcare.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.api.auth.AbstractBasedAPI;
import com.healthcare.model.entity.AgencyType;
import com.healthcare.service.AgencyTypeService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Created by Jean Antunes on 22/05/17.
 */
@CrossOrigin
@RestController(value = "AgencyTypeRestAPI")
@RequestMapping(value = "/api/agency_type")
public class AgencyTypeController extends AbstractBasedAPI {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AgencyTypeService agencyTypeService;

	@ApiOperation(value = "post agency type", notes = "post agency type")
	@ApiParam(name = "agencyType", value = "post agency type", required = true)
	@PostMapping()
	public ResponseEntity<AgencyType> create(@RequestBody AgencyType agencyType) {
		agencyType = agencyTypeService.save(agencyType);
		return new ResponseEntity<>(agencyType, HttpStatus.OK);
	}

	@ApiOperation(value = "get agency type", notes = "get agency type")
	@ApiImplicitParam(name = "id", value = "get agency type", required = true, dataType = "Long" ,paramType = "path")
	@GetMapping("/{id}")
	public AgencyType read(@PathVariable Long id) {
		logger.info("id : " + id);
		return agencyTypeService.findById(id);
	}

	@ApiOperation(value = "update agency type", notes = "update agency type")
	@ApiParam(name = "agencyType", value = "update agency type", required = true)
	@PutMapping()
	public ResponseEntity<AgencyType> update(@RequestBody AgencyType agencyType) {
		agencyType = agencyTypeService.save(agencyType);
		return new ResponseEntity<>(agencyType, HttpStatus.OK);
	}

	@ApiOperation(value = "delete agency type", notes = "delete agency type")
	@ApiImplicitParam(name = "id", value = "delete agency type", required = true, dataType = "Long" ,paramType = "path")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		agencyTypeService.deleteById(id);
	}
}
