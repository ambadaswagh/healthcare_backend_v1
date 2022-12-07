package com.healthcare.service;

import java.util.List;

import com.healthcare.dto.LeaveStatus;
import com.healthcare.model.entity.Leave;

public interface LeaveService extends IService<Leave> {
	public List<Leave> findAll();
	public List<Leave> findByEmployeeId(Long id);
	public List<LeaveStatus> getStatusByEmployee(Long employeeId);
}
