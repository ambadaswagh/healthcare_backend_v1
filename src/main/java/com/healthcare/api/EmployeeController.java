package com.healthcare.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.healthcare.api.common.HealthcareUtil;
import com.healthcare.api.model.ExpressEmployeeRequestDTO;
import com.healthcare.exception.UserException;
import com.healthcare.model.entity.*;
import com.healthcare.repository.EmployeeRepository;
import com.healthcare.service.AdminAgencyCompanyOrganizationService;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import com.healthcare.api.auth.AbstractBasedAPI;
import com.healthcare.api.model.EmployeeClockingReportRequest;
import com.healthcare.api.model.EmployeeClockingReportResponse;
import com.healthcare.api.model.TimeClockRequest;
import com.healthcare.dto.EmployeeRequestDTO;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.EmployeeClockingService;
import com.healthcare.service.EmployeeService;
import com.healthcare.util.DateUtils;
import com.healthcare.validator.SaveEmployeeValidator;
import com.healthcare.validator.UpdateEmployeeValidator;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletRequest;

import static com.healthcare.api.common.HealthcareConstants.AUTHENTICATED_ADMIN;
import static com.healthcare.api.common.HealthcareConstants.NOT_AUTHORIZED;
import static com.healthcare.api.common.HealthcareUtil.isEmpty;
import static com.healthcare.api.common.RoleUtil.isAgencyAdmin;
import static com.healthcare.api.common.RoleUtil.isCompanyAdmin;
import static com.healthcare.api.common.RoleUtil.isSuperAdmin;

/**
 * Created by Jean Antunes on 11/05/17.
 */
@CrossOrigin
@RestController(value = "EmployeeRestAPI")
@RequestMapping(value = "/api/employee")
public class EmployeeController extends AbstractBasedAPI {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EmployeeService employeeService;

	@Autowired 
	private SaveEmployeeValidator saveEmployeeValidator;
	
	@Autowired 
	private UpdateEmployeeValidator updateEmployeeValidator;
	
	@Autowired
	private EmployeeClockingService employeeClockingService;

	@Autowired
	AdminAgencyCompanyOrganizationService adminAgencyCompanyOrganizationService;

	@Autowired
	EmployeeRepository employeeRepository;

