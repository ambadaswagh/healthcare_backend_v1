package com.healthcare.api;

import static com.healthcare.api.common.HealthcareConstants.AUTHENTICATED_ADMIN;
import static com.healthcare.api.common.HealthcareConstants.NOT_AUTHORIZED;
import static com.healthcare.api.common.HealthcareUtil.isEmpty;
import static com.healthcare.api.common.RoleUtil.isAgencyAdmin;
import static com.healthcare.api.common.RoleUtil.isCompanyAdmin;
import static com.healthcare.api.common.RoleUtil.isSuperAdmin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.healthcare.model.entity.AdminAgencyCompanyOrganization;
import com.healthcare.service.AdminAgencyCompanyOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import com.healthcare.dto.AppointmentSearchDTO;
import com.healthcare.exception.UserException;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.Appointment;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.AppointmentService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Appointment controller
 */
@RestController
@RequestMapping("/api/appointment")
public class AppointmentController extends BaseController {

	@Autowired
	private AppointmentService appointmentService;

	@Autowired
	AdminAgencyCompanyOrganizationService adminAgencyCompanyOrganizationService;

	@ApiOperation(value = "Create appointment", notes = "Create an appointment")
	@ApiParam(name = "appointment", value = "appointment to create", required = true)
	@PostMapping
	public ResponseEntity create(@RequestBody Appointment appointment, HttpServletRequest req) {
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
		if (isEmpty(admin)) {
			throw new UserException(NOT_AUTHORIZED);
		}
		appointment.setAdmin(admin);
		return ResponseEntity.ok(appointmentService.save(appointment).getId());
	}

	@ApiOperation(value = "Get appointment by Id", notes = "Get appointment info by activityId")
	@ApiImplicitParam(name = "id", value = "appointment Id", required = true, dataType = "Long", paramType = "path")
	@GetMapping("/{id}")
	public ResponseEntity get(@PathVariable("id") Long id) {
		if (id == null) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(appointmentService.findById(id));
	}

	@ApiOperation(value = "Get all appointments", notes = "Get all appointments")
	@GetMapping()
	public ResponseEntity<Page<Appointment>> getAll(HttpServletRequest req, @RequestParam MultiValueMap<String, String> attributes,
													Pageable pageable) {
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
		if (isEmpty(admin)) {
			throw new UserException(NOT_AUTHORIZED);
		}
		if(isSuperAdmin(admin)) {
			AppointmentSearchDTO dto = new AppointmentSearchDTO().fromMap(attributes);
			return ResponseEntity.ok(appointmentService.search(dto, pageable));
		}
		AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
		if(isCompanyAdmin(admin)) {
			return getAllByCompany(adminAgencyCompanyOrganization.getCompany().getId(), attributes, pageable);
		}
		if(isAgencyAdmin(admin)) {
			return getAllByAgency(adminAgencyCompanyOrganization.getAgency().getId(), attributes, pageable);
		}
		throw new UserException(NOT_AUTHORIZED);
	}

	@ApiOperation(value = "Get all appointments by company", notes = "Get all appointments by company")
	@GetMapping("/company/{companyId}")
	public ResponseEntity<Page<Appointment>> getAllByCompany(@PathVariable("companyId") Long companyId, @RequestParam MultiValueMap<String, String> attributes,
															 Pageable pageable) {
		AppointmentSearchDTO dto = new AppointmentSearchDTO().fromMap(attributes);
		return ResponseEntity.ok(appointmentService.searchByCompany(companyId, dto, pageable));
	}

	@ApiOperation(value = "Get all appointments by agency", notes = "Get all appointments by agency")
	@GetMapping("/agency/{agencyId}")
	public ResponseEntity<Page<Appointment>> getAllByAgency(@PathVariable("agencyId") Long agencyId, @RequestParam MultiValueMap<String, String> attributes,
															 Pageable pageable) {
		AppointmentSearchDTO dto = new AppointmentSearchDTO().fromMap(attributes);
		return ResponseEntity.ok(appointmentService.searchByAgency(agencyId, dto, pageable));
	}

	@ApiOperation(value = "Update appointment", notes = "Update an appointment")
	@ApiParam(name = "appointment", value = "appointment to update", required = true)
	@PutMapping
	public void save(@RequestBody Appointment appointment) {
		appointmentService.save(appointment);
	}

	@ApiOperation(value = "Delete appointment", notes = "Delete an appointment")
	@ApiImplicitParam(name = "id", value = "appointment Id", required = true, dataType = "Long", paramType = "path")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id, HttpServletResponse response) {
		if (id == null) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return;
		}
		appointmentService.deleteById(id);
	}

}
