package com.healthcare.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import com.healthcare.api.model.MealMobileDTO;
import com.healthcare.exception.UserException;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.AdminAgencyCompanyOrganization;
import com.healthcare.model.entity.Agency;
import com.healthcare.service.AdminAgencyCompanyOrganizationService;
import com.healthcare.service.AgencyService;
import net.minidev.json.JSONObject;
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

import com.healthcare.model.entity.Meal;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.MealService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import static com.healthcare.api.common.HealthcareConstants.AUTHENTICATED_ADMIN;
import static com.healthcare.api.common.HealthcareConstants.NOT_AUTHORIZED;
import static com.healthcare.api.common.HealthcareUtil.isEmpty;
import static com.healthcare.api.common.RoleUtil.isAgencyAdmin;
import static com.healthcare.api.common.RoleUtil.isCompanyAdmin;
import static com.healthcare.api.common.RoleUtil.isSuperAdmin;

/**
 * Meal controller
 */
@RestController
@RequestMapping("/api/meal")
public class MealController extends BaseController {

	private MealService mealService;

	@Autowired
	private AdminAgencyCompanyOrganizationService adminAgencyCompanyOrganizationService;

	@Autowired
	private AgencyService agencyService;

	@Autowired
	public MealController(MealService mealService) {
		this.mealService = mealService;
	}

	@ApiOperation(value = "Create meal", notes = "Create meal")
	@ApiParam(name = "meal", value = "meal to create", required = true)
	@PostMapping()
	public ResponseEntity create(@RequestBody Meal meal) {

		return ResponseEntity.ok(mealService.save(meal).getId());
	}

	@ApiOperation(value = "Update meals in Bulk", notes = "Update meals in Bulk")
	@ApiParam(name = "meal", value = "meals list to update", required = true)
	@PostMapping("/bulk")
	public ResponseEntity bulkUpdate(@RequestBody List<Meal> meals) {

		return ResponseEntity.ok(mealService.bulkUpdate(meals));
	}

	@ApiOperation(value = "Get meal by Id", notes = "Get meal info by mealId")
	@ApiImplicitParam(name = "id", value = "meal Id", required = true, dataType = "Long" ,paramType = "path")
	@GetMapping("/{id}")
	public ResponseEntity get(@PathVariable("id") String id) {
		Long mealId = parseId(id);

		if (mealId == null) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok(mealService.findById(mealId));
	}

	@ApiOperation(value = "Update meal", notes = "Update meal")
	@ApiParam(name = "meal", value = "meal to update", required = true)
	@PutMapping
	public void save(@RequestBody Meal meal) {
		mealService.save(meal);
	}

	@ApiOperation(value = "Delete meal", notes = "Delete meal")
	@ApiImplicitParam(name = "id", value = "meal Id", required = true, dataType = "Long" ,paramType = "path")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") String id, HttpServletResponse response) {
		Long mealId = parseId(id);

		if (mealId == null) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return;
		}

		mealService.deleteById(mealId);
	}

	@ApiOperation(value = "Disable meal", notes = "Disable meal")
	@ApiImplicitParam(name = "id", value = "meal Id", required = true, dataType = "Long" ,paramType = "path")
	@PutMapping("/{id}/disable")
	public void disable(@PathVariable("id") String id, HttpServletResponse response) {
		Long mealId = parseId(id);

		if (mealId == null) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return;
		}

		mealService.disableById(mealId);
	}
	
	@ApiOperation(value = "Get all meals", notes = "Get all meals")
	@GetMapping()
	public ResponseEntity<Page<Meal>> readAll(HttpServletRequest req, @RequestParam MultiValueMap<String, String> attributes) {
		/*MultiValueMapConverter<Meal> converter = new MultiValueMapConverter<>(attributes, Meal.class);
		return ResponseEntity.ok(mealService.findAll(converter.getData(), converter.getPageable()));*/
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if(isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}

		MultiValueMapConverter<Meal> converter = new MultiValueMapConverter<>(attributes, Meal.class);

		if(isSuperAdmin(admin)){
			return ResponseEntity.ok(mealService.findAll(converter.getData(), converter.getPageable()));
		} else if (isCompanyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null) {
				// find all agencies by company id
				List<Agency> agencies = agencyService.findByCompany(adminAgencyCompanyOrganization.getCompany().getId());
				if (agencies != null && agencies.size() > 0) {
					List<Long> ids = agencies.stream().map(v -> v.getId()).collect(Collectors.toList());
					return ResponseEntity.ok(mealService.findAllByAgencies(ids, converter.getPageable()));
				}
			}

		} else if (isAgencyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
				return ResponseEntity.ok(
						mealService.findAllByAgency(adminAgencyCompanyOrganization.getAgency().getId(), converter.getPageable())
				);
			}
		}

		return ResponseEntity.ok(new PageImpl<Meal>(new ArrayList<Meal>()));
	}

	@ApiOperation(value = "get all meal by agency id", notes = "get all meal by agency id")
	@ApiImplicitParam(name = "id", value = "agency id", required = true, dataType = "String" ,paramType = "path")
	@GetMapping("/agency/{agencyId}")
	public ResponseEntity<Page<Meal>> readAllByAgency(@RequestParam MultiValueMap<String, String> attributes, @PathVariable("agencyId") Long agencyId) {
		MultiValueMapConverter<Meal> converter = new MultiValueMapConverter<>(attributes, Meal.class);
		return ResponseEntity.ok(mealService.findAllByAgency(agencyId, converter.getPageable()));
	}

	@ApiOperation(value = "get all meal by agency id", notes = "get all meal by agency id")
	@ApiImplicitParam(name = "id", value = "agency id", required = true, dataType = "String" ,paramType = "path")
	@GetMapping("/mobile/{agencyId}/{userId}")
	public ResponseEntity<JSONObject> readAllByAgency(@PathVariable("agencyId") Long agencyId,
													  @PathVariable("userId") Long userId) {
		MealMobileDTO mealMobileDTO = mealService.findAllByAgency(agencyId, userId);
		JSONObject entity = new JSONObject();
		try {
			entity.put("result", "SUCCESS");
			entity.put("data", mealMobileDTO);
			entity.put("message", "");
			return new ResponseEntity<JSONObject>(entity, HttpStatus.OK);
		} catch (Exception e) {
			entity.put("result", "ERROR");
			entity.put("data", new JSONObject());
			entity.put("message", "Invalid Input");
			return new ResponseEntity<JSONObject>(entity, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "get all meal by company id", notes = "get all meal by company id")
	@ApiImplicitParam(name = "id", value = "company id", required = true, dataType = "String" ,paramType = "path")
	@GetMapping("/company/{companyId}")
	public ResponseEntity<Page<Meal>> readAllByCompany(@RequestParam MultiValueMap<String, String> attributes, @PathVariable("companyId") Long companyId) {
		MultiValueMapConverter<Meal> converter = new MultiValueMapConverter<>(attributes, Meal.class);

		List<Agency> agencies = agencyService.findByCompany(companyId);
		if (agencies != null && agencies.size() > 0) {
			List<Long> ids = agencies.stream().map(v -> v.getId()).collect(Collectors.toList());
			return ResponseEntity.ok(mealService.findAllByAgencies(ids, converter.getPageable()));
		}
		return ResponseEntity.ok(new PageImpl<Meal>(new ArrayList<Meal>()));
	}
}
