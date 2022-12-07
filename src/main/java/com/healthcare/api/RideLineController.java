package com.healthcare.api;

import com.healthcare.exception.UserException;
import com.healthcare.model.entity.*;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.repository.RideLineColorRepository;
import com.healthcare.repository.RideLineRepository;
import com.healthcare.repository.RideRepository;
import com.healthcare.repository.UserRepository;

import com.healthcare.service.AdminAgencyCompanyOrganizationService;
import com.healthcare.service.AgencyService;
import com.healthcare.service.RideLineService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.healthcare.api.common.HealthcareConstants.AUTHENTICATED_ADMIN;
import static com.healthcare.api.common.HealthcareConstants.NOT_AUTHORIZED;
import static com.healthcare.api.common.HealthcareUtil.isEmpty;
import static com.healthcare.api.common.RoleUtil.isAgencyAdmin;
import static com.healthcare.api.common.RoleUtil.isCompanyAdmin;
import static com.healthcare.api.common.RoleUtil.isSuperAdmin;

@RestController
@RequestMapping(value = "/api/rideLine")
public class RideLineController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RideLineService rideLineService;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private RideLineRepository rideLineRepository;

    @Autowired
    private RideLineColorRepository rideLineColorRepository;

    @Autowired
    private AdminAgencyCompanyOrganizationService adminAgencyCompanyOrganizationService;

    @Autowired
    private AgencyService agencyService;

    @ApiOperation(value = "save rideLine", notes = "save rideLine")
    @ApiParam(name = "rideLine", value = "rideLine to save", required = true)
    @PostMapping()
    public ResponseEntity<RideLine> create(@RequestBody RideLine rideLine) {
        if(rideLine.getId() != null){
            RideLineColor rideLineColor = rideLineColorRepository.findByName(rideLine.getRideLineColor());
            //rideLineColor.setRideLineId(rideLine.getId());
            rideLineColorRepository.save(rideLineColor);
        }
        rideLine = rideLineRepository.save(rideLine);
        return new ResponseEntity<RideLine>(rideLine, HttpStatus.OK);
    }

    @ApiOperation(value = "get rideLine by id", notes = "get rideLine by id")
    @ApiImplicitParam(name = "id", value = "rideLine id", required = true, dataType = "Long" ,paramType = "path")
    @GetMapping("/{id}")
    public RideLine read(@PathVariable Long id) {
        logger.info("id : " + id);
        return rideLineRepository.findOne(id);
    }

    @ApiOperation(value = "get all riedLine", notes = "get all riedLine")
    @GetMapping("/getAllRideLines")
    public ResponseEntity<Page<RideLine>> readAll(@RequestParam MultiValueMap<String, String> attributes) {
        MultiValueMapConverter<RideLine> converter = new MultiValueMapConverter<>(attributes, RideLine.class);
        return ResponseEntity.ok(rideLineService.findAll(converter.getData(), converter.getPageable()));
    }

    @GetMapping("/getAllRideLinesForRoute")
    public ResponseEntity<Page<RideLine>> getAllRideLinesForRoute(HttpServletRequest req,@RequestParam MultiValueMap<String, String> attributes) {
        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

        if (isEmpty(admin)) {
            throw new UserException(NOT_AUTHORIZED);
        }

        MultiValueMapConverter<RideLine> converter = new MultiValueMapConverter<>(attributes, RideLine.class);

        if (isSuperAdmin(admin)) {
            return ResponseEntity.ok(rideLineService.findAll(converter.getData(), converter.getPageable()));
        }
        else if (isCompanyAdmin(admin)){

            // get company id by admin id
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
            if (adminAgencyCompanyOrganization != null) {

                // find all agencies by company id
                List<Agency> agencies = agencyService.findByCompany(adminAgencyCompanyOrganization.getCompany().getId());
                if (agencies != null && agencies.size() > 0) {
                    List<Long> ids = agencies.stream().map(v -> v.getId()).collect(Collectors.toList());
                    return ResponseEntity.ok(rideLineService.findRideLineByAgenciesList(ids, converter.getPageable()));
                }
            }
        } else if (isAgencyAdmin(admin)) {
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
            if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
                return ResponseEntity.ok(rideLineService.getRideLineByAgencyList(adminAgencyCompanyOrganization.getAgency().getId(), converter.getPageable()));
            }
        }

        return null;
    }

    @ApiOperation(value = "get all riedLine", notes = "get all riedLine")
    @GetMapping("/getDistRideLineForRides")
    public ResponseEntity<List<RideLine>> getDistRideLineForRides(@RequestParam(value = "agencyId") Long agencyId,
                                                                  @RequestParam(value = "companyId") Long companyId) {
        return ResponseEntity.ok(rideLineService.getDistRideLineForRides(agencyId, companyId));
    }
    @PostMapping("/updateStatus/{id}")
    public ResponseEntity<Integer> updateStatus(@RequestParam(value = "status") Long status, @PathVariable Long id) {
        return ResponseEntity.ok(rideLineService.updateStatus(id, status));
    }

    @GetMapping("/getAllRideLinesWithSeniorName")
    public ResponseEntity<List<RideLine>> getAllRideLinesWithSeniorName(@RequestParam(value = "agencyId") Long agencyId,
                                                                        @RequestParam(value = "companyId") Long companyId) {
        return ResponseEntity.ok(rideLineService.getRideLines(agencyId, companyId));
    }

    @Transactional
    @PostMapping("/deleteridelineandrides/{id}")
    public ResponseEntity<Integer> delete(@PathVariable("id") Long id) {
        logger.info("id : " + id);
        userRepository.removeRideLine(id);
        rideRepository.deleteRide(id);
        rideLineService.deleteById(id);
        return ResponseEntity.ok(new Integer(1));
    }

    @Transactional
    @PostMapping("/updateRideLineForUser")
    public ResponseEntity<Integer> updateRideLineForUser(@RequestParam(value = "rideLineId", required = false) Long rideLineId,
                               @RequestParam("userId") Long userId) {
        RideLine rideLine = null;
        if(rideLineId != null) {
            rideLine = rideLineRepository.findOne(rideLineId);
        }
       return ResponseEntity.ok(userRepository.updateRideLine(userId, rideLine));
    }

}
