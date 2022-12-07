package com.healthcare.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.healthcare.exception.UserException;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.AdminAgencyCompanyOrganization;
import com.healthcare.model.entity.Agency;
import com.healthcare.model.entity.Visit;
import com.healthcare.repository.ActivityRepository;
import com.healthcare.service.AdminAgencyCompanyOrganizationService;
import com.healthcare.service.AgencyService;
import com.healthcare.util.DateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.ActivityService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.healthcare.api.common.HealthcareConstants.AUTHENTICATED_ADMIN;
import static com.healthcare.api.common.HealthcareConstants.NOT_AUTHORIZED;
import static com.healthcare.api.common.HealthcareUtil.isEmpty;
import static com.healthcare.api.common.RoleUtil.isAgencyAdmin;
import static com.healthcare.api.common.RoleUtil.isCompanyAdmin;
import static com.healthcare.api.common.RoleUtil.isSuperAdmin;

/**
 * Activity controller
 */
@RestController
@RequestMapping("/api/activity")
public class ActivityController extends BaseController {

	private ActivityService activityService;

	@Autowired
	private AgencyService agencyService;

	@Autowired
	private AdminAgencyCompanyOrganizationService adminAgencyCompanyOrganizationService;

	@Autowired
	public ActivityController(ActivityService activityService) {
		this.activityService = activityService;
	}

	@Autowired
	private ActivityRepository activityRepository;

