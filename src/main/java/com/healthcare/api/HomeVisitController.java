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

import com.healthcare.model.entity.HomeVisit;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.HomeVisitService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Created by Hitesh on 06/25/17.
 */
@CrossOrigin
@RestController(value = "HomeVisitRestAPI")
@RequestMapping(value = "/api/homeVisit")
public class HomeVisitController extends BaseController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private HomeVisitService homeVisitService;

	@ApiOperation(value = "save home visit", notes = "save home visit")
	@ApiParam(name = "homeVisit", value = "home visit to save", required = true)
	@PostMapping()
	public ResponseEntity<HomeVisit> create(@RequestBody HomeVisit homeVisit) {
		homeVisit = homeVisitService.save(homeVisit);
		return new ResponseEntity<HomeVisit>(homeVisit, HttpStatus.OK);
	}

	@ApiOperation(value = "get home visit by id", notes = "get home visit by id")
	@ApiImplicitParam(name = "id", value = "home visit id", required = true, dataType = "Long", paramType = "path")
	@GetMapping("/{id}")
	public HomeVisit read(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		return homeVisitService.findById(id);
	}

	@ApiOperation(value = "get all home visit", notes = "get all home visit")
	@GetMapping()
	public ResponseEntity<Page<HomeVisit>> readAll(@RequestParam MultiValueMap<String, String> attributes) {
		MultiValueMapConverter<HomeVisit> converter = new MultiValueMapConverter<>(attributes, HomeVisit.class);
		return ResponseEntity.ok(homeVisitService.findAll(converter.getData(), converter.getPageable()));
	}

	@ApiOperation(value = "update home visit", notes = "update home visit")
	@ApiParam(name = "homeVisit", value = "home visit to update", required = true)
	@PutMapping()
	public ResponseEntity<HomeVisit> update(@RequestBody HomeVisit homeVisit) {
		homeVisit = homeVisitService.save(homeVisit);
		return new ResponseEntity<HomeVisit>(homeVisit, HttpStatus.OK);
	}

	@ApiOperation(value = "delete home visit", notes = "delete home visit")
	@ApiImplicitParam(name = "id", value = "home visit id", required = true, dataType = "Long", paramType = "path")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		homeVisitService.deleteById(id);
	}
}
