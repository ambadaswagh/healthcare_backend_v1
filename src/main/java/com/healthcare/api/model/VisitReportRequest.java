package com.healthcare.api.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

public @Data class VisitReportRequest implements Serializable {

	private static final long serialVersionUID = -8866621401679788273L;
	private Date startDate;
	private Date endDate;
}
