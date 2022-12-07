package com.healthcare.dto;

import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.Company;
import com.healthcare.model.entity.Role;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

public @Data class ReportDTO implements Serializable {

	private Long id;
	private long baseId;
	private String reportTitle;
	private Timestamp startDate;
	private Timestamp endDate;
	private String dataColumns;
	private String format;
	private Timestamp downloadAt;
	private Timestamp createdAt;

}
