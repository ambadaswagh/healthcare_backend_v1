package com.healthcare.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "home_visit")
@EqualsAndHashCode(callSuper = true)
public @Data class HomeVisit extends Audit implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name="check_in_time")
	private Timestamp checkInTime;
	@Column(name="carereceiver_signature")
	private String careReceiverSignature;
	
	@OneToOne
	@JoinColumn(name="carereceiver_signature_Id")
	private Document careReceiverSignatureId;
	@Column(name="check_out_time")
	private Timestamp checkOutTime;
	@Column(name="carereceiver_comments")
	private String careReceiverComments;
	private String notes;
	private String status;
	@ManyToOne
	@JoinColumn(name = "serviceplan_id")
	private ServicePlan serviceplan;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	@ManyToOne
	@JoinColumn(name = "caregiver_id")
	private CareGiver careGiver;
}
