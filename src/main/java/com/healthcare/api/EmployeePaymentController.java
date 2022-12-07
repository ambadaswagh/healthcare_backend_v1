package com.healthcare.api;

import com.healthcare.api.auth.AbstractBasedAPI;
import com.healthcare.dto.EmployeeRequestDTO;
import com.healthcare.exception.UserException;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.AdminAgencyCompanyOrganization;
import com.healthcare.model.entity.Employee;
import com.healthcare.model.entity.EmployeePayment;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.AdminAgencyCompanyOrganizationService;
import com.healthcare.service.EmployeePaymentService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.joda.time.DateTime;
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

import static com.healthcare.api.common.HealthcareConstants.AUTHENTICATED_ADMIN;
import static com.healthcare.api.common.HealthcareConstants.NOT_AUTHORIZED;
import static com.healthcare.api.common.HealthcareUtil.isEmpty;
import static com.healthcare.api.common.RoleUtil.isAgencyAdmin;
import static com.healthcare.api.common.RoleUtil.isCompanyAdmin;
import static com.healthcare.api.common.RoleUtil.isSuperAdmin;

@CrossOrigin
@RestController(value = "EmployeePaymentRestAPI")
@RequestMapping(value = "/api/employee/payment")
public class EmployeePaymentController extends AbstractBasedAPI {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EmployeePaymentService employeePaymentService;

    @Autowired
    private AdminAgencyCompanyOrganizationService adminAgencyCompanyOrganizationService;

    @ApiOperation(value = "save employee payment", notes = "save employee payment")
    @ApiParam(name = "employee payment", value = "employee payment to save", required = true)
    @PostMapping()
    public ResponseEntity<EmployeePayment> create(@RequestBody EmployeePayment employeePayment) {
        employeePayment = employeePaymentService.save(employeePayment);
        return new ResponseEntity<EmployeePayment>(employeePayment, HttpStatus.OK);
    }

    @ApiOperation(value = "get all employee payment", notes = "get all employee payment")
    @GetMapping()
    public ResponseEntity<Page<EmployeePayment>> readAll(HttpServletRequest req, @RequestParam MultiValueMap<String, String> attributes) {
        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

        if(isEmpty(admin)){
            throw new UserException(NOT_AUTHORIZED);
        }

        EmployeeRequestDTO dto = new EmployeeRequestDTO().fromMap(attributes);
        MultiValueMapConverter<EmployeePayment> converter = new MultiValueMapConverter<>(attributes, EmployeePayment.class);

        if(isSuperAdmin(admin)){
            Page<EmployeePayment> employeePPage = employeePaymentService.calculatePaymentByTimeRange(null, null, null, new DateTime(dto.getFromDate()).toDate(), new DateTime(dto.getToDate()).toDate(), converter.getPageable());
            return ResponseEntity.ok(employeePPage);
        } else if (isCompanyAdmin(admin)) {
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
            if (adminAgencyCompanyOrganization != null) {
                return ResponseEntity.ok(employeePaymentService.calculatePaymentByTimeRange(adminAgencyCompanyOrganization.getCompany().getId(),
                        null, null, new DateTime(dto.getFromDate()).toDate(), new DateTime(dto.getToDate()).toDate(), converter.getPageable()));
            }

        } else if (isAgencyAdmin(admin)) {
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
            if (adminAgencyCompanyOrganization != null) {
                return ResponseEntity.ok(employeePaymentService.calculatePaymentByTimeRange(null,
                        adminAgencyCompanyOrganization.getAgency().getId(), null,
                        new DateTime(dto.getFromDate()).toDate(), new DateTime(dto.getToDate()).toDate(), converter.getPageable()));
            }
        }

        return ResponseEntity.ok(new PageImpl<EmployeePayment>(new ArrayList<EmployeePayment>()));
    }

    @ApiOperation(value = "get employee payment by id", notes = "get employee payment by id")
    @ApiImplicitParam(name = "id", value = "employee payment id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/{id}")
    public EmployeePayment read(@PathVariable Long id) {
        logger.info("id : " + id);
        return employeePaymentService.findById(id);
    }

