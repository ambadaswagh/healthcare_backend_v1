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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Entity
@Table(name = "patient_diagnosis")
public @Data class PatientDiagnosis implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 226707159668098996L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "patient_date_of_service_from")
	private Date patientDateOfServiceFrom;
	@Column(name = "patient_date_of_service_to")
	private Date patientDateOfServiceTo;
	@Column(name = "patient_place_of_service")
	private String patientPlaceOfService;
	@Column(name = "patient_supplier_emg")
	private String patientSupplierEmgergency;
	@Column(name = "patient_procedures_services_supplies_cpt_hcpcs")
	private String patientProceduresServicesSuppliesCptHcpcs;
	@Column(name = "patient_procedures_services_supplies_cpt_modifier")
	private String patientProceduresServicesSuppliesCptModifier;
	@Column(name = "patient_diagnosis_pointer")
	private String patientDiagnosisPointer;
	@Column(name = "patient_charges")
	private double patientCharges;
	@Column(name = "days_or_units")
	private String daysOrUnits;
	@Column(name = "epsdt_family_plan")
	private String epsdtFamilyPlan;
	@Column(name = "id_qual")
	private String idQual;
	@Column(name = "rendering_provider_id_no")
	private String renderingProviderIdNo;
	
	@ManyToOne
	@JoinColumn(name = "health_insurance_claim_id",referencedColumnName ="id")
	@JsonProperty(access = Access.WRITE_ONLY)
	private HealthInsuranceClaim healthInsuranceClaim;
	
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof PatientDiagnosis))
			return false;

		final PatientDiagnosis patientDiagnosis = (PatientDiagnosis) other;

		if (!patientDiagnosis.getId().equals(getId()))
			return false;

		return true;
	}

	public int hashCode() {
		int result;
		result = getId().hashCode();
		result = (int) (29 * result);
		return result;
	}
}
