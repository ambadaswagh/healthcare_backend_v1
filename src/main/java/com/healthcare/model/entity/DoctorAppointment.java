package com.healthcare.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "doctor_appointment")
@EqualsAndHashCode(callSuper = true)
public @Data class DoctorAppointment extends Audit implements Serializable {

	
	private static final long serialVersionUID = -7901755875334279875L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@Column(name = "trip_id")
	private Integer tripId;
	
	private String reason;
	
	@Column(name = "doctor_name")
	private String doctorName;
	
	@Column(name = "doctor_field")
	private String doctorField;
	
	@Column(name = "appointment_time")
	private Date appointmentTime;
	
	@Column(name = "doctor_tel")
	private String doctorTel;
	
	@Column(name = "doctor_fax")
	private String doctorFax;
	
	@Column(name = "doctor_address_one")
	private String doctorAddressOne;
	
	@Column(name = "doctor_address_two")
	private String doctorAddressTwo;
	
	@Column(name = "doctor_address_city")
	private String doctorAddressCity;
	
	@Column(name = "doctor_address_state")
	private String doctorAddressState;

	@Column(name = "doctor_address_zipcode")
	private String doctorAddressZipcode;

	@Column(name = "doctor_office_working_time")
	private String doctorOfficeWorkingTime;

	@Column(name = "appointment_type")
	private String appointmentType;

	private int status;
}
