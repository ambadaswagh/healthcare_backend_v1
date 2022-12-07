package com.healthcare.api.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

public @Data class VisitReportResponse implements Serializable{
	
	private static final long serialVersionUID = -4945447007472895294L;

	private String userName;
	private BigInteger noOfVisit;
	private BigDecimal averageHour;
}
