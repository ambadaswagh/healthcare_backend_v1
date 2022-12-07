package com.healthcare.api;

import com.healthcare.api.auth.AbstractBasedAPI;
import com.healthcare.api.common.HealthcareUtil;
import com.healthcare.dto.ReminderDTO;
import com.healthcare.exception.UserException;
import com.healthcare.model.entity.*;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.AdminAgencyCompanyOrganizationService;
import com.healthcare.service.AgencyService;
import com.healthcare.service.VehicleService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

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

/**
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api/vehicle")
public class VehicleController extends AbstractBasedAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private AdminAgencyCompanyOrganizationService adminAgencyCompanyOrganizationService;

    @Autowired
    private AgencyService agencyService;

    @ApiOperation(value = "Save Vehicle", notes = "Save Vehicle")
    @ApiParam(name = "Vehicle", value = "Vehicle to save", required = true)
    @PostMapping()
    public ResponseEntity<Vehicle> create(@RequestBody Vehicle vehicle) {
        vehicle = vehicleService.save(vehicle);
        return new ResponseEntity<Vehicle>(vehicle, HttpStatus.OK);
    }

    @ApiOperation(value = "get all Vehicles", notes = "get all Vehicles")
    @ApiParam(name = "id", value = "Vehicle id", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @GetMapping
    public ResponseEntity<Page<Vehicle>> readAll(HttpServletRequest req, @RequestParam MultiValueMap<String, String> attributes) {
        /*MultiValueMapConverter<Vehicle> converter = new MultiValueMapConverter<Vehicle>(attributes, Vehicle.class);
        return vehicleService.findAll(converter.getData(), converter.getPageable());*/
        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

        if (isEmpty(admin)) {
            throw new UserException(NOT_AUTHORIZED);
        }

        MultiValueMapConverter<Vehicle> converter = new MultiValueMapConverter<>(attributes, Vehicle.class);
        if (isSuperAdmin(admin)) {
            return ResponseEntity.ok(vehicleService.findAll(converter.getData(), converter.getPageable()));
        }
        else if (isCompanyAdmin(admin)){

            // get company id by admin id
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
            if (adminAgencyCompanyOrganization != null) {

                // find all agencies by company id
                List<Agency> agencies = agencyService.findByCompany(adminAgencyCompanyOrganization.getCompany().getId());
                if (agencies != null && agencies.size() > 0) {
                    List<Long> ids = agencies.stream().map(v -> v.getId()).collect(Collectors.toList());
                    return ResponseEntity.ok(vehicleService.findVehicleByAgencies(ids, converter.getPageable()));
                }
            }
        } else if (isAgencyAdmin(admin)) {
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
            if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
                return ResponseEntity.ok(vehicleService.getVehiclesByAgency(adminAgencyCompanyOrganization.getAgency().getId(), converter.getPageable()));
            }
        }

        return null;
    }
