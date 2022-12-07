package com.healthcare.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import com.healthcare.model.entity.Employee;
import com.healthcare.model.entity.HealthInsuranceClaim;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.HealthInsuranceClaimService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Created by Hitesh on 07/14/17.
 */
@CrossOrigin
@RestController(value = "HealthInsuranceClaimRestAPI")
@RequestMapping(value = "/api/healthInsuranceClaim")
public class HealthInsuranceClaimController extends BaseController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private HealthInsuranceClaimService healthInsuranceClaimService;

	@ApiOperation(value = "save health insurance claim", notes = "save health insurance claim")
	@ApiParam(name = "healthInsuranceClaim", value = "health insurance claim to save", required = true)
	@PostMapping()
	public ResponseEntity<HealthInsuranceClaim> create(@RequestBody HealthInsuranceClaim healthInsuranceClaim) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String name = auth.getName();
		healthInsuranceClaim = healthInsuranceClaimService.save(healthInsuranceClaim);
		return new ResponseEntity<HealthInsuranceClaim>(healthInsuranceClaim, HttpStatus.OK);
	}

	@ApiOperation(value = "get health insurance claim by id", notes = "get health insurance claim by id")
	@ApiImplicitParam(name = "id", value = "health insurance claim id", required = true, dataType = "Long", paramType = "path")
	@GetMapping("/{id}")
	public HealthInsuranceClaim read(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		return healthInsuranceClaimService.findById(id);
	}

	@ApiOperation(value = "get all health insurance claim", notes = "get all health insurance claim")
	@GetMapping()
	public ResponseEntity<Page<HealthInsuranceClaim>> readAll(@RequestParam MultiValueMap<String, String> attributes) {
		MultiValueMapConverter<HealthInsuranceClaim> converter = new MultiValueMapConverter<>(attributes, HealthInsuranceClaim.class);
		Page<HealthInsuranceClaim> healthInsuranceClaimPage = healthInsuranceClaimService.findAll(converter.getData(), converter.getPageable());
		healthInsuranceClaimService.findTripStatusForBilling(healthInsuranceClaimPage.getContent());
		return ResponseEntity.ok(healthInsuranceClaimPage);
	}

	

	@ApiOperation(value = "get all health insurance claims for user", notes = "get all health insurance claims for user")
	@ApiImplicitParam(name = "userId", value = "user id", required = true, dataType = "Long", paramType = "path")
	@GetMapping("user/{userId}")
	public List<HealthInsuranceClaim> findByUserId(@PathVariable("userId") Long userId) {
		return healthInsuranceClaimService.findByUserId(userId);
	}
	
	@ApiOperation(value = "update health insurance claim", notes = "update health insurance claim")
	@ApiParam(name = "healthInsuranceClaim", value = "health insurance claim to update", required = true)
	@PutMapping()
	public ResponseEntity<HealthInsuranceClaim> update(@RequestBody HealthInsuranceClaim healthInsuranceClaim) {
		HealthInsuranceClaim db = healthInsuranceClaimService.findById(healthInsuranceClaim.getId());
		healthInsuranceClaim.setUser(db.getUser());
		healthInsuranceClaim = healthInsuranceClaimService.save(healthInsuranceClaim);
		return new ResponseEntity<HealthInsuranceClaim>(healthInsuranceClaim, HttpStatus.OK);
	}

	@ApiOperation(value = "delete health insurance claim", notes = "delete health insurance claim")
	@ApiImplicitParam(name = "id", value = "health insurance claim id", required = true, dataType = "Long", paramType = "path")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		healthInsuranceClaimService.deleteById(id);
	}
	
}
