package com.healthcare.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "insurance_broker_company")
@EqualsAndHashCode(callSuper = true)
public @Data class InsuranceBrokerCompany extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6230066427815064175L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String type;
	private String name;
	private String code;
	private String email;
	private String tel;
	@Column(name = "address_one")
	private String addessOne;
	@Column(name = "address_two")
	private String addressTwo;
	private String city;
	private String state;
	private String zipcode;
	@Column(name = "contact_person")
	private String contactPerson;
	@Column(name = "web_url")
	private String webURL;
	private Integer status = 1;
}
