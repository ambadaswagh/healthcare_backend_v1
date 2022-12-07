package com.healthcare.model.entity;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "company")
@EqualsAndHashCode(callSuper = true)
public @Data class Company extends Audit implements Serializable {

	
	private static final long serialVersionUID = -7901755875334279875L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(name = "license_no")
	private String licenseNo;
	@Column(name = "federal_tax")
	private String federalTax;
	@Column(name = "federal_tax_start")
	private Timestamp federalTaxStart;
	@Column(name = "federal_tax_expire")
	private Timestamp federalTaxExpire;
	@Column(name = "federal_tax_status")
	private int federalTaxStatus;
	@Column(name = "state_tax")
	private String stateTax;
	@Column(name = "state_tax_start")
	private Timestamp stateTaxStart;
	@Column(name = "state_tax_expire")
	private Timestamp stateTaxExpire;
	@Column(name = "state_tax_status")
	private int stateTaxStatus;
	private String name;
	private String phone;
	private String fax;
	private String email;
	@Column(name = "address_one")
	private String addressOne;
	@Column(name = "address_two")
	private String addressTwo;
	@Column(name = "worktime_start")
	private Time worktimeStart;
	@Column(name = "worktime_end")
	private Time worktimeEnd;
	private String city;
	private String state;
	private String zipcode;
	private int status;
	private String type;
	private String daysWork;

}
