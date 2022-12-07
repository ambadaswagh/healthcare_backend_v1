package com.healthcare.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.healthcare.exception.UserException;
import com.healthcare.model.entity.*;
import com.healthcare.service.AdminAgencyCompanyOrganizationService;
import com.healthcare.service.AgencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.TrainingService;
import com.healthcare.util.DateUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletRequest;

import static com.healthcare.api.common.HealthcareConstants.AUTHENTICATED_ADMIN;
import static com.healthcare.api.common.HealthcareConstants.NOT_AUTHORIZED;
import static com.healthcare.api.common.HealthcareUtil.isEmpty;
import static com.healthcare.api.common.RoleUtil.isAgencyAdmin;
import static com.healthcare.api.common.RoleUtil.isCompanyAdmin;
import static com.healthcare.api.common.RoleUtil.isSuperAdmin;

/**
 * Created by Jean Antunes on 23/05/17.
 */
@CrossOrigin
@RestController(value = "TrainingRestAPI")
@RequestMapping(value = "/api/training")
public class TrainingController extends AbstractBasedAPI {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TrainingService trainingService;

	@Autowired
	private AdminAgencyCompanyOrganizationService adminAgencyCompanyOrganizationService;

	@Autowired
	private AgencyService agencyService;

	@ApiOperation(value = "save training", notes = "save training")
	@ApiParam(name = "training", value = "training to save", required = true)
	@PostMapping()
	public ResponseEntity<Training> create(@RequestBody Training training) {
		training = trainingService.save(training);
		return new ResponseEntity<Training>(training, HttpStatus.OK);
	}

	@ApiOperation(value = "get training by id", notes = "get training by id")
	@ApiImplicitParam(name = "id", value = "training id", required = true, dataType = "Long" ,paramType = "path")
	@GetMapping("/{id}")
	public Training read(HttpServletRequest req, @PathVariable Long id) {
		logger.info("id : " + id);
		Training training = trainingService.findById(id);
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
		if (!isEmpty(admin) && isAgencyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
			if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
				String timezone = DateUtils.get_timezone(adminAgencyCompanyOrganization.getAgency().getTimezone());
				training = get_object_with_local_times(training, timezone);
			}
		}
		return training;
	}

	@ApiOperation(value = "get all training", notes = "get all training")
	@GetMapping()
	public ResponseEntity<Page<Training>> readAll(HttpServletRequest req, @RequestParam MultiValueMap<String, String> attributes) {
		/*MultiValueMapConverter<Training> converter = new MultiValueMapConverter<>(attributes, Training.class);
		return ResponseEntity.ok(trainingService.findAll(converter.getData(), converter.getPageable()));*/
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if(isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}

		MultiValueMapConverter<Training> converter = new MultiValueMapConverter<>(attributes, Training.class);

		if(isSuperAdmin(admin)){
			return ResponseEntity.ok(trainingService.findAll(converter.getData(), converter.getPageable()));
		} else if (isCompanyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null) {
				// find all agencies by company id
				List<Agency> agencies = agencyService.findByCompany(adminAgencyCompanyOrganization.getCompany().getId());
				if (agencies != null && agencies.size() > 0) {
					List<Long> ids = agencies.stream().map(v -> v.getId()).collect(Collectors.toList());
					return ResponseEntity.ok(trainingService.findAllByAgencies(ids, converter.getPageable()));
				}
			}

		} else if (isAgencyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
				String timezone = DateUtils.get_timezone(adminAgencyCompanyOrganization.getAgency().getTimezone());
				
				Page<Training> trainings = get_local_time(trainingService.findAllByAgency(adminAgencyCompanyOrganization.getAgency().getId(), converter.getPageable()).getContent(), timezone);
				return ResponseEntity.ok(
						trainings
				);
			}
		}

		return ResponseEntity.ok(new PageImpl<Training>(new ArrayList<Training>()));
	}

	@ApiOperation(value = "update training", notes = "update training")
	@ApiParam(name = "training", value = "training to update", required = true)
	@PutMapping()
	public ResponseEntity<Training> update(@RequestBody Training training) {
		training = trainingService.save(training);
		return new ResponseEntity<Training>(training, HttpStatus.OK);
	}

	@ApiOperation(value = "delete training", notes = "delete training")
	@ApiImplicitParam(name = "id", value = "training id", required = true, dataType = "Long" ,paramType = "path")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		trainingService.deleteById(id);
	}

	@ApiOperation(value = "disable training", notes = "disable training")
	@ApiImplicitParam(name = "id", value = "training id", required = true, dataType = "Long" ,paramType = "path")
	@PutMapping("/{id}/disable")
	public void disable(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		trainingService.disableById(id);
	}
	
	
    @ApiOperation(value = "get all training of trainee", notes = "get all training of trainee")
    @ApiImplicitParam(name = "id", value = "trainee id(employee or user ids)", required = true, dataType = "String" ,paramType = "path")
    @GetMapping("/trainee/{id}")
	public ResponseEntity<List<Training>> readByTraineeId(@PathVariable("id") String id) {
		return ResponseEntity.ok(trainingService.findByTraineeId(id));
	}

	@ApiOperation(value = "get all training by agency id", notes = "get all training by agency id")
	@ApiImplicitParam(name = "id", value = "agency id", required = true, dataType = "String" ,paramType = "path")
	@GetMapping("/agency/{agencyId}")
	public ResponseEntity<Page<Training>> readAllByAgency(@RequestParam MultiValueMap<String, String> attributes, @PathVariable("agencyId") Long agencyId) {
		MultiValueMapConverter<Training> converter = new MultiValueMapConverter<>(attributes, Training.class);
		return ResponseEntity.ok(trainingService.findAllByAgency(agencyId, converter.getPageable()));
	}

	@ApiOperation(value = "get all training by company id", notes = "get all training by company id")
	@ApiImplicitParam(name = "id", value = "company id", required = true, dataType = "String" ,paramType = "path")
	@GetMapping("/company/{companyId}")
	public ResponseEntity<Page<Training>> readAllByCompany(@RequestParam MultiValueMap<String, String> attributes, @PathVariable("companyId") Long companyId) {
		MultiValueMapConverter<Training> converter = new MultiValueMapConverter<>(attributes, Training.class);

		List<Agency> agencies = agencyService.findByCompany(companyId);
		if (agencies != null && agencies.size() > 0) {
			List<Long> ids = agencies.stream().map(v -> v.getId()).collect(Collectors.toList());
			return ResponseEntity.ok(trainingService.findAllByAgencies(ids, converter.getPageable()));
		}
		return ResponseEntity.ok(new PageImpl<Training>(new ArrayList<Training>()));
	}
	
	public Page<Training> get_local_time(List<Training> trainings, String target_time_zone){
		for(Training training : trainings){
			training = get_object_with_local_times(training, target_time_zone);
		}
				
		return new PageImpl<>(trainings); 
	}
	
	public Training get_object_with_local_times(Training training, String target_time_zone){
		if(training.getStartTime() != null){
			System.err.println(training.getStartTime());
			training.setStartTime(DateUtils.get_local_time(training.getStartTime(), target_time_zone));
			System.err.println(training.getStartTime());
		}
		if(training.getEndTime() != null){
			System.err.println(training.getEndTime());
			training.setEndTime(DateUtils.get_local_time(training.getEndTime(), target_time_zone));
			System.err.println(training.getEndTime());
		}
		return training;
	}
}