    @ApiOperation(value = "update employee payment", notes = "update employee payment")
    @ApiParam(name = "employee payment", value = "employee payment", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @PutMapping
    public ResponseEntity<EmployeePayment> update(@RequestBody EmployeePayment employeePayment) {
        employeePayment = employeePaymentService.save(employeePayment);
        return  new ResponseEntity<EmployeePayment>(employeePayment,HttpStatus.OK);
    }

    @ApiOperation(value = "get all employee payment by employee", notes = "get all employee payment employee")
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Page<EmployeePayment>> findByEmployee(HttpServletRequest req, @PathVariable("employeeId") Long employeeId,
                                                                @RequestParam MultiValueMap<String, String> attributes) {
        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

        if(isEmpty(admin)){
            throw new UserException(NOT_AUTHORIZED);
        }

        EmployeeRequestDTO dto = new EmployeeRequestDTO().fromMap(attributes);
        MultiValueMapConverter<EmployeePayment> converter = new MultiValueMapConverter<>(attributes, EmployeePayment.class);

        return ResponseEntity.ok(employeePaymentService.calculatePaymentByTimeRange(null, null, employeeId, new DateTime(dto.getFromDate()).toDate(), new DateTime(dto.getToDate()).toDate(), converter.getPageable()));
    }

    @ApiOperation(value = "get all employee payment by agency", notes = "get all employee payment by agency")
    @GetMapping("/agency/{agencyId}")
    public ResponseEntity<Page<EmployeePayment>> findByEmployeeByAgency(HttpServletRequest req, @PathVariable("agencyId") Long agencyId,
                                                                @RequestParam MultiValueMap<String, String> attributes) {
        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

        if(isEmpty(admin)){
            throw new UserException(NOT_AUTHORIZED);
        }

        EmployeeRequestDTO dto = new EmployeeRequestDTO().fromMap(attributes);
        MultiValueMapConverter<EmployeePayment> converter = new MultiValueMapConverter<>(attributes, EmployeePayment.class);

        return ResponseEntity.ok(employeePaymentService.calculatePaymentByTimeRange(null, agencyId, null, new DateTime(dto.getFromDate()).toDate(), new DateTime(dto.getToDate()).toDate(),converter.getPageable()));
    }

    @ApiOperation(value = "get all employee payment by company", notes = "get all employee payment by company")
    @GetMapping("/company/{companyId}")
    public ResponseEntity<Page<EmployeePayment>> findByEmployeeByCompany(HttpServletRequest req, @PathVariable("companyId") Long companyId,
                                                                        @RequestParam MultiValueMap<String, String> attributes) {
        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

        if(isEmpty(admin)){
            throw new UserException(NOT_AUTHORIZED);
        }

        EmployeeRequestDTO dto = new EmployeeRequestDTO().fromMap(attributes);
        MultiValueMapConverter<EmployeePayment> converter = new MultiValueMapConverter<>(attributes, EmployeePayment.class);

        return ResponseEntity.ok(employeePaymentService.calculatePaymentByTimeRange(companyId, null, null, new DateTime(dto.getFromDate()).toDate(), new DateTime(dto.getToDate()).toDate(),converter.getPageable()));
    }

    @ApiOperation(value = "get all employee payment by employee", notes = "get all employee payment employee")
    @GetMapping("/list/employee/{employeeId}")
    public ResponseEntity<Page<EmployeePayment>> findListByEmployee(HttpServletRequest req, @PathVariable("employeeId") Long employeeId,
                                                                @RequestParam MultiValueMap<String, String> attributes) {
        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

        if(isEmpty(admin)){
            throw new UserException(NOT_AUTHORIZED);
        }

        EmployeeRequestDTO dto = new EmployeeRequestDTO().fromMap(attributes);
        MultiValueMapConverter<EmployeePayment> converter = new MultiValueMapConverter<>(attributes, EmployeePayment.class);

        return ResponseEntity.ok(employeePaymentService.findPaymentByEmployee(employeeId, new DateTime(dto.getFromDate()).toDate(), new DateTime(dto.getToDate()).toDate(), converter.getPageable()));
    }
}