	@ApiOperation(value = "save employee", notes = "save employee")
	@ApiParam(name = "employee", value = "employee to save", required = true)
	@PostMapping()
	public ResponseEntity<Employee> create(@RequestBody Employee employee) {
		DataBinder binder = new DataBinder(employee);
		binder.setValidator(saveEmployeeValidator);
		binder.validate();
		BindingResult result = binder.getBindingResult();
		if (result.hasErrors()) {
			return new ResponseEntity(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		employee = employeeService.save(employee);
		return new ResponseEntity<Employee>(employee, HttpStatus.OK);
	}

	@ApiOperation(value = "check pin", notes = "check pin")
	@ApiParam(name = "employee", value = "check pin validity", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@PostMapping("/pin/{pin}")
	public ResponseEntity<Boolean> checkPinValidity(@PathVariable String pin) {
		boolean isValid = employeeService.validPin(pin);
		return new ResponseEntity<Boolean>(isValid, HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "generate pin", notes = "generate pin")
	@ApiParam(name = "employee", value = "generate pin", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@GetMapping("/pin/")
	public ResponseEntity<String> generatePin() {
		String pin = employeeService.generatePin();
		return new ResponseEntity<String>(pin, HttpStatus.OK);
	} 
	
	
	@ApiOperation(value = "get employee by id", notes = "get employee by id")
	@ApiImplicitParam(name = "id", value = "employee id", required = true, dataType = "Long", paramType = "path")
	@GetMapping("/{id}")
	public Employee read(@PathVariable Long id) {
		logger.info("id : " + id);
		return employeeService.findById(id);
	}

	@ApiOperation(value = "get employee by id", notes = "get employee by id")
	@ApiImplicitParam(name = "id", value = "employee id", required = true, dataType = "Long", paramType = "path")
	@GetMapping("/work-hour/{id}")
	public List<EmployeeClocking> detail(@PathVariable Long id) {
		return employeeService.getTotalWorkingTimeThisWeek(id);
	}

	@ApiOperation(value = "get all employee", notes = "get all employee")
	@GetMapping()
	public ResponseEntity<Page<Employee>> readAll(HttpServletRequest req, @RequestParam MultiValueMap<String, String> attributes) {
		/*MultiValueMapConverter<Employee> converter = new MultiValueMapConverter<>(attributes, Employee.class);
        Page<Employee> employeePage = employeeService.findAll(converter.getData(), converter.getPageable());
        employeeService.getEmployeesPunchStatus(employeePage.getContent());
        return ResponseEntity.ok(employeePage);*/

		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if(isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}

		MultiValueMapConverter<Employee> converter = new MultiValueMapConverter<>(attributes, Employee.class);

		if(isSuperAdmin(admin)){
			Page<Employee> employeePage = employeeService.findAll(converter.getData(), converter.getPageable());
			employeeService.getEmployeesPunchStatus(employeePage.getContent());
			return ResponseEntity.ok(employeePage);
		} else if (isCompanyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null) {
				Page<Employee> employeePage = employeeService.findByCompany(adminAgencyCompanyOrganization.getCompany().getId(), converter.getPageable());
				return ResponseEntity.ok(employeePage);
			}

		} else if (isAgencyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null) {
				Page<Employee> employeePage = employeeService.findByCpmAndAgency(adminAgencyCompanyOrganization.getCompany().getId(),
															adminAgencyCompanyOrganization.getAgency().getId(), converter.getPageable());
				employeeService.getEmployeesPunchStatus(employeePage.getContent());
				return ResponseEntity.ok(employeePage);
			}
		}

		return ResponseEntity.ok(new PageImpl<Employee>(new ArrayList<Employee>()));
	}

	@ApiOperation(value = "get all employee", notes = "get all employee")
	@GetMapping("/getAllEmployeeList")
	public ResponseEntity<List<Employee>> getAllEmployeeList(HttpServletRequest req) {
		/*MultiValueMapConverter<Employee> converter = new MultiValueMapConverter<>(attributes, Employee.class);
        Page<Employee> employeePage = employeeService.findAll(converter.getData(), converter.getPageable());
        employeeService.getEmployeesPunchStatus(employeePage.getContent());
        return ResponseEntity.ok(employeePage);*/

		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if(isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}

		if(isSuperAdmin(admin)){
			List<Employee> employeeList = employeeRepository.findAll();
			employeeService.getEmployeesPunchStatus(employeeList);
			return ResponseEntity.ok(employeeList);
		} else if (isCompanyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null) {
				return ResponseEntity.ok(employeeService.findByCompany(adminAgencyCompanyOrganization.getCompany().getId()));
			}

		} else if (isAgencyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null) {
				return ResponseEntity.ok(
						employeeService.findByCpmAndAgencyList(
								adminAgencyCompanyOrganization.getCompany().getId(),
								adminAgencyCompanyOrganization.getAgency().getId()));
			}
		}

		return ResponseEntity.ok((new ArrayList<Employee>()));
	}

	@ApiOperation(value = "update employee", notes = "update employee")
	@ApiParam(name = "employee", value = "employee to update", required = true)
	@PutMapping()
	public ResponseEntity<Employee> update(@RequestBody Employee employee) {
		DataBinder binder = new DataBinder(employee);
		binder.setValidator(updateEmployeeValidator);
		binder.validate();
		BindingResult result = binder.getBindingResult();
		if (result.hasErrors()) {
			return new ResponseEntity(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		employee = employeeService.save(employee);
		return new ResponseEntity<Employee>(employee, HttpStatus.OK);
	}

	@ApiOperation(value = "delete employee", notes = "delete employee")
	@ApiImplicitParam(name = "id", value = "employee id", required = true, dataType = "Long")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		employeeService.deleteById(id);
	}

	@ApiOperation(value = "disable employee", notes = "disable employee")
	@ApiImplicitParam(name = "id", value = "employee id", required = true, dataType = "Long")
	@PutMapping("/{id}/disable")
	public void disable(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		employeeService.disableById(id);
	}

	@ApiOperation(value = "employee check in/out", notes = "employee check in/out")
	@ApiImplicitParam(name = "TimeClockRequest", value = "TimeClockRequest", required = true)
	@PostMapping("/timeclock")
	public ResponseEntity<Employee> timeClock(@RequestBody TimeClockRequest timeClockRequest) {
		Employee employee = employeeClockingService.timeClock(timeClockRequest);
		return new ResponseEntity<Employee>(employee, HttpStatus.OK);
	}
	
    @ApiOperation(value = "employee punch", notes = "employee punch")
    @ApiParam(name = "employee", value = "employee punch to save", required = true)
    @PostMapping("/punch")
    public ResponseEntity<Employee> punch(@RequestBody Employee employee) {
        Employee dbEmp = employeeService.findById(employee.getId());
       if (!dbEmp.getPin().equals(employee.getPin())) {
           return new ResponseEntity(employee, HttpStatus.NOT_ACCEPTABLE);
       }
       dbEmp.setRulesAndRegusDocId(employee.getRulesAndRegusDocId());
       dbEmp = employeeService.savePunch(dbEmp);
       return new ResponseEntity<Employee>(dbEmp, HttpStatus.OK);
    }

	@ApiOperation(value = "get employee report", notes = "get employee report")
	@ApiImplicitParam(name = "hours", value = "difference hours, it is default is 8", required = true)
	@PostMapping("/report")
	public ResponseEntity<EmployeeClockingReportResponse> generateReport(
			@RequestBody EmployeeClockingReportRequest EmployeeClockingReportRequest) {
		EmployeeClockingReportResponse response = employeeClockingService
				.generateEmployeeClockingReport(EmployeeClockingReportRequest);
		return new ResponseEntity<EmployeeClockingReportResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "get employee working overtime", notes = "get employee working overtime")
	@ApiImplicitParam(name = "EmployeeClockingReportRequest", value = "EmployeeClockingReportRequest", required = true)
	@RequestMapping(value = "/worked/overtime", method = RequestMethod.GET)
	public ResponseEntity<?> getEmployeeWorkedOverTime(HttpServletRequest req, @RequestParam MultiValueMap<String, String> attributes) {

		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if(isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}

		MultiValueMapConverter<Employee> converter = new MultiValueMapConverter<>(attributes, Employee.class);


		List<Employee> employees = employeeService.findEmployeesOvertime(admin);
		if (employees == null) {
			return ResponseEntity.ok(new PageImpl<Employee>(new ArrayList<>()));
		}

		return ResponseEntity.ok(new PageImpl<Employee>(employees, converter.getPageable(), employees.size()));
	}
	
	@ApiOperation(value = "get employee by pin", notes = "get employee by pin")
	@ApiParam(name = "employee", value = "check pin validity", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@GetMapping("/inputpin/{pin}")
	public Employee getEmployeeByPin(@PathVariable String pin) {
		System.out.println("InController Pin"+pin);
		Employee employee = employeeService.getEmployeeByPin(pin);
		employee = employeeService.getEmployeesPunchStatus(employee);
		System.out.println("Value"+employee.getFirstName());
		return employee;
	}

	@ApiOperation(value = "get all employee clocking", notes = "get all employee clocking")
	@GetMapping("/clocking-all")
	public ResponseEntity<Page<EmployeeClocking>> readAllEmployeeClocking(HttpServletRequest req, @RequestParam MultiValueMap<String, String> attributes) {
		/*MultiValueMapConverter<EmployeeClocking> converter = new MultiValueMapConverter<>(attributes, EmployeeClocking.class);
		Page<EmployeeClocking> employeeClockingPage = employeeClockingService.findAll(converter.getPageable());
		return ResponseEntity.ok(employeeClockingPage);*/


		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if(isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}

		MultiValueMapConverter<EmployeeClocking> converter = new MultiValueMapConverter<>(attributes, EmployeeClocking.class);

		if(isSuperAdmin(admin)){
			Page<EmployeeClocking> employeeClockingPage = employeeClockingService.findAll(converter.getPageable());
			return ResponseEntity.ok(employeeClockingPage);
		} else if (isCompanyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null) {
				List<Employee> employees = employeeService.findByCompany(adminAgencyCompanyOrganization.getCompany().getId());
				if (employees != null) {
					List<Long> employeeIds = employees.stream().map(v -> v.getId()).collect(Collectors.toList());
					Page<EmployeeClocking> employeeClockingPage = employeeClockingService.findByeEmployeeIds(employeeIds, converter.getPageable());
					return ResponseEntity.ok(employeeClockingPage);
				}
			}

		} else if (isAgencyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

			if (adminAgencyCompanyOrganization != null) {
				List<Employee> employees = employeeService.findByCampanyIdAndAgencyId(adminAgencyCompanyOrganization.getCompany().getId(),
						adminAgencyCompanyOrganization.getAgency().getId());
				if (employees != null) {
					List<Long> employeeIds = employees.stream().map(v -> v.getId()).collect(Collectors.toList());
					Page<EmployeeClocking> employeeClockingPage = employeeClockingService.findByeEmployeeIds(employeeIds, converter.getPageable());
					String timezone = DateUtils.get_timezone(adminAgencyCompanyOrganization.getAgency().getTimezone());
					employeeClockingPage = get_local_time(employeeClockingPage.getContent(), timezone);
					return ResponseEntity.ok(employeeClockingPage);
				}
			}
		}

		return ResponseEntity.ok(new PageImpl<EmployeeClocking>(new ArrayList<EmployeeClocking>()));
	}
	

	@ApiOperation(value = "get all employee clocking by range", notes = "get all employee clocking by date")
	@GetMapping("/clocking-all-range")
	public ResponseEntity<Page<EmployeeClocking>> readAllEmployeeClockingByDate(HttpServletRequest req, @RequestParam MultiValueMap<String, String> attributes) {

		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if(isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}
		
		EmployeeRequestDTO dto = new EmployeeRequestDTO().fromMap(attributes);
		MultiValueMapConverter<EmployeeClocking> converter = new MultiValueMapConverter<>(attributes, EmployeeClocking.class);
		return ResponseEntity.ok(employeeClockingService.findAllEmployeeByRange(dto.getEmployeeId(), dto.getFromDate(), dto.getToDate(), converter.getPageable()));
		
    }


	@ApiOperation(value = "get employee clocking", notes = "get employee clocking")
	@GetMapping("/clocking/{id}")
	public EmployeeClocking readClocking(HttpServletRequest req, @PathVariable Long id) {
		logger.info("id : " + id);
		EmployeeClocking employeeClocking = employeeClockingService.findById(id);
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
		if (!isEmpty(admin) && isAgencyAdmin(admin)) {
			AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());
			if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
				String timezone = DateUtils.get_timezone(adminAgencyCompanyOrganization.getAgency().getTimezone());
				employeeClocking = get_object_with_local_times(employeeClocking, timezone);
			}
		}
		return employeeClocking;
	}

