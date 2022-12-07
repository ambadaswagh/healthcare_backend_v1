package com.healthcare.service.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.api.model.EmployeeClockingReportRequest;
import com.healthcare.api.model.EmployeeClockingReportResponse;
import com.healthcare.api.model.TimeClockRequest;
import com.healthcare.model.entity.Employee;
import com.healthcare.model.entity.EmployeeClocking;
import com.healthcare.repository.EmployeeClockingRepository;
import com.healthcare.service.EmployeeClockingService;
import com.healthcare.service.EmployeeService;

@Service
@Transactional
public class EmployeeClockingServiceImpl extends BasicService<EmployeeClocking, EmployeeClockingRepository> implements EmployeeClockingService {
	private static final String KEY = EmployeeClocking.class.getSimpleName();

	@Autowired
	EmployeeClockingRepository employeeClockingRepository;

	@Autowired
	EmployeeService employeeService;

	@Autowired
	private RedisTemplate<String, EmployeeClocking> employeeClockingRedisTemplate;

	@Override @Transactional
	public EmployeeClocking save(EmployeeClocking employeeClocking) {
		employeeClocking = employeeClockingRepository.save(employeeClocking);
		employeeClockingRedisTemplate.opsForHash().put(KEY, employeeClocking.getId(), employeeClocking);
		return employeeClocking;
	}

	@Override @Transactional
	public Long deleteById(Long id) {
		employeeClockingRepository.delete(id);
		return employeeClockingRedisTemplate.opsForHash().delete(KEY, id);
	}

	@Override @Transactional
	public EmployeeClocking findById(Long id) {
		if (employeeClockingRedisTemplate.opsForHash().hasKey(KEY, id)) {
			return (EmployeeClocking) employeeClockingRedisTemplate.opsForHash().get(KEY, id);
		}
		return employeeClockingRepository.findOne(id);
	}

	@Override @Transactional
    public Employee timeClock(TimeClockRequest clockRequest) {
        EmployeeClocking employeeClocking = employeeClockingRepository.getLastClockingByEmployee(clockRequest.getEmployeeId());
        employeeClocking.setCheckOutTime(new Timestamp(new Date().getTime()));
        employeeClocking.setUpdatedAt(new Timestamp(new Date().getTime()));
        employeeClocking.setCheckOutSignatureId(clockRequest.getDocSignature());
        this.save(employeeClocking);
        
        Employee employee = employeeClocking.getEmployee();
        String weeklyHoursWorked = employee.getWeeklyHoursWorked();
        int hours = Integer.valueOf(weeklyHoursWorked.substring(0, weeklyHoursWorked.indexOf('h')));
        int minutes = Integer.valueOf(weeklyHoursWorked.substring(weeklyHoursWorked.indexOf('h') + 1, weeklyHoursWorked.length() - 1));
        long diff = employeeClocking.getCheckOutTime().getTime() - employeeClocking.getCheckInTime().getTime();
        hours += diff / 3600000;
        minutes += (diff % 3600000) / 60000;
        employee.setWeeklyHoursWorked(hours + "h" + minutes + "m");
        employee = employeeService.save(employee);

        return employee;
    }

	@Override @Transactional
	public EmployeeClockingReportResponse generateEmployeeClockingReport(
			EmployeeClockingReportRequest employeeClockingReportRequest) {
		List<EmployeeClocking> employeeClockingList = employeeClockingRepository.generateReport(
				employeeClockingReportRequest.getStartDate(), employeeClockingReportRequest.getEndDate(),
				employeeClockingReportRequest.getEmployeeId());
		EmployeeClockingReportResponse employeeClockingReportResponse = new EmployeeClockingReportResponse();
		employeeClockingReportResponse.setEmployeeClocking(employeeClockingList);
		int hours = 0, minutes = 0;
		if (employeeClockingList != null) {
			for(EmployeeClocking employeeClocking : employeeClockingList){
				long diff = employeeClocking.getCheckOutTime().getTime() - employeeClocking.getCheckInTime().getTime();
				hours += diff / 3600000;
				minutes += (diff % 3600000) / 60000;
			}
		}
		employeeClockingReportResponse.setTotalHours(hours + "h" + minutes + "m");
		return employeeClockingReportResponse;
	}

	@Override @Transactional
	public Long disableById(Long id) {
		//TODO
		return null;
	}

	@Override
	@Transactional
	public Page<EmployeeClocking> findByEmployeeId(Long employeeId, Pageable pageable) {
		return employeeClockingRepository.findByEmployeeId(employeeId, pageable);
	}

	@Override
	@Transactional
	public Page<EmployeeClocking> findByeEmployeeIds(List<Long> employeeIds, Pageable pageable) {
		return employeeClockingRepository.findByeEmployeeIds(employeeIds, pageable);
	}

	@Override
	public Page<EmployeeClocking> findAllEmployeeByRange(Long employeeId, String startDate, String endDate, Pageable pageable) {
	   
		DateTime today = new DateTime(startDate);
		DateTime endDay = new DateTime(endDate);
		if(employeeId !=null){
		    return employeeClockingRepository.findAllByStartDateEndDateEmployeeId(today.toDate(), endDay.toDate(),employeeId, pageable);	
		}else{
			return employeeClockingRepository.findAllByStartDateEndDate(today.toDate(), endDay.toDate(), pageable);
		}
	}
}