	@ApiOperation(value = "Create activity", notes = "Create an activity")
	@ApiParam(name = "activity", value = "activity to create", required = true)
	@PostMapping
	public ResponseEntity create(HttpServletRequest req, @RequestBody Activity activity) {
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
		if (!isEmpty(admin) && isAgencyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
			if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
				String timezone = DateUtils.get_timezone(adminAgencyCompanyOrganization.getAgency().getTimezone());
				activity = get_object_with_local_times(activity, timezone);
			}
		}
		return ResponseEntity.ok(activityService.save(activity).getId());
	}

	@ApiOperation(value = "Get activity by Id", notes = "Get activity info by activityId")
	@ApiImplicitParam(name = "id", value = "activity Id", required = true, dataType = "Long", paramType = "path")
	@GetMapping("/{id}")
	public ResponseEntity get(HttpServletRequest req, @PathVariable("id") String id) {
		Long activityId = parseId(id);

		if (activityId == null) {
			return ResponseEntity.badRequest().build();
		}
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
		Activity activity = activityService.findById(activityId);
		if (!isEmpty(admin) && isAgencyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
			if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
				String timezone = DateUtils.get_timezone(adminAgencyCompanyOrganization.getAgency().getTimezone());
				activity = get_object_with_local_times(activity, timezone);
			}
		}
		return ResponseEntity.ok(activity);
	}

	@ApiOperation(value = "Get all activities", notes = "Get all activities")
	@GetMapping()
	public ResponseEntity<Page<Activity>> getAll(HttpServletRequest req, @RequestParam MultiValueMap<String, String> attributes) {
		/*MultiValueMapConverter<Activity> converter = new MultiValueMapConverter<>(attributes, Activity.class);
		return ResponseEntity.ok(activityService.findAll(converter.getData(), converter.getPageable()));*/
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if(isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}

		MultiValueMapConverter<Activity> converter = new MultiValueMapConverter<>(attributes, Activity.class);

		if(isSuperAdmin(admin)){
			return ResponseEntity.ok(activityService.findAll(converter.getData(), converter.getPageable()));
		} else if (isCompanyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null) {
				// find all agencies by company id
				List<Agency> agencies = agencyService.findByCompany(adminAgencyCompanyOrganization.getCompany().getId());
				if (agencies != null && agencies.size() > 0) {
					List<Long> ids = agencies.stream().map(v -> v.getId()).collect(Collectors.toList());
					return ResponseEntity.ok(activityService.findAllByAgencies(ids, converter.getPageable()));
				}
			}

		} else if (isAgencyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
				String timezone = DateUtils.get_timezone(adminAgencyCompanyOrganization.getAgency().getTimezone());
				Page<Activity> activities = get_local_time(activityService.findAllByAgency(adminAgencyCompanyOrganization.getAgency().getId(), converter.getPageable()).getContent(), timezone);
				return ResponseEntity.ok(
						activities
				);
			}
		}

		return ResponseEntity.ok(new PageImpl<Activity>(new ArrayList<Activity>()));
	}

	@ApiOperation(value = "Get all activities", notes = "Get all activities")
	@GetMapping("/getAllActivityList")
	public ResponseEntity<List<Activity>> getAllActivityList(HttpServletRequest req) {
		/*MultiValueMapConverter<Activity> converter = new MultiValueMapConverter<>(attributes, Activity.class);
		return ResponseEntity.ok(activityService.findAll(converter.getData(), converter.getPageable()));*/
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if(isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}

		if(isSuperAdmin(admin)){
			return ResponseEntity.ok(activityRepository.findAll());
		} else if (isCompanyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null) {
				// find all agencies by company id
				List<Agency> agencies = agencyService.findByCompany(adminAgencyCompanyOrganization.getCompany().getId());
				if (agencies != null && agencies.size() > 0) {
					List<Long> ids = agencies.stream().map(v -> v.getId()).collect(Collectors.toList());
					return ResponseEntity.ok(activityService.findAllByAgenciesList(ids));
				}
			}

		} else if (isAgencyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
				String timezone = DateUtils.get_timezone(adminAgencyCompanyOrganization.getAgency().getTimezone());
				List<Activity> activities = get_local_time_list(activityService.findAllByAgencyList(adminAgencyCompanyOrganization.getAgency().getId()), timezone);
				return ResponseEntity.ok(
						activities
				);
			}
		}

		return ResponseEntity.ok((new ArrayList<Activity>()));
	}

	@ApiOperation(value = "Update activity", notes = "Update an activity")
	@ApiParam(name = "activity", value = "activity to update", required = true)
	@PutMapping
	public void save(HttpServletRequest req, @RequestBody Activity activity) {
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
		if (!isEmpty(admin) && isAgencyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
			if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
				String timezone = DateUtils.get_timezone(adminAgencyCompanyOrganization.getAgency().getTimezone());
				activity = get_object_with_local_times(activity, timezone);
			}
		}
		activityService.save(activity);
	}

	@ApiOperation(value = "Delete activity", notes = "Delete an activity")
	@ApiImplicitParam(name = "id", value = "activity Id", required = true, dataType = "Long", paramType = "path")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") String id, HttpServletResponse response) {
		Long activityId = parseId(id);

		if (activityId == null) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return;
		}

		activityService.deleteById(activityId);
	}

	@ApiOperation(value = "Disable activity", notes = "Disable an activity")
	@ApiImplicitParam(name = "id", value = "activity Id", required = true, dataType = "Long", paramType = "path")
	@PutMapping("/{id}/disable")
	public void disable(@PathVariable("id") String id, HttpServletResponse response) {
		Long activityId = parseId(id);
		if (activityId == null) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return;
		}
		activityService.disableById(activityId);
	}

	@ApiOperation(value = "get all acitivity by agency id", notes = "get all acitivity by agency id")
	@ApiImplicitParam(name = "id", value = "agency id", required = true, dataType = "String" ,paramType = "path")
	@GetMapping("/agency/{agencyId}")
	public ResponseEntity<Page<Activity>> readAllByAgency(@RequestParam MultiValueMap<String, String> attributes, @PathVariable("agencyId") Long agencyId) {
		MultiValueMapConverter<Activity> converter = new MultiValueMapConverter<>(attributes, Activity.class);
		return ResponseEntity.ok(activityService.findAllByAgency(agencyId, converter.getPageable()));
	}

	@ApiOperation(value = "get all acitivity by company id", notes = "get all acitivity by company id")
	@ApiImplicitParam(name = "id", value = "company id", required = true, dataType = "String" ,paramType = "path")
	@GetMapping("/company/{companyId}")
	public ResponseEntity<Page<Activity>> readAllByCompany(@RequestParam MultiValueMap<String, String> attributes, @PathVariable("companyId") Long companyId) {
		MultiValueMapConverter<Activity> converter = new MultiValueMapConverter<>(attributes, Activity.class);

		List<Agency> agencies = agencyService.findByCompany(companyId);
		if (agencies != null && agencies.size() > 0) {
			List<Long> ids = agencies.stream().map(v -> v.getId()).collect(Collectors.toList());
			return ResponseEntity.ok(activityService.findAllByAgencies(ids, converter.getPageable()));
		}
		return ResponseEntity.ok(new PageImpl<Activity>(new ArrayList<Activity>()));
	}
	
	public Page<Activity> get_local_time(List<Activity> activities, String target_time_zone){
		for(Activity activity : activities){
			activity = get_object_with_local_times(activity, target_time_zone);
		}

		return new PageImpl<>(activities);
	}

	public List<Activity> get_local_time_list(List<Activity> activities, String target_time_zone){
		for(Activity activity : activities){
			activity = get_object_with_local_times(activity, target_time_zone);
		}

		return new ArrayList<>(activities);
	}
	
	public Activity get_object_with_local_times(Activity activity, String target_time_zone){
		if(activity.getTimeStart() != null){
			activity.setTimeStart(DateUtils.get_local_time_string(activity.getTimeStart(), target_time_zone));
		}
		if(activity.getTimeEnd() != null){
			activity.setTimeEnd(DateUtils.get_local_time_string(activity.getTimeEnd(), target_time_zone));
		}
		return activity;
	}
}
