package com.healthcare.api;


import com.healthcare.dto.SeniorMapDTO;
import com.healthcare.exception.UserException;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.AdminAgencyCompanyOrganization;
import com.healthcare.model.entity.User;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.AdminAgencyCompanyOrganizationService;
import com.healthcare.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.healthcare.api.common.HealthcareConstants.AUTHENTICATED_ADMIN;
import static com.healthcare.api.common.HealthcareConstants.NOT_AUTHORIZED;
import static com.healthcare.api.common.HealthcareUtil.isEmpty;
import static com.healthcare.api.common.RoleUtil.isAgencyAdmin;
import static com.healthcare.api.common.RoleUtil.isCompanyAdmin;
import static com.healthcare.api.common.RoleUtil.isSuperAdmin;

@RestController
@RequestMapping(value = "/api/map")
public class MapController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private AdminAgencyCompanyOrganizationService adminAgencyCompanyOrganizationService;

    @ApiOperation(value = "Get senior's location info", notes = "Get senior's location info")
    @ApiParam(name = "date", value = "ride date", required = true)
    @GetMapping()
    public ResponseEntity<List<SeniorMapDTO>> getSeniorInfo(HttpServletRequest req, @RequestParam(value = "date", required = false) String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        if(!StringUtils.isEmpty(dateStr)) {
            try {
                date = formatter.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

        if(isEmpty(admin)){
            throw new UserException(NOT_AUTHORIZED);
        }

        List<SeniorMapDTO> seniorMapDTOS = new ArrayList<SeniorMapDTO>();

        if(isSuperAdmin(admin)){
            seniorMapDTOS = userService.getSeniorMapLocations(date);
        } else if (isCompanyAdmin(admin)) {
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

            if (adminAgencyCompanyOrganization != null) {
                seniorMapDTOS = userService.getSeniorMapLocations(date, adminAgencyCompanyOrganization.getCompany().getId());
            }

        } else if (isAgencyAdmin(admin)) {
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

            if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
                seniorMapDTOS = userService.getSeniorMapLocations(date, adminAgencyCompanyOrganization.getCompany().getId(), adminAgencyCompanyOrganization.getAgency().getId());
            }
        }


        return new ResponseEntity<List<SeniorMapDTO>>(seniorMapDTOS, HttpStatus.OK);
    }

    @ApiOperation(value = "Get senior's location info", notes = "Get senior's location info")
    @ApiParam(name = "date", value = "ride date", required = true)
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<SeniorMapDTO>> getSeniorInfoByCompany(@RequestParam(value = "date", required = false) String dateStr, @PathVariable("companyId") Long companyId) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        if(!StringUtils.isEmpty(dateStr)) {
            try {
                date = formatter.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        List<SeniorMapDTO> seniorMapDTOS = userService.getSeniorMapLocations(date, companyId);

        return new ResponseEntity<List<SeniorMapDTO>>(seniorMapDTOS, HttpStatus.OK);
    }


    @ApiOperation(value = "Get senior's location info", notes = "Get senior's location info")
    @ApiParam(name = "date", value = "ride date", required = true)
    @GetMapping("/company/{companyId}/agency/{agencyId}")
    public ResponseEntity<List<SeniorMapDTO>> getSeniorInfoByCompanyAgency(
            @RequestParam(value = "date", required = false) String dateStr, @PathVariable("companyId") Long companyId, @PathVariable("agencyId") Long agencyId) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        if(!StringUtils.isEmpty(dateStr)) {
            try {
                date = formatter.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        List<SeniorMapDTO> seniorMapDTOS = userService.getSeniorMapLocations(date, companyId, agencyId);

        return new ResponseEntity<List<SeniorMapDTO>>(seniorMapDTOS, HttpStatus.OK);
    }

}
