package com.healthcare.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.healthcare.model.entity.Report;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.ReportService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/api/report")
public class ReportController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ReportService reportService;

	@ApiOperation(value = "save report", notes = "save report")
	@ApiParam(name = "report", value = "report to save", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")

	@PostMapping()
	public ResponseEntity<Report> create(@RequestBody Report report) {
		report = reportService.save(report);
		return new ResponseEntity<Report>(report, HttpStatus.OK);
	}

	@ApiOperation(value = "get report by id", notes = "get report by id")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "report id", required = true, dataType = "Long", paramType = "path"),
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header") })

	@GetMapping("/{id}")
	public Report read(@PathVariable Long id) {
		logger.info("id : " + id);
		return reportService.findById(id);
	}

	@ApiOperation(value = "get all report", notes = "get all report")
	@GetMapping()
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")

	public ResponseEntity<Page<Report>> readAll(@RequestParam MultiValueMap<String, String> attributes) {
		MultiValueMapConverter<Report> converter = new MultiValueMapConverter<>(attributes, Report.class);
		return ResponseEntity.ok(reportService.findAll(converter.getData(), converter.getPageable()));
	}

	@ApiOperation(value = "update report", notes = "update report")
	@ApiParam(name = "report", value = "report to update", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")

	@PutMapping()
	public ResponseEntity<Report> update(@RequestBody Report report) {
		report = reportService.save(report);
		return new ResponseEntity<Report>(report, HttpStatus.OK);
	}

	@ApiOperation(value = "delete report", notes = "delete report")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "report id", required = true, dataType = "Long", paramType = "path"),
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header") })
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		reportService.deleteById(id); 
	}
}
