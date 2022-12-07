package com.healthcare.api.model;

import java.io.Serializable;
import java.util.List;

import com.healthcare.model.entity.EmployeeClocking;

import lombok.Data;

public @Data class EmployeeClockingReportResponse implements Serializable{
	
	private static final long serialVersionUID = 2669160683037436888L;

	private List<EmployeeClocking> employeeClocking;
	private String totalHours;
}
