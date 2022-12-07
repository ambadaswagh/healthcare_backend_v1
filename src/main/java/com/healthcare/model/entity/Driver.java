package com.healthcare.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.healthcare.model.enums.EmployeeTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "driver")
@EqualsAndHashCode(callSuper = true)
public @Data class Driver extends Audit implements Serializable {

	private static final long serialVersionUID = 512962093355769597L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "gender")
	private String gender;
	@Column(name = "email")
	private String email;
	@Column(name = "dob")
	private Date dob;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "secondary_phone")
	private String secondaryPhone;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "state")
	private String state;
	
	@Column(name = "zipcode")
	private String zipcode;
	
	@Column(name = "username")
	private String username;
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	
	private String driverType;
	
	@Column(name = "basePercent")
	private Double basePercent;
	
	
	@Column(name = "driverPercent")
	private Double driverPercent;
	
	@Column(name = "reservePercent")
	private Double reservePercent;
	
	private String fleetNum;
	private String nationality;
	private Integer drivingExperience;
	private String signature;
	private String driverLicense;
	private String driverLicenseNum;
	private String driverLicenseClass;
	private String driverLicenseState;
	
	@Column(name = "driverLicenseStart")
	private Date driverLicenseStart;
	
	@Column(name = "driverLicenseExpire")
	private Date driverLicenseExpire;
	
	@Column(name = "driverLicenseStatus")
	private Integer driverLicenseStatus;
	
	@Column(name = "driverTlcFhvLicense")
	private String driverTlcFhvLicense;
	
	@Column(name = "driverTlcFhvLicenseNum")
	private String driverTlcFhvLicenseNum;
	
	@Column(name = "driverTlcFhvLicenseStart")
	private Date driverTlcFhvLicenseStart;
	
	@Column(name = "driverTlcFhvLicenseExpire")
	private Date driverTlcFhvLicenseExpire;
	
	@Column(name = "driverTlcFhvLicenseStatus")
	private Integer driverTlcFhvLicenseStatus;
	
	@Column(name = "background_check")
	private String backgroundCheck;
	
	@Column(name = "backgroundCheckStart")
	private Date backgroundCheckStart;
	
	@Column(name = "backgroundCheckExpire")
	private Date backgroundCheckExpire;
	
	@Column(name = "drivingRecord")
	private String drivingRecord;
	
	@Column(name = "drivingRecordStart")
	private Date drivingRecordStart;
	
	@Column(name = "drivingRecordExpire")
	private Date drivingRecordExpire;
	
	@Column(name = "drugScreen")
	private String drugScreen;
	
	@Column(name = "drugScreenStart")
	private Date drugScreenStart;
	
	@Column(name = "drugScreenExpire")
	private Date drugScreenExpire;
	
	@Column(name = "dspPercent")
	private Double dspPercent;
	@Column(name = "allow_pets")
	private String allow_pets;
	@Column(name = "allow_wheelchair")
	private String allow_wheelchair;
	@Column(name = "duty_status")
	private String duty_status;
	
	@OneToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "profile_photo")
	private Document profilePhoto;
	@ManyToOne
	@JoinColumn(name = "agency_id")
	private Agency agency;
	
	@Column(name = "status")
	private Integer status;
	@Column(name = "background_check_status")
	private Integer backgroundCheckStatus;
	@Column(name = "driving_record_status")
	private Integer drivingRecordStatus;
	@Column(name = "drug_screen_status")
	private Integer drugScreenStatus;
	@Column(name = "base_approved")
	private Integer baseApproved;
	
	
	@Column(name = "approvable_mail")
	private Integer approvableMail;
	@Column(name = "verification_code")
	private String verificationCode;
	@Column(name = "group_id")
	private Integer groupId;
	@Column(name = "permit_id")
	private String permitId;
	@OneToOne
	@JoinColumn(name = "company_id")
	private Company company;
	@Column(name = "vehicle_id")
	private Integer vehicleId;
	
}
