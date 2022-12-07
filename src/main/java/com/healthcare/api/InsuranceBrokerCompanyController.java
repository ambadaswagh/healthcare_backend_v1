package com.healthcare.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.model.entity.InsuranceBrokerCompany;
import com.healthcare.service.InsuranceBrokerCompanyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/api/insuranceBrokerCompany")
@Api(value = "/api/insuranceBrokerCompany")
public class InsuranceBrokerCompanyController {
	@Autowired
	private InsuranceBrokerCompanyService insuranceBrokerCompanyService;

	@ApiOperation(value = "save insurance_broker_company'", notes = "save a new insurance_broker_company'")
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@ApiParam(name = "insuranceBrokerCompany", value = "insuranceBrokerCompany to save", required = true)
	@PostMapping
	public ResponseEntity<InsuranceBrokerCompany> create(HttpServletRequest req,
			@RequestBody InsuranceBrokerCompany insuranceBrokerCompany) {
		insuranceBrokerCompany = insuranceBrokerCompanyService.save(insuranceBrokerCompany);

		return new ResponseEntity<InsuranceBrokerCompany>(insuranceBrokerCompany, HttpStatus.CREATED);
	}

	@ApiOperation(value = "get insurance_broker_company by Id", notes = "get insurance_broker_company info by id")
	@ApiImplicitParam(name = "id", value = "insurance_broker_company Id", required = true, dataType = "Long", paramType = "path")
	@GetMapping("/{id}")
	public InsuranceBrokerCompany read(@PathVariable("id") Long id) {
		return insuranceBrokerCompanyService.findById(id);
	}

	@ApiOperation(value = "update insurance_broker_company", notes = "update a insurance_broker_company")
	@ApiParam(name = "insurance_broker_company", value = "insurance_broker_company to update", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@PutMapping
	public ResponseEntity<InsuranceBrokerCompany> update(HttpServletRequest req,
			@RequestBody InsuranceBrokerCompany insuranceBrokerCompany) {
		insuranceBrokerCompany = insuranceBrokerCompanyService.save(insuranceBrokerCompany);

		return new ResponseEntity<InsuranceBrokerCompany>(insuranceBrokerCompany, HttpStatus.OK);
	}

	@ApiOperation(value = "delete insurance_broker_company", notes = "delete a insurance_broker_company")
	@ApiImplicitParam(name = "id", value = "insurance_broker_company Id", required = true, dataType = "Long", paramType = "path")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		insuranceBrokerCompanyService.deleteById(id);
	}

	@ApiOperation(value = "Get all insurance_broker_companies", notes = "Get all insurance_broker_companies")
	@GetMapping
	public ResponseEntity<List<InsuranceBrokerCompany>> findAll() {
		return ResponseEntity.ok(insuranceBrokerCompanyService.findAll());
	}

	@ApiOperation(value = "update insurance_broker_company", notes = "update a insurance_broker_company")
	@ApiParam(name = "insurance_broker_company", value = "insurance_broker_company to update", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@PutMapping("/{id}/disable")
	public void disable(@PathVariable("id") Long id) {
		insuranceBrokerCompanyService.disableById(id);
	}
}
