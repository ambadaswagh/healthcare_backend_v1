package com.healthcare.api.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

public @Data class EmployeeClockingReportRequest implements Serializable{
	
	private static final long serialVersionUID = 2669160683037436888L;

	private Long employeeId;
	private Date startDate;
	private Date endDate;
}
