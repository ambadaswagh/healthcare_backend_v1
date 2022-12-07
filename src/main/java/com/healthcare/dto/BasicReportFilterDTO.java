package com.healthcare.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public @Data class BasicReportFilterDTO extends TimeFilterDTO implements Serializable {
	private Long companyId;
	private Long agencyId;
	private Long userId;
}
