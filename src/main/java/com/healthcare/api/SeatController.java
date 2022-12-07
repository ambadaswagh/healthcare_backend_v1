package com.healthcare.api;

import com.healthcare.api.auth.AbstractBasedAPI;
import com.healthcare.exception.UserException;
import com.healthcare.model.entity.*;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.AdminAgencyCompanyOrganizationService;
import com.healthcare.service.AgencyService;
import com.healthcare.service.SeatService;
import com.healthcare.service.TableService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
@RequestMapping(value = "/api/seat")
public class SeatController extends AbstractBasedAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeatService seatService;

    @Autowired
    private AdminAgencyCompanyOrganizationService adminAgencyCompanyOrganizationService;

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private TableService tableService;

    @ApiOperation(value = "Save Seat", notes = "Save Seat")
    @ApiParam(name = "Seat", value = "Seat to save", required = true)
    @PostMapping()
    public ResponseEntity<Seat> create(@RequestBody Seat seat) {
        seat = seatService.save(seat);
        return new ResponseEntity<Seat>(seat, HttpStatus.OK);
    }

    @ApiOperation(value = "get all Seats", notes = "get all Seats")
    @ApiParam(name = "id", value = "Seat id", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @GetMapping
    public Page<Seat> readAll(HttpServletRequest req, @RequestParam MultiValueMap<String, String> attributes) {
        /*MultiValueMapConverter<Seat> converter = new MultiValueMapConverter<Seat>(attributes, Seat.class);
        return seatService.findAll(converter.getData(), converter.getPageable());*/

        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

        if(isEmpty(admin)){
            throw new UserException(NOT_AUTHORIZED);
        }

        MultiValueMapConverter<Seat> converter = new MultiValueMapConverter<Seat>(attributes, Seat.class);

        if(isSuperAdmin(admin)){
            return seatService.findAll(converter.getData(), converter.getPageable());
        } else if (isCompanyAdmin(admin)) {
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

            if (adminAgencyCompanyOrganization != null) {
                List<Agency> agencies = agencyService.findByCompany(adminAgencyCompanyOrganization.getCompany().getId());
                if (agencies != null && agencies.size() > 0) {
                    List<Long> ids = agencies.stream().map(v -> v.getId()).collect(Collectors.toList());
                    Page<TableEntity> tables =  tableService.findByAgencyIds(ids, converter.getPageable());
                    if (tables != null) {
                        List<Long> tableIds = tables.getContent().stream().map(v -> v.getId()).collect(Collectors.toList());
                        return seatService.findByTableIdList(tableIds, converter.getPageable());
                    }
                }
            }

        } else if (isAgencyAdmin(admin)) {
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

            if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
                List<TableEntity> tableEntities = tableService.findByAgencyId(adminAgencyCompanyOrganization.getAgency().getId());
                if (tableEntities != null) {
                    List<Long> tableIds = tableEntities.stream().map(v -> v.getId()).collect(Collectors.toList());
                    return seatService.findByTableIdList(tableIds, converter.getPageable());
                }
            }
        }

        return null;
    }

    @ApiOperation(value = "get all Seats by agency id", notes = "get all Seats by agency id")
    @ApiParam(name = "id", value = "agency id", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/agency/{agencyId}")
    public Page<Seat> getSeatByAgency(@PathVariable Long agencyId, @RequestParam MultiValueMap<String, String> attributes) {
        MultiValueMapConverter<Seat> converter = new MultiValueMapConverter<Seat>(attributes, Seat.class);
        List<TableEntity> tables =  tableService.findByAgency(agencyId);
        if (tables != null) {
            List<Long> tableIds = tables.stream().map(v -> v.getId()).collect(Collectors.toList());
            return seatService.findByTableIdList(tableIds, converter.getPageable());
        }
        return new PageImpl<Seat>(new ArrayList<Seat>());
    }

    @ApiOperation(value = "get all Seats by agency id", notes = "get all Seats by agency id")
    @ApiParam(name = "id", value = "agency id", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/company/{companyId}")
    public Page<Seat> getSeatByCompany(@PathVariable("companyId") Long companyId, @RequestParam MultiValueMap<String, String> attributes) {
        MultiValueMapConverter<Seat> converter = new MultiValueMapConverter<Seat>(attributes, Seat.class);
        List<Agency> agencies = agencyService.findByCompany(companyId);
        if (agencies != null && agencies.size() > 0) {
            List<Long> ids = agencies.stream().map(v -> v.getId()).collect(Collectors.toList());
            List<TableEntity> tables =  tableService.findByAgencyIds(ids);
            if (tables != null) {
                List<Long> tableIds = tables.stream().map(v -> v.getId()).collect(Collectors.toList());
                return seatService.findByTableIdList(tableIds, converter.getPageable());
            }
        }
        return new PageImpl<Seat>(new ArrayList<Seat>());
    }

    @ApiOperation(value = "update Seat", notes = "update Seat")
    @ApiParam(name = "seat", value = "seat", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @PutMapping
    public ResponseEntity<Seat> update(@RequestBody Seat seat) {
        seat = seatService.save(seat);
        return  new ResponseEntity<Seat>(seat,HttpStatus.OK);
    }

    @ApiOperation(value = "get seat by Id", notes = "get seat by Id")
    @ApiParam(name = "id", value = "Seat id", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/{id}")
    public Seat getSeatById(@PathVariable("id") Long id) {
        return seatService.findById(id);
    }

    @ApiOperation(value = "delete seat", notes = "delete seat")
    @ApiParam(name = "id", value = "Seat id", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        logger.info("id : " + id);
        seatService.deleteById(id);
    }
}