	@ApiOperation(value = "update employee clocking", notes = "update employee clocking")
	@ApiParam(name = "employee clocking", value = "employee clocking to update", required = true)
	@PutMapping("/clocking")
	public ResponseEntity<EmployeeClocking> update(@RequestBody EmployeeClocking employeeClocking) {
		DataBinder binder = new DataBinder(employeeClocking);
		BindingResult result = binder.getBindingResult();
		if (result.hasErrors()) {
			return new ResponseEntity(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		employeeClocking = employeeClockingService.save(employeeClocking);
		return new ResponseEntity<EmployeeClocking>(employeeClocking, HttpStatus.OK);
	}

	@ApiOperation(value = "List employee have birthday next few days", notes = "Default will be 7 days")
	@ApiImplicitParam(name = "days", value = "how many next day. Default value is 7", dataType = "Integer" ,paramType = "path")
	@RequestMapping(value = "/birthday/feature/{days}", method = RequestMethod.GET)
	public ResponseEntity<?> getAuthorizationEmployeeBirthdayFeature(HttpServletRequest req, @PathVariable Integer days, @RequestParam MultiValueMap<String, String> attributes) {
		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
		if (HealthcareUtil.isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}
		Integer realDay = days == null ? 0 : days;
		if (realDay < 0) {
			return ResponseEntity.badRequest().body("days must be larger than 0");
		}

		MultiValueMapConverter<Employee> converter = new MultiValueMapConverter<>(attributes, Employee.class);
		return ResponseEntity.ok(employeeService.getAuthorizationEmployeeBornFromDateToDate(realDay, admin, converter.getPageable()));

	}
	@ApiOperation(value = "get all employee by company id", notes = "get all employee by company id")
	@GetMapping("/company/{companyId}")
	public ResponseEntity<Page<Employee>> findAllEmployeeByCompany(@RequestParam MultiValueMap<String, String> attributes, @PathVariable("companyId") Long companyId) {
		MultiValueMapConverter<Employee> converter = new MultiValueMapConverter<>(attributes, Employee.class);

		return ResponseEntity.ok(employeeService.findByCompany(companyId, converter.getPageable()));
	}

	@ApiOperation(value = "get all employee by company id and agency id", notes = "get all employee by company id and agency id")
	@GetMapping("/company/{companyId}/agency/{agencyId}")
	public ResponseEntity<Page<Employee>> findAllEmployeeByCompanyAndAgency(@RequestParam MultiValueMap<String, String> attributes,
																	@PathVariable("companyId") Long companyId,
																	@PathVariable("agencyId") Long agencyId) {
		MultiValueMapConverter<Employee> converter = new MultiValueMapConverter<>(attributes, Employee.class);

		return ResponseEntity.ok(employeeService.findByCpmAndAgency(companyId, agencyId, converter.getPageable()));
	}

	@ApiOperation(value = "List employee have vacation next few days", notes = "Default will be 7 days")
	@ApiImplicitParam(name = "days", value = "how many next day. Default value is 7", dataType = "Integer" ,paramType = "path")
	@RequestMapping(value = "/vacation/feature/{days}", method = RequestMethod.GET)
	public ResponseEntity<?> getVacationEmployeeExpiring(HttpServletRequest req, @PathVariable Integer days, @RequestParam MultiValueMap<String, String> attributes) {

		Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

		if(isEmpty(admin)){
			throw new UserException(NOT_AUTHORIZED);
		}


		Integer realDay = days == null ? 0 : days;
		if(realDay < 0){
			return ResponseEntity.badRequest().body("days must be larger than 0");
		}

		MultiValueMapConverter<Employee> converter = new MultiValueMapConverter<>(attributes, Employee.class);
		return ResponseEntity.ok(employeeService.getVacationEmployeeReminder(days, admin, converter.getPageable()));
	}
	
	public Page<EmployeeClocking> get_local_time(List<EmployeeClocking> employees, String target_time_zone){
		for(EmployeeClocking employee : employees){
			employee = get_object_with_local_times(employee, target_time_zone);
		}
				
		return new PageImpl<>(employees); 
	}
	
	public EmployeeClocking get_object_with_local_times(EmployeeClocking employee, String target_time_zone){
		if(employee.getCheckInTime() != null){
			System.err.println(employee.getCheckInTime());
			employee.setCheckInTime(DateUtils.get_local_date_time(employee.getCheckInTime(), target_time_zone));
			System.err.println(employee.getCheckInTime());
		}
		if(employee.getCheckOutTime() != null){
			
			employee.setCheckOutTime(DateUtils.get_local_date_time(employee.getCheckOutTime(), target_time_zone));
		}
		return employee;
	}

	@ApiOperation(value = "express check in", notes = "express check in")
	@ApiParam(name = "ExpressEmployeeRequestDTO", value = "Pin", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@RequestMapping(value = "/mobile/check-in-out", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> mobileCheckInOut(@RequestBody ExpressEmployeeRequestDTO expressEmployeeRequestDTO) {

		JSONObject entity = new JSONObject();
		try {
			Employee employee = new Employee();


			String pin = getDigitPin(expressEmployeeRequestDTO.getPin());
			if (pin == null) {
				return throughError(entity);
			}
			employee = employeeRepository.findByPin(pin);
			expressEmployeeRequestDTO = employeeService.expressCheckin(expressEmployeeRequestDTO, employee);
			entity.put("result", "SUCCESS");
			entity.put("data", expressEmployeeRequestDTO);
			entity.put("message", "");
			return new ResponseEntity<JSONObject>(entity, HttpStatus.OK);
		} catch (Exception e) {
			return throughError(entity);
		}
	}

	private ResponseEntity<JSONObject> throughError(JSONObject entity) {
		entity.put("result", "ERROR");
		entity.put("data", new JSONObject());
		entity.put("message", "Invalid Input");
		return new ResponseEntity<JSONObject>(entity, HttpStatus.BAD_REQUEST);
	}

	private String getDigitPin(String pin) {
		if (pin == null || pin.isEmpty())
			return null;
		// This is to handle online pinOrBarcode which in format PIN-Firstname
		// lastname
		if (pin.contains("-")) {
			return pin.substring(0, pin.indexOf("-"));
		}

		return pin;
	}
}
