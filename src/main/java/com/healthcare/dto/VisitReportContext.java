package com.healthcare.dto;

import java.util.List;

import com.healthcare.model.entity.Agency;
import com.healthcare.model.entity.Company;
import com.healthcare.model.entity.User;
import com.healthcare.model.entity.Visit;

import lombok.Data;

@Data
public class VisitReportContext {
	private List<Visit> visits;
	private Company company;
	private User user;
	private Agency agency;

	public VisitReportContext() {
	}

	public VisitReportContext(List<Visit> visits, Company company, Agency agency, User user) {
		this.visits = visits;
		this.company = company;
		this.agency = agency;
		this.user = user;
	}
}
