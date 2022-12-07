package com.healthcare.api;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.healthcare.model.entity.Activity;
import com.healthcare.model.entity.Medication;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.MedicationService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Medication controller
 */
@RestController
@RequestMapping("/api/medication")
public class MedicationController extends BaseController {

	private MedicationService medicationService;

	@Autowired
	public MedicationController(MedicationService medicationService) {
		this.medicationService = medicationService;
	}

	@ApiOperation(value = "Create new medication", notes = "Create new medication")
	@ApiParam(name = "medication", value = "Medication to create", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@PostMapping
	public ResponseEntity create(@RequestBody Medication medication) {
		return ResponseEntity.ok(medicationService.save(medication).getId());
	}

	@ApiOperation(value = "Get medication by id", notes = "Get medication info by id")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "medication Id", required = true, dataType = "Long", paramType = "path"),
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header") })
	@GetMapping("/{id}")
	public ResponseEntity get(@PathVariable("id") Long id) {
		if (id == null) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(medicationService.findById(id));
	}

	@ApiOperation(value = "Get all medication", notes = "Get all medication")
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@GetMapping()
	public ResponseEntity<Page<Medication>> getAll(@RequestParam MultiValueMap<String, String> attributes) {
		MultiValueMapConverter<Medication> converter = new MultiValueMapConverter<>(attributes, Medication.class);
		return ResponseEntity.ok(medicationService.findAll(converter.getData(), converter.getPageable()));
	}

	@ApiOperation(value = "Update medication", notes = "Update an medication")
	@ApiParam(name = "medication", value = "medication to update", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@PutMapping
	public void save(@RequestBody Medication medication) {
		medicationService.update(medication);
	}

	@ApiOperation(value = "Delete medication", notes = "Delete an medication")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "medication Id", required = true, dataType = "Long", paramType = "path"),
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header") })
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id, HttpServletResponse response) {
		if (id == null) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return;
		}
		medicationService.deleteById(id);
	}

}
