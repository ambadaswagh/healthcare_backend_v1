package com.healthcare.api;

import static com.healthcare.api.common.HealthcareConstants.AUTHENTICATED_ADMIN;
import static com.healthcare.api.common.HealthcareUtil.isEmpty;
import static com.healthcare.api.common.RoleUtil.isAgencyAdmin;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.AdminAgencyCompanyOrganization;
import com.healthcare.model.entity.Billing;
import com.healthcare.service.BillingService;
import com.healthcare.util.DateUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/billing")
public class BillingController extends BaseController {

	@Autowired
	BillingService billingService;
	
	@ApiOperation(value = "Create billing", notes = "Create billing for trip or visit")
	@ApiParam(name = "billing", value = "billing to create", required = true)
	@PostMapping
	public ResponseEntity create(HttpServletRequest req, @RequestBody Billing billing) {
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
		return ResponseEntity.ok(billingService.save(billing,admin.getId()).getId());
	}
	
	
	
	@ApiOperation(value = "Get billing by Id", notes = "Get billing info by id")
	@ApiImplicitParam(name = "id", value = "activity Id", required = true, dataType = "Long", paramType = "path")
	@GetMapping("/{id}")
	public ResponseEntity get(HttpServletRequest req, @PathVariable("id") String id) {
		Long billingId = parseId(id);
		if (billingId == null) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(billingService.findById(billingId));
	}
	
	@ApiOperation(value = "Update billing", notes = "Update billing for trip or visit")
	@ApiParam(name = "billing", value = "billing to update", required = true)
	@PutMapping
	public void update(HttpServletRequest req, @RequestBody Billing billing) {
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
		billingService.save(billing,admin.getId());
	}
}
