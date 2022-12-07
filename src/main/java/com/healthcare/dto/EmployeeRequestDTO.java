package com.healthcare.dto;

import lombok.Data;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.util.MultiValueMap;

import java.util.Date;
import java.util.List;

@Data
public class EmployeeRequestDTO {
	private Long employeeId;
    private String  fromDate;
    private String toDate;
    private String statFilter;

    private static DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");

    public EmployeeRequestDTO fromMap(MultiValueMap<String, String> attributes) {
    	
    	List<String> employeeId =  attributes.get("employeeId");
        List<String> fromDate = attributes.get("fromDate");
        List<String> toDate = attributes.get("toDate");
        List<String> statFilter = attributes.get("statFilter");

        this.employeeId = employeeId != null ? Long.valueOf(employeeId.get(0)) : null;
        this.fromDate = fromDate != null ? fromDate.get(0) : null;
        this.toDate = toDate != null ? toDate.get(0): null;
        this.statFilter = statFilter != null ? statFilter.get(0): null;
        return this;
    }
}
