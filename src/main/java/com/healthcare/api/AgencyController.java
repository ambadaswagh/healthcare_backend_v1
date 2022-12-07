package com.healthcare.api;

import static com.healthcare.api.common.HealthcareConstants.*;
import static com.healthcare.api.common.HealthcareUtil.*;
import static com.healthcare.api.common.RoleUtil.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.healthcare.dto.AgencyLogoSettingDTO;
import com.healthcare.dto.AgencyStatsDTO;
import com.healthcare.repository.AgencyRepository;
import com.healthcare.model.entity.AdminAgencyCompanyOrganization;
import com.healthcare.service.AdminAgencyCompanyOrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.healthcare.dto.UserDto;
import com.healthcare.exception.UserException;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.Agency;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.AgencyService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/api/agency")
public class AgencyController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AgencyService agencyService;

	@Autowired
	private AgencyRepository agencyRepository;

	@Autowired
	private AdminAgencyCompanyOrganizationService adminAgencyCompanyOrganizationService;


	@ApiOperation(value = "save agency", notes = "save agency")
	@ApiParam(name = "agency", value = "agency to save", required = true)
	@PostMapping()
	public ResponseEntity<Agency> create(@RequestBody Agency agency) {
		agency = agencyService.save(agency);
		return new ResponseEntity<Agency>(agency, HttpStatus.OK);
	}

	@ApiOperation(value = "get agency by id", notes = "get agency by id")
	@ApiImplicitParam(name = "id", value = "agency id", required = true, dataType = "Long" ,paramType = "path")
	@GetMapping("/{id}")
	public Agency read(@PathVariable Long id) {
		logger.info("id : " + id);
		return agencyService.findById(id);
	}

	@ApiOperation(value = "get all agency", notes = "get all agency")
	@GetMapping()
	public ResponseEntity<Page<Agency>> readAll(HttpServletRequest req, @RequestParam MultiValueMap<String, String> attributes) {
		/*MultiValueMapConverter<Agency> converter = new MultiValueMapConverter<>(attributes, Agency.class);
		return ResponseEntity.ok(agencyService.findAll(converter.getData(), converter.getPageable()));*/

		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if(isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}

		MultiValueMapConverter<Agency> converter = new MultiValueMapConverter<>(attributes, Agency.class);

		if(isSuperAdmin(admin)){
			return ResponseEntity.ok(agencyService.findAll(converter.getPageable()));
		} else if (isCompanyAdmin(admin)){

			// get company id by admin id
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
			if (adminAgencyCompanyOrganization != null) {
				return ResponseEntity.ok(agencyService.findByCompanyPage(adminAgencyCompanyOrganization.getCompany().getId(), converter.getPageable()));
			}

		} else if (isAgencyAdmin(admin)){
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
			if (adminAgencyCompanyOrganization != null) {
				return ResponseEntity.ok(new PageImpl<Agency>(Collections.singletonList(agencyService.findById(adminAgencyCompanyOrganization.getAgency().getId()))));
			}
		}

		return ResponseEntity.ok(new PageImpl<Agency>(new ArrayList<Agency>()));
	}

	@ApiOperation(value = "get all agency", notes = "get all agency")
	@GetMapping("/getAllAgency")
	public ResponseEntity<List<Agency>> getAllAgencyList() {
		return ResponseEntity.ok(agencyRepository.findAll());
	}

	@ApiOperation(value = "get all agency", notes = "get all agency")
	@GetMapping("/valid")
	public List<Agency> readAllWithRole(HttpServletRequest req) {
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
		
		if(isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}

		if(isSuperAdmin(admin)){
			return agencyService.findAll();
		} else if (isCompanyAdmin(admin)){
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
			if (adminAgencyCompanyOrganization != null) {
				return agencyService.findAllValid(adminAgencyCompanyOrganization.getCompany().getId());
			}
		} else if (isAgencyAdmin(admin)){
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
			if (adminAgencyCompanyOrganization != null) {
				return (List<Agency>) Collections.singleton(agencyService.findById(adminAgencyCompanyOrganization.getAgency().getId()));
			}
		}
		
		return null;
	}

	@ApiOperation(value = "update agency", notes = "update agency")
	@ApiParam(name = "agency", value = "agency to update", required = true)
	@PutMapping()
	public ResponseEntity<Agency> update(@RequestBody Agency agency) {
		agency = agencyService.save(agency);
		return new ResponseEntity<Agency>(agency, HttpStatus.OK);
	}

	@ApiOperation(value = "delete agency", notes = "delete agency")
	@ApiImplicitParam(name = "id", value = "agency id", required = true, dataType = "Long" ,paramType = "path")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		agencyService.deleteById(id);
	}
	
