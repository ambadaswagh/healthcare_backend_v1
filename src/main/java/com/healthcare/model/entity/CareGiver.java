package com.healthcare.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "caregiver")
@EqualsAndHashCode(callSuper = true)
public @Data class CareGiver extends Audit implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;
	@ManyToOne
	@JoinColumn(name = "agency_id")
	private Agency agency;
	@Column(name="caregiver_type")
	private Long careGiverType;
	private String username;
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "middle_name")
	private String middleName;
	private String lastName;
	private String gender;
	private String language;
	@Column(name = "social_security_number")
	private String socialSecurityNumber;
	@Column(name = "dob")
	private Date dateOfBirth;
	private String email;
	private String phone;
	@Column(name = "secondary_phone")
	private String secondaryPhone;
	@Column(name = "verification_code")
	private String verificationCode;
	@Column(name = "address_type")
	private String addressType;
	@Column(name = "address_one")
	private String addressOne;
	@Column(name = "address_two")
	private String addressTwo;
	private String city;
	private String state;
	private String zipcode;
	@OneToOne
	@JoinColumn(name="profile_photo")
	private Document profilePhoto;
	private String certificate;
	@Column(name = "certificate_start")
	private Timestamp certificateStart;
	@Column(name = "certificate_end")
	private Timestamp certificateEnd;
	private Integer status;
	@Column(name = "vacation_note")
	private String vacationNote;
	@Column(name = "vacation_start")
	private Timestamp vacationStart;
	@Column(name = "vacation_end")
	private Timestamp vacationEnd;
	
}
