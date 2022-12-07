package com.healthcare.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import lombok.Data;


/**
 * The persistent class for the medication database table.
 * 
 */
@Entity
@NamedQuery(name="Medication.findAll", query="SELECT m FROM Medication m")
@Data
public class Medication extends Audit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String allergies;

	@Column(name="ambulatory_status")
	private String ambulatoryStatus;

	private String diagonosis;

	@Column(name="home_attendant_agency_id")
	private Organization homeAttendantAgency;

	@ManyToOne
	@JoinColumn(name = "insurance_id")
	private Organization insurance;

	@Column(name="insurance_eligiable")
	private String insuranceEligiable;

	@Column(name="medical_condition")
	private String medicalCondition;

	@ManyToOne
	@JoinColumn(name = "mltc_id")
	private Organization mltc;

	@Column(name="visiting_nurse")
	private String visitingNurse;

	@Column(name="visiting_nurse_schedule")
	private String visitingNurseSchedule;

	@ManyToOne
	@JoinColumn(name = "service_plan_id")
	private ServicePlan servicePlan;

	@ManyToOne
	@JoinColumn(name = "family_doctor_id")
	private Organization familyDoctor;

	@ManyToOne
	@JoinColumn(name = "expert_doctor_id")
	private Organization expertDoctor;

	@ManyToOne
	@JoinColumn(name = "pharmacy_id")
	private Organization pharmacyId;

	@ManyToOne
	@JoinColumn(name = "medicine_id")
	private  Medicine medicine;
}