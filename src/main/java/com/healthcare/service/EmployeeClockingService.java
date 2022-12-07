package com.healthcare.service;

import com.healthcare.api.model.EmployeeClockingReportRequest;
import com.healthcare.api.model.EmployeeClockingReportResponse;
import com.healthcare.api.model.TimeClockRequest;
import com.healthcare.model.entity.Employee;
import com.healthcare.model.entity.EmployeeClocking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeClockingService extends IService<EmployeeClocking>, IFinder<EmployeeClocking> {
	
	public EmployeeClockingReportResponse generateEmployeeClockingReport(EmployeeClockingReportRequest employeeClockingReportRequest);
	
	public Employee timeClock(TimeClockRequest clockRequest);

	Page<EmployeeClocking> findByEmployeeId(Long employeeId, Pageable pageable);

	Page<EmployeeClocking> findByeEmployeeIds(List<Long> employeeIds, Pageable pageable);
	
	Page<EmployeeClocking> findAllEmployeeByRange(Long employeeId, String startDate, String endDate, Pageable pageable);


}