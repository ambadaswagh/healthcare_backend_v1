package com.healthcare.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "service_plan")
@EqualsAndHashCode(callSuper = true)
public @Data class ServicePlan extends Audit implements Serializable {

	private static final long serialVersionUID = -8777670249499595658L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "approved_by", nullable = false)
	private String approvedBy;

	@Column(name = "plan_start")
	private Timestamp planStart;
	@Column(name = "plan_end")
	private Timestamp planEnd;
	private String days;
	
	@Column(name = "authorization_code")
	private String authorizationCode;

	@Column(name = "insurance_start")
	@Temporal(TemporalType.TIMESTAMP)
	private Date insuranceStart;

	@Column(name = "insurance_end")
	@Temporal(TemporalType.TIMESTAMP)
	private Date insuranceEnd;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "doc_file_id")
	private Document docFile;

	@Column(name = "outcome_target_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date outcomeTargetDate;

	@Column(name = "outcome_date_achieved", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date outcome_date_achieved;

	@Column(name = "activity_and_engagement_level")
	private String activityAndEngagementLevel;

	@Column(name = "capacity_self_estimate")
	private String capacity_self_estimate;

	@Column(name = "capacity_independence_self_care")
	private String capacity_independence_self_care;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "signature_id")
	private Signature signature;

	@Column(name = "medical_status")
	private String medicalStatus;
	 
	@Column(name = "nutrition_status")
	private String nutritionStatus;
	 
	@Column(name = "sensory_status")
	private String sensoryStatus;
	 
	@Column(name = "medication_status")
	private String medicationStatus;
	 
	@Column(name = "pain_status")
	private String painStatus;
	 
	@Column(name = "congonitive_status")
	private String congonitiveStatus;
	 
	@Column(name = "psychosocial_status")
	private String psychosocialStatus;
	 
	@Column(name = "spiritual_status")
	private String spiritualStatus;
	
	@Column(name = "communication_status")
	private String communicationStatus;
	
	@Column(name = "expected_outcome")
	private String expectedOutcome;
	
	
	// @Column(name = "outcome_target_date")
	// private Timestamp outcomeTargetDate;
	// @Column(name = "outcome_date_achieved")
	// private Timestamp outcomeDateAchieved;
	// @Column(name = "outcome_criteria")
	// private String outcomeCriteria;
	//
	// @Column(name = "activity_level_engagement")
	// private String activityLevelEngagement;
	//
	// @Column(name = "capacity_self_estimate")
	// private String capacitySelfEstimate;
	
	@Column(name = "adls_level_care")
	private String adlsLevelCare;
	
	// @Column(name = "capacity_independence")
	// private String capacityIndependence;
	// @Column(name = "self_care")
	// private String selfCare;
	// @Column(name = "signature_path")
	// private String signaturePath;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	@JsonBackReference
	private User user;
}