//    public ResponseEntity<List<Vehicle>> getAllDriver() {
//        return ResponseEntity.ok(vehicleService.findAll());
//    }

    @ApiOperation(value = "get all Vehicles", notes = "get all Vehicles")
    @GetMapping("/getAllVehicles")

    public ResponseEntity<List<Vehicle>> getAllDriver(HttpServletRequest req) {
        /*MultiValueMapConverter<Vehicle> converter = new MultiValueMapConverter<Vehicle>(attributes, Vehicle.class);
        return vehicleService.findAll(converter.getData(), converter.getPageable());*/
        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

        if (isEmpty(admin)) {
            throw new UserException(NOT_AUTHORIZED);
        }


        if (isSuperAdmin(admin)) {
            return ResponseEntity.ok(vehicleService.findAll());
        }
        else if (isCompanyAdmin(admin)){

            // get company id by admin id
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
            if (adminAgencyCompanyOrganization != null) {

                // find all agencies by company id
                List<Agency> agencies = agencyService.findByCompany(adminAgencyCompanyOrganization.getCompany().getId());
                if (agencies != null && agencies.size() > 0) {
                    List<Long> ids = agencies.stream().map(v -> v.getId()).collect(Collectors.toList());
                    return ResponseEntity.ok(vehicleService.findVehicleByAgenciesList(ids));
                }
            }
        } else if (isAgencyAdmin(admin)) {
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
            if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
                return ResponseEntity.ok(vehicleService.getVehiclesByAgencyList(adminAgencyCompanyOrganization.getAgency().getId()));
            }
        }

        return null;
    }

    @ApiOperation(value = "update Vehicle", notes = "update Vehicle")
    @ApiParam(name = "vehicle", value = "vehicle", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @PutMapping
    public ResponseEntity<Vehicle> update(@RequestBody Vehicle vehicle) {
        vehicle = vehicleService.save(vehicle);
        return  new ResponseEntity<Vehicle>(vehicle,HttpStatus.OK);
    }

    @ApiOperation(value = "get vehicle by Id", notes = "get vehicle by Id")
    @ApiParam(name = "id", value = "Vehicle id", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/{id}")
    public Vehicle getVehicleById(@PathVariable("id") Long id) {
        return vehicleService.findById(id);
    }

    @ApiOperation(value = "get all vehicles by agency id", notes = "get all vehicles by agency id")
    @ApiParam(name = "id", value = "agency id", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/agency/{agencyId}")
    public ResponseEntity<Page<Vehicle>> getVehicles(@RequestParam MultiValueMap<String, String> attributes, @PathVariable Long agencyId) {
        MultiValueMapConverter<Vehicle> converter = new MultiValueMapConverter<Vehicle>(attributes, Vehicle.class);
        return ResponseEntity.ok(vehicleService.getVehiclesByAgency(agencyId, converter.getPageable()));
    }

    @ApiOperation(value = "List Vehicle have Registration end next few days", notes = "Default will be 10 days")
    @ApiImplicitParam(name = "days", value = "how many next day. Default value is 10", dataType = "Integer" ,paramType = "path")
    @RequestMapping(value = "/status/registration-expire/{days}", method = RequestMethod.GET)
    public ResponseEntity<?> getVehicleRegistrationEndFewDays(HttpServletRequest req, @PathVariable Integer days, @RequestParam MultiValueMap<String, String> attributes) {

        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
        if (HealthcareUtil.isEmpty(admin)){
            throw new UserException(NOT_AUTHORIZED);
        }

        Integer realDay = days == null ? 0 : days;
        if(realDay < 0){
            return ResponseEntity.badRequest().body("days must be larger than 0");
        }

        ReminderDTO dto = new ReminderDTO().fromMap(attributes);
        return ResponseEntity.ok(vehicleService.getVehicleRegistrationEndNextDays(realDay, admin,
                new PageRequest(dto.getPage(), dto.getSize()), dto.getCompanyId(), dto.getAgencyId()));
    }

    @ApiOperation(value = "List Vehicle have liability insurance end next few days", notes = "Default will be 10 days")
    @ApiImplicitParam(name = "days", value = "how many next day. Default value is 10", dataType = "Integer" ,paramType = "path")
    @RequestMapping(value = "/status/liability-insurance-expire/{days}", method = RequestMethod.GET)
    public ResponseEntity<?> getVehicleLiabilityInsuranceEndFewDays(HttpServletRequest req, @PathVariable Integer days, @RequestParam MultiValueMap<String, String> attributes) {

        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
        if (HealthcareUtil.isEmpty(admin)){
            throw new UserException(NOT_AUTHORIZED);
        }

        Integer realDay = days == null ? 0 : days;
        if(realDay < 0){
            return ResponseEntity.badRequest().body("days must be larger than 0");
        }

        ReminderDTO dto = new ReminderDTO().fromMap(attributes);
        return ResponseEntity.ok(vehicleService.getVehicleLiabilityInsuranceEndNextDays(realDay, admin,
                new PageRequest(dto.getPage(), dto.getSize()), dto.getCompanyId(), dto.getAgencyId()));
    }

    @ApiOperation(value = "List Vehicle have extra insurance end next few days", notes = "Default will be 10 days")
    @ApiImplicitParam(name = "days", value = "how many next day. Default value is 10", dataType = "Integer" ,paramType = "path")
    @RequestMapping(value = "/status/extra-insurance-expire/{days}", method = RequestMethod.GET)
    public ResponseEntity<?> getVehicleExtraInsuranceEndFewDays(HttpServletRequest req, @PathVariable Integer days, @RequestParam MultiValueMap<String, String> attributes) {

        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
        if (HealthcareUtil.isEmpty(admin)){
            throw new UserException(NOT_AUTHORIZED);
        }

        Integer realDay = days == null ? 0 : days;
        if(realDay < 0){
            return ResponseEntity.badRequest().body("days must be larger than 0");
        }

        ReminderDTO dto = new ReminderDTO().fromMap(attributes);
        return ResponseEntity.ok(vehicleService.getVehicleExtraInsuranceEndNextDays(realDay, admin,
                new PageRequest(dto.getPage(), dto.getSize()), dto.getCompanyId(), dto.getAgencyId()));
    }

    @ApiOperation(value = "List Vehicle have Inspection end next few days", notes = "Default will be 10 days")
    @ApiImplicitParam(name = "days", value = "how many next day. Default value is 10", dataType = "Integer" ,paramType = "path")
    @RequestMapping(value = "/status/inspection-expire/{days}", method = RequestMethod.GET)
    public ResponseEntity<?> getVehicleInspectionEndFewDays(HttpServletRequest req, @PathVariable Integer days, @RequestParam MultiValueMap<String, String> attributes) {

        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
        if (HealthcareUtil.isEmpty(admin)){
            throw new UserException(NOT_AUTHORIZED);
        }

        Integer realDay = days == null ? 0 : days;
        if(realDay < 0){
            return ResponseEntity.badRequest().body("days must be larger than 0");
        }

        ReminderDTO dto = new ReminderDTO().fromMap(attributes);
        return ResponseEntity.ok(vehicleService.getVehicleInspectionEndNextDays(realDay, admin,
                new PageRequest(dto.getPage(), dto.getSize()), dto.getCompanyId(), dto.getAgencyId()));
    }


}
