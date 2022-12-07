package com.healthcare.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Table(name = "visit")
@EqualsAndHashCode(callSuper = true)
public @Data class Visit extends Audit implements Serializable {

	private static final long serialVersionUID = -5449963759010972006L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "activity_id")
	private Long activityId;
	@ManyToOne
	@JoinColumn(name = "agency_id")
	private Agency agency;
	
	@Column(name = "check_in_time")
	private Timestamp checkInTime;
	@ManyToOne
	@JoinColumn(name = "service_plan_id")
	private ServicePlan servicePlan;
	@ManyToOne
	@JoinColumn(name = "selected_breakfast_id")
	private Meal selectedBreakfast;
	@ManyToOne
	@JoinColumn(name = "selected_lunch_id")
	private Meal selectedLunch;
	@ManyToOne
	@JoinColumn(name = "selected_dinner_id")
	private Meal selectedDinner;
	
	@Column(name = "selected_seat")
	private String selectedSeat;

	/*@OneToMany(cascade=CascadeType.ALL)
	@JoinTable(
            name = "visit_has_activity",
            joinColumns = @JoinColumn(name = "visit_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_id")
	)
	private Set<Activity> activities;*/

//	
//	@OneToOne
//	@JoinColumn(name = "user_signature")
//	private Document userSignature;
//	
	@Column(name = "pin")
	private String pin;
	
	@Column(name = "check_out_time")
	private Timestamp checkOutTime;
	
	@Column(name = "user_comments")
	private String userComments;
	
	@Column(name = "notes")
	private String notes;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "active")
	private int active;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private User user;
	
	@Column(name = "signature")
	private String signature;
	
	@Column(name = "billing_code")
	private String billingCode;

	@Column(name = "expected_money")
	private Double expectedMoney;

	@Column(name = "actual_money")
	private Double actualMoney;

	@ManyToOne
	@JoinColumn(name = "reservation_id")
	private Reservation reservation;

	@ManyToOne
	@JoinColumn(name = "admin_id")
	private Admin admin;
	
	@ManyToOne
	@JoinColumn(name = "seat_id")
	private Seat seat;
	@ManyToOne
	@JoinColumn(name = "check_in_photo_id")
	private Document checkinPhoto;

	@ManyToOne
	@JoinColumn(name = "check_out_photo_id")
	private Document checkoutPhoto;
	
	@ManyToOne
	@JoinColumn(name = "check_in_signature_id")
	private Document checkInSignature;
	
	@ManyToOne
	@JoinColumn(name = "check_out_signature_id")
	private Document checkOutSignature;

	@Column(name = "reserved_date")
	private Date reservedDate;

}