//	@ApiOperation(value = "seniors statistics",
//			notes = "1. Total seniors registered with this senior center. (it's 87 in the example)"
//					+ "2. Total checked in Seniors for that moment (It's 66 in the example)"
//					+ "3. Among checked Seniors, the total active Seniors. ")
//	@ApiImplicitParam(name = "id", value = "agency id", required = true, dataType = "Long", paramType = "path")
//	@GetMapping("/stats/{id}")
//	public UserDto generateUserStats(@PathVariable("id") Long id) {
//		logger.info("id : " + id);
//		return agencyService.generateUserStats(id);
//	}

	@ApiOperation(value = "disable agency", notes = "disable agency")
	@ApiImplicitParam(name = "id", value = "agency id", required = true, dataType = "Long" ,paramType = "path")
	@PutMapping("/{id}/disable")
	public void disable(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		agencyService.disableById(id);
	}

	@ApiOperation(value = "seniors statistics",
			notes = "1. Total seniors active in the senior center."
					+ "2. Total checked in Seniors for that moment"
					+ "3. Total seniors active and registered in the senior center.")
	@ApiImplicitParam(name = "id", value = "agency id", required = true, dataType = "Long", paramType = "path")
	@GetMapping("/stats/{id}")
	public Object agencyStatsProvider(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		return agencyService.computeAgencyStats(id);
	}

	@ApiOperation(value = "get all agency", notes = "get all agency")
	@GetMapping("/company/{companyId}")
	public Page<Agency> getByCompany(@RequestParam MultiValueMap<String, String> attributes, @PathVariable("companyId") Long companyId) {
		MultiValueMapConverter<Agency> converter = new MultiValueMapConverter<>(attributes, Agency.class);
		return agencyService.findByCompanyPage(companyId, converter.getPageable());
	}

	@ApiOperation(value = "get all agency", notes = "get all agency")
	@GetMapping("/companyList/{companyId}")
	public List<Agency> getListByCompany(@PathVariable("companyId") Long companyId) {
		return agencyService.findByCompany(companyId);
	}

	@ApiOperation(value = "check name for agency", notes = "check name for agency")
	@ApiParam(name = "name", value = "agency name", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@PostMapping("/name/{name}")
	public ResponseEntity<Boolean> checkNameExisted(@PathVariable String name) {
		boolean isValid = agencyService.isAgencyName(name);
		return new ResponseEntity<Boolean>(isValid, HttpStatus.OK);
	}

	@ApiOperation(value = "update meal selected", notes = "update meal selected")
	@ApiParam(name = "agency", value = "agency to meal selected", required = true)
	@GetMapping("/agency/{agencyId}/meal-selected/{mealSelected}")
	public void updateMealSelected(@PathVariable Long agencyId, @PathVariable String mealSelected){
		agencyService.updateMealSelected(agencyId, mealSelected);
	}

	@ApiOperation(value = "update meal selected", notes = "update meal selected")
	@ApiParam(name = "agency", value = "agency to meal selected", required = true)
	@GetMapping("/agency/{agencyId}/meal-selected")
	public void updateNoneMealSelected(@PathVariable Long agencyId){
		agencyService.updateMealSelected(agencyId, null);
	}

    @PostMapping("/{id}/updateAgreementSignatureId/{fileUploadId}")
    public ResponseEntity<Agency> updateAgreementSignatureId(@PathVariable Long id,
                                                             @PathVariable Long fileUploadId) {
        Agency rp = agencyService.updateAgreementSignatureId(id, fileUploadId);
        return new ResponseEntity<Agency>(rp, HttpStatus.OK);
    }

	@ApiOperation(value = "update logo setting", notes = "update logo setting")
	@ApiParam(name = "agency", value = "update logo setting", required = true)
	@PutMapping("/logo-setting")
	public void updateLogoSetting(@RequestBody AgencyLogoSettingDTO dto) {
		agencyService.updateLogoSetting(dto.getAgencyId(), dto.getLogoId(), dto.getMainWhiteLabel(), dto.getMainWhiteLabel());
	}

}
