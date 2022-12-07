package com.healthcare.dto;

import java.util.Date;
import java.util.List;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.util.MultiValueMap;

import lombok.Data;

@Data
public class AppointmentSearchDTO {
	private String search;
	private Date fromDate;
	private Date toDate;

	private static DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy");

	public AppointmentSearchDTO fromMap(MultiValueMap<String, String> attributes) {
		List<String> searchQueries = attributes.get("search");
		List<String> fromDate = attributes.get("fromDate");
		List<String> toDate = attributes.get("toDate");

		this.search = searchQueries != null && !searchQueries.isEmpty() ? searchQueries.get(0) : "";
		this.fromDate = fromDate != null ? dtf.parseDateTime(fromDate.get(0)).toDate() : null;
		this.toDate = toDate != null ? dtf.parseDateTime(toDate.get(0)).toDate() : null;
		return this;

	}
}
