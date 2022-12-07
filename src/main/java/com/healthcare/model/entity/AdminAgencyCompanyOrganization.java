package com.healthcare.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The persistent class for the admin_agency_company_organization database
 * table.
 * 
 */
@Entity
@Table(name = "admin_agency_company_organization")
@NamedQuery(name = "AdminAgencyCompanyOrganization.findAll", query = "SELECT a FROM AdminAgencyCompanyOrganization a")
@EqualsAndHashCode(callSuper = true)
public @Data class AdminAgencyCompanyOrganization extends Audit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToOne
	@JoinColumn(name = "admin_id")
	private Admin admin;

	@OneToOne
	@JoinColumn(name = "agency_id")
	private Agency agency;

	@OneToOne
	@JoinColumn(name = "company_id")
	private Company company;

	@OneToOne
	@JoinColumn(name = "organization_id")
	private Organization organization;

}