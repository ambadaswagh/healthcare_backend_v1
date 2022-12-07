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

import com.healthcare.model.entity.CareGiver;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.CareGiverService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Created by Hitesh on 06/25/17.
 */
@CrossOrigin
@RestController(value = "CareGiverRestAPI")
@RequestMapping(value = "/api/caregiver")
public class CareGiverController extends BaseController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CareGiverService careGiverService;

	@ApiOperation(value = "save care giver", notes = "save care giver")
	@ApiParam(name = "caregiver", value = "care giver to save", required = true)
	@PostMapping()
	public ResponseEntity<CareGiver> create(@RequestBody CareGiver careGiver) {
		careGiver = careGiverService.save(careGiver);
		return new ResponseEntity<CareGiver>(careGiver, HttpStatus.OK);
	}

	@ApiOperation(value = "get care giver by id", notes = "get care giver by id")
	@ApiImplicitParam(name = "id", value = "care giver id", required = true, dataType = "Long", paramType = "path")
	@GetMapping("/{id}")
	public CareGiver read(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		return careGiverService.findById(id);
	}

	@ApiOperation(value = "get all care giver", notes = "get all care giver")
	@GetMapping()
	public ResponseEntity<Page<CareGiver>> readAll(@RequestParam MultiValueMap<String, String> attributes) {
		MultiValueMapConverter<CareGiver> converter = new MultiValueMapConverter<>(attributes, CareGiver.class);
		return ResponseEntity.ok(careGiverService.findAll(converter.getData(), converter.getPageable()));
	}


	@ApiOperation(value = "update care giver", notes = "update care giver")
	@ApiParam(name = "caregiver", value = "care giver to update", required = true)
	@PutMapping()
	public ResponseEntity<CareGiver> update(@RequestBody CareGiver CareGiver) {
		CareGiver = careGiverService.save(CareGiver);
		return new ResponseEntity<CareGiver>(CareGiver, HttpStatus.OK);
	}

	@ApiOperation(value = "delete care giver", notes = "delete care giver")
	@ApiImplicitParam(name = "id", value = "care giver id", required = true, dataType = "Long", paramType = "path")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		careGiverService.deleteById(id);
	}
}
