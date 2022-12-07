package com.healthcare.api;

import com.healthcare.exception.UserException;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.AdminAgencyCompanyOrganization;
import com.healthcare.model.entity.Agency;
import com.healthcare.repository.OrganizationRepository;
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
import com.healthcare.model.entity.Organization;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.OrganizationService;
import com.healthcare.util.DateUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.healthcare.api.common.HealthcareConstants.AUTHENTICATED_ADMIN;
import static com.healthcare.api.common.HealthcareConstants.NOT_AUTHORIZED;
import static com.healthcare.api.common.HealthcareUtil.isEmpty;
import static com.healthcare.api.common.RoleUtil.isAgencyAdmin;
import static com.healthcare.api.common.RoleUtil.isCompanyAdmin;
import static com.healthcare.api.common.RoleUtil.isSuperAdmin;

@CrossOrigin
@RestController(value = "Organization API")
@RequestMapping(value = "/api/organization")
public class OrganizationController extends AbstractBasedAPI {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private AdminAgencyCompanyOrganizationService adminAgencyCompanyOrganizationService;

	@Autowired
	private AgencyService agencyService;

	@Autowired
	private OrganizationRepository organizationRepository;

	@ApiOperation(value = "get all organization", notes = "get all organization")
	@GetMapping()
	public ResponseEntity<Page<Organization>> readAll(HttpServletRequest req, @RequestParam MultiValueMap<String, String> attributes) {
		/*MultiValueMapConverter<Organization> converter = new MultiValueMapConverter<>(attributes, Organization.class);
		return ResponseEntity.ok(organizationService.findAll(converter.getData(), converter.getPageable()));*/

		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if(isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}

		MultiValueMapConverter<Organization> converter = new MultiValueMapConverter<>(attributes, Organization.class);

		if(isSuperAdmin(admin)){
			return ResponseEntity.ok(organizationService.findAll(converter.getData(), converter.getPageable()));
		} else if (isCompanyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null) {
				// find all agencies by company id
				List<Agency> agencies = agencyService.findByCompany(adminAgencyCompanyOrganization.getCompany().getId());
				if (agencies != null && agencies.size() > 0) {
					List<Long> ids = agencies.stream().map(v -> v.getId()).collect(Collectors.toList());
					return ResponseEntity.ok(organizationService.findAllByAgencies(ids, converter.getPageable()));
				}
			}

		} else if (isAgencyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
				String timezone = DateUtils.get_timezone(adminAgencyCompanyOrganization.getAgency().getTimezone());
				return ResponseEntity.ok(
						get_local_time(organizationService.findAllByAgency(adminAgencyCompanyOrganization.getAgency().getId(), converter.getPageable()).getContent(), timezone)
						);
			}
		}

		return ResponseEntity.ok(new PageImpl<Organization>(new ArrayList<Organization>()));
	}

	@ApiOperation(value = "get all organization", notes = "get all organization")
	@GetMapping("/getAllOrganizationList")
	public ResponseEntity<List<Organization>> getAllOrganizationList(HttpServletRequest req) {
		/*MultiValueMapConverter<Organization> converter = new MultiValueMapConverter<>(attributes, Organization.class);
		return ResponseEntity.ok(organizationService.findAll(converter.getData(), converter.getPageable()));*/

		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if(isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}

		if(isSuperAdmin(admin)){
			return ResponseEntity.ok(organizationRepository.findAll());
		} else if (isCompanyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null) {
				// find all agencies by company id
				List<Agency> agencies = agencyService.findByCompany(adminAgencyCompanyOrganization.getCompany().getId());
				if (agencies != null && agencies.size() > 0) {
					List<Long> ids = agencies.stream().map(v -> v.getId()).collect(Collectors.toList());
					return ResponseEntity.ok(organizationService.findAllByAgenciesList(ids));
				}
			}

		} else if (isAgencyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
				String timezone = DateUtils.get_timezone(adminAgencyCompanyOrganization.getAgency().getTimezone());
				return ResponseEntity.ok(
						get_local_time_list(organizationService.findAllByAgencyList(adminAgencyCompanyOrganization.getAgency().getId()), timezone)
				);
			}
		}

		return ResponseEntity.ok((new ArrayList<Organization>()));
	}
	
	@ApiOperation(value="save organization", notes="Save Organization")
	@ApiParam(name = "organization", value = "organization to save", required = true)
	@PostMapping()
	public ResponseEntity<Organization> create(HttpServletRequest req, @RequestBody Organization organization){
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
		if (!isEmpty(admin) && isAgencyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
			if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
				String timezone = DateUtils.get_timezone(adminAgencyCompanyOrganization.getAgency().getTimezone());
				organization = get_object_with_local_times(organization, timezone);
			}
		}
		
		
		organization=organizationService.save(organization);
		return new ResponseEntity<Organization>(organization, HttpStatus.OK);
	}
	
	@ApiOperation(value = "get organization by id", notes = "get organization by id")
	@ApiImplicitParam(name = "id", value = "organization id", required = true, dataType = "Long", paramType = "path")
	@GetMapping("/{id}")
	public Organization read(@PathVariable Long id) {
		logger.info("organization id : " + id);
		return organizationService.findById(id);
	}
	
	@ApiOperation(value="update organization", notes="update organization")
	@ApiParam(name = "organization", value = "organization to update", required = true)
	@PutMapping()
	public ResponseEntity<Organization> update(@RequestBody Organization organization){
		organization=organizationService.save(organization);
		return new ResponseEntity<Organization>(organization, HttpStatus.OK);
	}
	
	@ApiOperation(value="delete organization", notes="delete organization")
	@ApiImplicitParam(name = "id", value = "organization id", required = true, dataType = "Long", paramType = "path")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		logger.info("organization id : " + id);
		organizationService.deleteById(id);
	}
	
	@ApiOperation(value = "diable organization", notes = "disable organization")
	@ApiImplicitParam(name = "id", value = "organization id", required = true, dataType = "Long", paramType = "path")
	@PutMapping("/{id}/disable")
	public void disable(@PathVariable Long id) {
		logger.info("organization id : "+id);
		organizationService.disableById(id);
	}

	@ApiOperation(value = "get organization by id", notes = "get organization by id")
	@ApiImplicitParam(name = "id", value = "agency id", required = true, dataType = "Long", paramType = "path")
	@GetMapping("/agency/{agencyId}")
	public ResponseEntity<Page<Organization>> readAllByAgency(@RequestParam MultiValueMap<String, String> attributes, @PathVariable("agencyId") Long agencyId) {
		logger.info("agency id : " + agencyId);
		MultiValueMapConverter<Organization> converter = new MultiValueMapConverter<>(attributes, Organization.class);
		return ResponseEntity.ok(organizationService.findAllByAgency(agencyId, converter.getPageable()));
	}

	@ApiOperation(value = "get organization by company id", notes = "get organization by company id")
	@ApiImplicitParam(name = "id", value = "company id", required = true, dataType = "Long", paramType = "path")
	@GetMapping("/company/{companyId}")
	public ResponseEntity<Page<Organization>> readAllByCompany(@RequestParam MultiValueMap<String, String> attributes, @PathVariable("companyId") Long companyId) {
		logger.info("agency id : " + companyId);
		MultiValueMapConverter<Organization> converter = new MultiValueMapConverter<>(attributes, Organization.class);
		List<Agency> agencies = agencyService.findByCompany(companyId);
		if (agencies != null && agencies.size() > 0) {
			List<Long> ids = agencies.stream().map(v -> v.getId()).collect(Collectors.toList());
			return ResponseEntity.ok(organizationService.findAllByAgencies(ids, converter.getPageable()));
		}
		return ResponseEntity.ok(new PageImpl<Organization>(new ArrayList<Organization>()));
	}
	
	public Page<Organization> get_local_time(List<Organization> orgs, String target_time_zone){
		for(Organization org : orgs){
			org = get_object_with_local_times(org, target_time_zone);
		}

		return new PageImpl<>(orgs);
	}

	public List<Organization> get_local_time_list(List<Organization> orgs, String target_time_zone){
		for(Organization org : orgs){
			org = get_object_with_local_times(org, target_time_zone);
		}

		return new ArrayList<>(orgs);
	}
	
	public Organization get_object_with_local_times(Organization org, String target_time_zone){
		System.err.println("G" + org.getWorktimeStart());
		if(org.getWorktimeStart() != null)
			org.setWorktimeStart(DateUtils.get_local_time(org.getWorktimeStart(), target_time_zone));
		if(org.getWorktimeEnd() != null)
			org.setWorktimeEnd(DateUtils.get_local_time(org.getWorktimeEnd(), target_time_zone));
		System.err.println("G" + org.getWorktimeStart());
		return org;
	}

}
