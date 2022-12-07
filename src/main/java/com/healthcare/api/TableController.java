package com.healthcare.api;

import java.util.List;

import com.healthcare.dto.BlackListDTO;
import com.healthcare.model.enums.SeatStatusEnum;
import com.healthcare.model.enums.StatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.healthcare.api.model.TableArrangementResponseDTO;
import com.healthcare.model.entity.Seat;
import com.healthcare.model.entity.TableEntity;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.AgencyService;
import com.healthcare.service.SeatService;
import com.healthcare.service.TableService;
import com.healthcare.service.UserService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.healthcare.exception.UserException;
import com.healthcare.model.entity.*;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.*;
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
import java.util.Date;
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
@RequestMapping(value = "/api/table")
public class TableController extends AbstractBasedAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TableService tableService;
    @Autowired
    private AgencyService agencyService;
    @Autowired
    private SeatService seatService;
    @Autowired
    UserService userService;
    @Autowired
    private AdminAgencyCompanyOrganizationService adminAgencyCompanyOrganizationService;
    @Autowired
    private VisitService visitService;


    @ApiOperation(value = "Save table", notes = "Save Table")
    @ApiParam(name = "Table", value = "Table to save", required = true)
    @PostMapping()
    public ResponseEntity<TableEntity> create(@RequestBody TableEntity table) {
        table.setAgency(agencyService.findById(table.getAgency().getId()));
        return new ResponseEntity<TableEntity>(tableService.save(table),HttpStatus.OK);
    }

    @ApiOperation(value = "get all Tables by agency id", notes = "get all Tables by agency id")
    @ApiParam(name = "id", value = "agency id", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/agency/{agencyId}")
    public Page<TableEntity> getTableByAgency(@PathVariable Long agencyId, @RequestParam MultiValueMap<String, String> attributes) {
        MultiValueMapConverter<TableEntity> converter = new MultiValueMapConverter<TableEntity>(attributes, TableEntity.class);
        return tableService.findByAgency(agencyId, converter.getPageable());
    }

    @ApiOperation(value = "get all Tables by agency id", notes = "get all Tables by agency id")
    @ApiParam(name = "id", value = "agency id", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/company/{companyId}")
    public Page<TableEntity> getTableByCompany(@PathVariable("companyId") Long companyId, @RequestParam MultiValueMap<String, String> attributes) {
        MultiValueMapConverter<TableEntity> converter = new MultiValueMapConverter<TableEntity>(attributes, TableEntity.class);

        List<Agency> agencies = agencyService.findByCompany(companyId);
        if (agencies != null && agencies.size() > 0) {
            List<Long> ids = agencies.stream().map(v -> v.getId()).collect(Collectors.toList());
            return tableService.findByAgencyIds(ids, converter.getPageable());
        }
        return new PageImpl<TableEntity>(new ArrayList<TableEntity>());
    }

    @ApiOperation(value = "get all Tables", notes = "get all Tables")
    @ApiParam(name = "id", value = "Table id", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @GetMapping
    public Page<TableEntity> readAll(HttpServletRequest req, @RequestParam MultiValueMap<String, String> attributes) {

        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

        if(isEmpty(admin)){
            throw new UserException(NOT_AUTHORIZED);
        }

        MultiValueMapConverter<TableEntity> converter = new MultiValueMapConverter<TableEntity>(attributes, TableEntity.class);

        if(isSuperAdmin(admin)){
            return tableService.findAll(converter.getData(), converter.getPageable());
        } else if (isCompanyAdmin(admin)) {
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

            if (adminAgencyCompanyOrganization != null) {
                List<Agency> agencies = agencyService.findByCompany(adminAgencyCompanyOrganization.getCompany().getId());
                if (agencies != null && agencies.size() > 0) {
                    List<Long> ids = agencies.stream().map(v -> v.getId()).collect(Collectors.toList());
                    return tableService.findByAgencyIds(ids, converter.getPageable());
                }
            }

        } else if (isAgencyAdmin(admin)) {
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

            if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
                return tableService.findByAgency(adminAgencyCompanyOrganization.getAgency().getId(), converter.getPageable());
            }
        }

        return null;
    }

    @ApiOperation(value = "get all Tables", notes = "get all Tables")
    @ApiParam(name = "id", value = "Table id", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/available")
    public List<TableEntity> getAllTablesAvailable(HttpServletRequest req) {
        //return tableService.finAllTablesAvailable();
        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

        if(isEmpty(admin)){
            throw new UserException(NOT_AUTHORIZED);
        }

        if(isSuperAdmin(admin)){
            return tableService.finAllTablesAvailable();
        } else if (isCompanyAdmin(admin)) {
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

            if (adminAgencyCompanyOrganization != null) {
                List<Agency> agencies = agencyService.findByCompany(adminAgencyCompanyOrganization.getCompany().getId());
                if (agencies != null && agencies.size() > 0) {
                    List<Long> ids = agencies.stream().map(v -> v.getId()).collect(Collectors.toList());
                    return tableService.finAllTablesAvailableByAgencyList(ids);
                }
            }

        } else if (isAgencyAdmin(admin)) {
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

            if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
                return tableService.findByAgencyId(adminAgencyCompanyOrganization.getAgency().getId());
            }
        }

        return new ArrayList<TableEntity>();
    }

    @ApiOperation(value = "update Table", notes = "update Table")
    @ApiParam(name = "table", value = "table", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @PutMapping
    public ResponseEntity<TableEntity> update(@RequestBody TableEntity table) {
        table = tableService.save(table);
        return  new ResponseEntity<TableEntity>(table,HttpStatus.OK);
    }

    @ApiOperation(value = "get all Seats", notes = "get all Seats")
    @ApiParam(name = "id", value = "Seat id", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/{tableId}/seats")
    public Page<Seat> findByTableId(@PathVariable Long tableId, @RequestParam MultiValueMap<String, String> attributes) {
        MultiValueMapConverter<TableEntity> converter = new MultiValueMapConverter<TableEntity>(attributes, TableEntity.class);
        return seatService.findByTable(tableId, converter.getPageable());
    }

    @ApiOperation(value = "get all Seats and user pin", notes = "get all Seats and user pin")
    @ApiParam(name = "id", value = "Seat id", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/{tableId}/seats/{agencyId}/{pin}")
    public Page<Seat> findByTableIdUserPin(@PathVariable Long tableId, @PathVariable Long agencyId, @PathVariable String pin, @RequestParam MultiValueMap<String, String> attributes) {
        //MultiValueMapConverter<TableEntity> converter = new MultiValueMapConverter<TableEntity>(attributes, TableEntity.class);
        List<Seat> seats = seatService.findByTableId(tableId);
        if (seats == null || seats.size() == 0) {
            return new PageImpl<Seat>(seats);
        }

        if (agencyId == null || pin == null || pin.isEmpty()) {
            return new PageImpl<Seat>(seats);
        }

        User user = userService.getUserByPIN(pin, agencyId);

        if (user == null) {
            return new PageImpl<Seat>(seats);
        }

        List<BlackListDTO> blacklistUsers = user.getBlacklistUser();
        if (blacklistUsers == null || blacklistUsers.size() == 0) {
            return new PageImpl<Seat>(seats);
        }

        // get all blacklist from user's seat
        boolean foundInBlackList = false;
        for (Seat seat: seats) {
            User takenUser = seat.getUser();
            if (takenUser != null) {
                List<BlackListDTO> takenUserBlacklist = takenUser.getBlacklistUser();
                if (takenUserBlacklist != null && takenUserBlacklist.size() > 0) {
                    for (BlackListDTO takenBlacklist: takenUserBlacklist) {
                        Long userTakenId = takenBlacklist.getId();
                        // found
                        if (userTakenId == user.getId()) {
                            foundInBlackList = true;
                            break;
                        }
                    }
                }
            }

            if (foundInBlackList) {
                break;
            }
        }

        // the user is already existed in the other's blacklist in the same table
        if (foundInBlackList) {
            for (Seat seat: seats) {
                seat.setStatus(SeatStatusEnum.UNAVAILABLE);
            }
            return new PageImpl<Seat>(seats);
        }

        // get black list is based on the user
        for (BlackListDTO dto : blacklistUsers) {
            Long userId = dto.getId();
            boolean isFoundBlacklist = false;
            for (Seat seat: seats) {
                if (seat.getUser() != null && userId == seat.getUser().getId()) {
                    isFoundBlacklist = true;
                    break;
                }
            }

            // found black list
            if (isFoundBlacklist) {
                for (Seat seat: seats) {
                    seat.setStatus(SeatStatusEnum.UNAVAILABLE);
                }
                break;
            }
        }

        return new PageImpl<Seat>(seats);
    }

    @ApiOperation(value = "get table by Id", notes = "get table by Id")
    @ApiParam(name = "id", value = "Table id", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/{id}")
    public TableEntity getTableById(@PathVariable("id") Long id) {
        return tableService.findById(id);
    }

    @ApiOperation(value = "delete table", notes = "delete table")
    @ApiParam(name = "id", value = "Table id", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        logger.info("id : " + id);
        tableService.deleteById(id);
    }
    
    
    @ApiOperation(value = "get tables arrangement by agency id", notes = "get tables arrangement by agency id")
    @ApiParam(name = "id", value = "agency id", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/arrangement/{agencyId}")
    public ResponseEntity<TableArrangementResponseDTO> getTableArrangementByAgency(@PathVariable Long agencyId) {
        return ResponseEntity.ok(tableService.getTableArrangementByAgency(agencyId));
    }
    
    

}
