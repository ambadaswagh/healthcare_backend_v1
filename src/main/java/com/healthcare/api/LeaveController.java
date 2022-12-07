package com.healthcare.api;

import java.util.List;
import java.util.Objects;

import com.healthcare.container.LeaveUpdateRequest;
import com.healthcare.model.entity.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.api.auth.AbstractBasedAPI;
import com.healthcare.api.common.HealthcareConstants;
import com.healthcare.api.common.HealthcareUtil;
import com.healthcare.dto.EmployeeLeaveBatchDTO;
import com.healthcare.dto.LeaveStatus;
import com.healthcare.model.entity.Leave;
import com.healthcare.service.EmployeeService;
import com.healthcare.service.LeaveService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 */
@CrossOrigin
@RestController(value = "LeaveRestAPI")
@RequestMapping(value = "/api/leave")
public class LeaveController extends AbstractBasedAPI {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private LeaveService employeeLeaveService;

	@Autowired
	private EmployeeService employeeService;

	@ApiOperation(value = "save employee leaves", notes = "save employee leaves")
	@ApiParam(name = "employeeLeaveBatchDTO", value = "employee leaves to save", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@PutMapping("/batch_save")
	public EmployeeLeaveBatchDTO create(@RequestBody EmployeeLeaveBatchDTO employeeLeaveBatchDTO) {

		employeeLeaveBatchDTO.setStatus(HealthcareConstants.SUCCESS);

		for (Leave employeeLeave : employeeLeaveBatchDTO.getEmployeeLeaveList()) {
			try {
				employeeLeave = employeeLeaveService.save(employeeLeave);
			} catch (Exception e) {
				employeeLeaveBatchDTO.setStatus(HealthcareConstants.FAIL);
				employeeLeave.setErrorMessage(e.getMessage());
			}
		}

		return employeeLeaveBatchDTO;
	}

	@ApiOperation(value = "get employee leaves", notes = "get employee leaves")
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@GetMapping
	public EmployeeLeaveBatchDTO getAll() {
		List<Leave> employeeLeaves = employeeLeaveService.findAll();
		return new EmployeeLeaveBatchDTO(HealthcareConstants.SUCCESS, employeeLeaves);
	}

	@ApiOperation(value = "get employee leave", notes = "get employee leave")
	@ApiParam(name = "id", value = "employee id", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@GetMapping("/{id}")
	public Leave getEmployeeLeave(@PathVariable("id") Long id) {
		List<Leave> employeeLeaves = employeeLeaveService.findByEmployeeId(id);
		for (Leave l : employeeLeaves) {
			if (l.getYear() == HealthcareUtil.getCurrentYear()) {
				return l;
			}
		}

		return null;
	}

	@ApiOperation(value = "get employee leave status", notes = "get employee leave status")
	@ApiParam(name = "id", value = "employee id", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@GetMapping("/status/{id}")
	public List<LeaveStatus> getEmployeeLeaveStatus(@PathVariable("id") Long id) {
		return employeeLeaveService.getStatusByEmployee(id);
	}

	@ApiOperation(value = "save employee leave", notes = "save employee leave")
	@ApiParam(name = "employeeLeave", value = "employeeLeave", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@PutMapping
	public Leave saveEmployeeLeaves(@RequestBody Leave employeeLeave) {
		employeeLeave = employeeLeaveService.save(employeeLeave);
		return employeeLeave;
	}

	@ApiOperation(value = "save employee leave", notes = "save employee leave")
	@ApiParam(name = "employeeLeave", value = "employeeLeave", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@PostMapping
	public Leave addEmployeeLeaves(@RequestBody LeaveUpdateRequest employeeLeaveReq) {
    List<Leave> employeeLeaves = employeeLeaveService.findByEmployeeId(employeeLeaveReq.employee_id);
    for (Leave leave : employeeLeaves) {
      if(Objects.equals(leave.getId(), employeeLeaveReq.id)){
        leave.setSickDaysUsed(employeeLeaveReq.sickDaysUsed);
        leave.setAskForLeaveDaysLimitation(employeeLeaveReq.askForLeaveDaysLimitation);
        leave.setAskForLeaveDaysUsed(employeeLeaveReq.askForLeaveDaysUsed);
        leave.setSickDaysLimitation(employeeLeaveReq.sickDaysLimitation);
        leave.setVacationDaysLimitation(employeeLeaveReq.vacationDaysLimitation);
        leave.setVacationDaysUsed(employeeLeaveReq.vacationDaysUsed);
        return employeeLeaveService.save(leave);
      }
    }
    return null;
	}
}
