package com.healthcare.model.entity;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.healthcare.util.CustomJsonDateSerializer;

import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Data;

@Entity
@Table(name = "health_insurance_claim")
@EqualsAndHashCode (callSuper = false)
public @Data class HealthInsuranceClaim extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -942763745378021453L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnore
    private User user;
	
	@Column(name = "insurance_plan_name")
	private String insurancePlanName;
	@Column(name = "relationship_to_insured")
	private String relationshipToInsured;
	@Column(name = "patient_marital_status")
	private String patientMaritalStatus;
	@Column(name = "patient_employment_status")
	private String patientEmploymentStatus;
	@Column(name = "other_insured_first_name")
	private String otherInsuredFirstName;
	@Column(name = "other_insured_middle_name")
	private String otherInsuredMiddleName;
	@Column(name = "other_insured_last_name")
	private String otherInsuredLastName;
	@Column(name = "other_insured_policy_or_group_no")
	private String otherInsuredPolicyOrGroupNo;
	@Column(name = "other_insured_sex")
	private String otherInsuredSex;
	@Column(name = "other_insured_employer_school_name")
	private String otherInsuredEmployerSchoolName;
	@Column(name = "other_insured_insurance_insurance_plan_name")
	private String otherInsuredInsurancePlanName;
	@Column(name = "patient_condtion_relation")
	private String patientCondtionRelation;
	@Column(name = "auto_accident_place")
	private String autoAccidentPlace;
	@Column(name = "reserved_local_use")
	private String reservedLocalUse;
	@OneToOne
	@JoinColumn(name = "patient_signature_id")
	private Document patientSignatureDocId;


	@Column(name = "insured_id_no")
	private String insuredIdNo;
	@Column(name = "insured_first_name")
	private String insuredFirstName;
	@Column(name = "insured_middle_name")
	private String insuredMiddleName;
	@Column(name = "insured_last_name")
	private String insuredLastName;
	@Column(name = "insured_address")
	private String insuredAddress;
	@Column(name = "insured_city")
	private String insuredCity;
	@Column(name = "insured_state")
	private String insuredState;
	@Column(name = "insured_zipcode")
	private String insuredZipcode;
	@Column(name = "insured_phone_no")
	private String insuredPhoneNo;
	@Column(name = "insured_policy_group_or_feca_no")
	private String insuredPolicyGroupOrFecaNo;
	@Column(name = "insured_sex")
	private String insuredSex;
	@Column(name = "insured_dob")
	@JsonSerialize(using=CustomJsonDateSerializer.class)
	private Date insuredDob;
	@Column(name = "insuredEmployerSchoolName")
	private String insured_employer_school_name;
	@Column(name = "insured_insurance_plan_name_or_program_name")
	private String insuredInsurancePlanNameOrProgramName;
	@Column(name = "insured_another_health_plan")
	private Integer insuredAnotherHealthPlan;
	@OneToOne
	@JoinColumn(name = "insured_signature_id")
	private Document insuredSignatureDocId;
	@Column(name = "current_illness_injury_pregnancy_date")
	private Date currentIllnessInjuryPregnancyDate;
	@Column(name = "first_similar_illness_date")
	private Date firstSimilarIllnessDate;
	@Column(name = "patient_unable_work_current_occupation_from_date")
	private Date patientUnableWorkCurrentOccupationFromDate;
	@Column(name = "patient_unable_work_current_occupation_from_to")
	private Date patientUnableWorkCurrentOccupationFromTo;
	@Column(name = "hospitalization_related_to_current_services_date_from")
	private Date hospitalizationRelatedToCurrentServicesDateFrom;
	@Column(name = "hospitalization_related_to_current_services_date_to")
	private Date hospitalizationRelatedToCurrentServicesDateTo;
	@Column(name = "outside_lab")
	private Integer outsideLab;
	private double charges;
	@Column(name = "medicaid_resubmission_code")
	private String medicaidResubmissionCode;
	@Column(name = "original_ref_no")
	private String originalRefNo;
	@Column(name = "prior_authorization_no")
	private String priorAuthorizationNo;
	@Column(name = "referring_provider_name")
	private String referringProviderName;
	@Column(name = "referring_provider_npi")
	private String referringProviderNpi;
	@Column(name = "reserved_for_local_use")
	private String reservedForLocalUse;
	@Column(name = "diagnosis_or_nature_of_illness_or_injury_type_1")
	private String diagnosisOrNatureOfIllnessOrInjuryType1;
	@Column(name = "diagnosis_or_nature_of_illness_or_injury_type_2")
	private String diagnosisOrNatureOfIllnessOrInjuryType2;
	@Column(name = "diagnosis_or_nature_of_illness_or_injury_type_3")
	private String diagnosisOrNatureOfIllnessOrInjuryType3;
	@Column(name = "diagnosis_or_nature_of_illness_or_injury_type_4")
	private String diagnosisOrNatureOfIllnessOrInjuryType4;
	@Column(name = "federal_tax_id_no_ssn")
	private String federalTaxIdNoSsn;
	@Column(name = "federal_tax_id_no_ein")
	private String federalTaxIdNoEin;
	@Column(name = "patient_account_no")
	private String patientAccountNo;
	@Column(name = "accept_assignment")
	private Integer acceptAssignment;
	@Column(name = "total_charge")
	private double totalCharge;
	@Column(name = "amount_paid")
	private double amountPaid;
	@Column(name = "balance_due")
	private double balanceDue;
	@OneToOne
	@JoinColumn(name = "physician_or_supplier_signature_id")
	private Document physicianOrSupplierSignatureId;
	@Column(name = "service_facility_location_information_a")
	private String serviceFacilityLocationInformationA;
	@Column(name = "service_facility_location_information_b")
	private String serviceFacilityLocationInformationB;
	@Column(name = "billing_provider_info_and_ph_no_a")
	private String billingProviderInfoAndPhNoA;
	@Column(name = "billing_provider_info_and_ph_no_b")
	private String billingProviderInfoAndPhNoB;
	
	@OneToMany(fetch=FetchType.EAGER, cascade= CascadeType.ALL)
	@JoinColumn(name = "health_insurance_claim_id")
	@NotFound(action = NotFoundAction.IGNORE)
	private List<PatientDiagnosis> patientDiagnosis = new ArrayList<PatientDiagnosis>();
	
	@Transient
	private String tripStatus;
	
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof HealthInsuranceClaim))
			return false;

		final HealthInsuranceClaim hic = (HealthInsuranceClaim) other;

		if (!hic.getId().equals(getId()))
			return false;
		if (!hic.getPatientDiagnosis().equals(getPatientDiagnosis()))
			return false;

		return true;
	}

	public int hashCode() {
		int result;
		result = getPatientDiagnosis().hashCode();
		result = (int) (29 * result + getId());
		return result;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getInsurancePlanName() {
		return insurancePlanName;
	}

	public void setInsurancePlanName(String insurancePlanName) {
		this.insurancePlanName = insurancePlanName;
	}

	public String getRelationshipToInsured() {
		return relationshipToInsured;
	}

	public void setRelationshipToInsured(String relationshipToInsured) {
		this.relationshipToInsured = relationshipToInsured;
	}

	public String getPatientMaritalStatus() {
		return patientMaritalStatus;
	}

	public void setPatientMaritalStatus(String patientMaritalStatus) {
		this.patientMaritalStatus = patientMaritalStatus;
	}

	public String getPatientEmploymentStatus() {
		return patientEmploymentStatus;
	}

	public void setPatientEmploymentStatus(String patientEmploymentStatus) {
		this.patientEmploymentStatus = patientEmploymentStatus;
	}

	public String getOtherInsuredFirstName() {
		return otherInsuredFirstName;
	}

	public void setOtherInsuredFirstName(String otherInsuredFirstName) {
		this.otherInsuredFirstName = otherInsuredFirstName;
	}

	public String getOtherInsuredMiddleName() {
		return otherInsuredMiddleName;
	}

	public void setOtherInsuredMiddleName(String otherInsuredMiddleName) {
		this.otherInsuredMiddleName = otherInsuredMiddleName;
	}

	public String getOtherInsuredLastName() {
		return otherInsuredLastName;
	}

	public void setOtherInsuredLastName(String otherInsuredLastName) {
		this.otherInsuredLastName = otherInsuredLastName;
	}

	public String getOtherInsuredPolicyOrGroupNo() {
		return otherInsuredPolicyOrGroupNo;
	}

	public void setOtherInsuredPolicyOrGroupNo(String otherInsuredPolicyOrGroupNo) {
		this.otherInsuredPolicyOrGroupNo = otherInsuredPolicyOrGroupNo;
	}

	public String getOtherInsuredSex() {
		return otherInsuredSex;
	}

	public void setOtherInsuredSex(String otherInsuredSex) {
		this.otherInsuredSex = otherInsuredSex;
	}

	public String getOtherInsuredEmployerSchoolName() {
		return otherInsuredEmployerSchoolName;
	}

	public void setOtherInsuredEmployerSchoolName(String otherInsuredEmployerSchoolName) {
		this.otherInsuredEmployerSchoolName = otherInsuredEmployerSchoolName;
	}

	public String getOtherInsuredInsurancePlanName() {
		return otherInsuredInsurancePlanName;
	}

	public void setOtherInsuredInsurancePlanName(String otherInsuredInsurancePlanName) {
		this.otherInsuredInsurancePlanName = otherInsuredInsurancePlanName;
	}

	public String getPatientCondtionRelation() {
		return patientCondtionRelation;
	}

	public void setPatientCondtionRelation(String patientCondtionRelation) {
		this.patientCondtionRelation = patientCondtionRelation;
	}

	public String getAutoAccidentPlace() {
		return autoAccidentPlace;
	}

	public void setAutoAccidentPlace(String autoAccidentPlace) {
		this.autoAccidentPlace = autoAccidentPlace;
	}

	public String getReservedLocalUse() {
		return reservedLocalUse;
	}

	public void setReservedLocalUse(String reservedLocalUse) {
		this.reservedLocalUse = reservedLocalUse;
	}

	public Document getPatientSignatureDocId() {
		return patientSignatureDocId;
	}

	public void setPatientSignatureDocId(Document patientSignatureDocId) {
		this.patientSignatureDocId = patientSignatureDocId;
	}

	public String getInsuredIdNo() {
		return insuredIdNo;
	}

	public void setInsuredIdNo(String insuredIdNo) {
		this.insuredIdNo = insuredIdNo;
	}

	public String getInsuredFirstName() {
		return insuredFirstName;
	}

	public void setInsuredFirstName(String insuredFirstName) {
		this.insuredFirstName = insuredFirstName;
	}

	public String getInsuredMiddleName() {
		return insuredMiddleName;
	}

	public void setInsuredMiddleName(String insuredMiddleName) {
		this.insuredMiddleName = insuredMiddleName;
	}

	public String getInsuredLastName() {
		return insuredLastName;
	}

	public void setInsuredLastName(String insuredLastName) {
		this.insuredLastName = insuredLastName;
	}

	public String getInsuredAddress() {
		return insuredAddress;
	}

	public void setInsuredAddress(String insuredAddress) {
		this.insuredAddress = insuredAddress;
	}

	public String getInsuredCity() {
		return insuredCity;
	}

	public void setInsuredCity(String insuredCity) {
		this.insuredCity = insuredCity;
	}

	public String getInsuredState() {
		return insuredState;
	}

	public void setInsuredState(String insuredState) {
		this.insuredState = insuredState;
	}

	public String getInsuredZipcode() {
		return insuredZipcode;
	}

	public void setInsuredZipcode(String insuredZipcode) {
		this.insuredZipcode = insuredZipcode;
	}

	public String getInsuredPhoneNo() {
		return insuredPhoneNo;
	}

	public void setInsuredPhoneNo(String insuredPhoneNo) {
		this.insuredPhoneNo = insuredPhoneNo;
	}

	public String getInsuredPolicyGroupOrFecaNo() {
		return insuredPolicyGroupOrFecaNo;
	}

	public void setInsuredPolicyGroupOrFecaNo(String insuredPolicyGroupOrFecaNo) {
		this.insuredPolicyGroupOrFecaNo = insuredPolicyGroupOrFecaNo;
	}

	public String getInsuredSex() {
		return insuredSex;
	}

	public void setInsuredSex(String insuredSex) {
		this.insuredSex = insuredSex;
	}

	public Date getInsuredDob() {
		return insuredDob;
	}

	public void setInsuredDob(Date insuredDob) {
		this.insuredDob = insuredDob;
	}

	public String getInsured_employer_school_name() {
		return insured_employer_school_name;
	}

	public void setInsured_employer_school_name(String insured_employer_school_name) {
		this.insured_employer_school_name = insured_employer_school_name;
	}

	public String getInsuredInsurancePlanNameOrProgramName() {
		return insuredInsurancePlanNameOrProgramName;
	}

	public void setInsuredInsurancePlanNameOrProgramName(String insuredInsurancePlanNameOrProgramName) {
		this.insuredInsurancePlanNameOrProgramName = insuredInsurancePlanNameOrProgramName;
	}

	public Integer getInsuredAnotherHealthPlan() {
		return insuredAnotherHealthPlan;
	}

	public void setInsuredAnotherHealthPlan(Integer insuredAnotherHealthPlan) {
		this.insuredAnotherHealthPlan = insuredAnotherHealthPlan;
	}

	public Document getInsuredSignatureDocId() {
		return insuredSignatureDocId;
	}

	public void setInsuredSignatureDocId(Document insuredSignatureDocId) {
		this.insuredSignatureDocId = insuredSignatureDocId;
	}

	public Date getCurrentIllnessInjuryPregnancyDate() {
		return currentIllnessInjuryPregnancyDate;
	}

	public void setCurrentIllnessInjuryPregnancyDate(Date currentIllnessInjuryPregnancyDate) {
		this.currentIllnessInjuryPregnancyDate = currentIllnessInjuryPregnancyDate;
	}

	public Date getFirstSimilarIllnessDate() {
		return firstSimilarIllnessDate;
	}

	public void setFirstSimilarIllnessDate(Date firstSimilarIllnessDate) {
		this.firstSimilarIllnessDate = firstSimilarIllnessDate;
	}

	public Date getPatientUnableWorkCurrentOccupationFromDate() {
		return patientUnableWorkCurrentOccupationFromDate;
	}

	public void setPatientUnableWorkCurrentOccupationFromDate(Date patientUnableWorkCurrentOccupationFromDate) {
		this.patientUnableWorkCurrentOccupationFromDate = patientUnableWorkCurrentOccupationFromDate;
	}

	public Date getPatientUnableWorkCurrentOccupationFromTo() {
		return patientUnableWorkCurrentOccupationFromTo;
	}

	public void setPatientUnableWorkCurrentOccupationFromTo(Date patientUnableWorkCurrentOccupationFromTo) {
		this.patientUnableWorkCurrentOccupationFromTo = patientUnableWorkCurrentOccupationFromTo;
	}

	public Date getHospitalizationRelatedToCurrentServicesDateFrom() {
		return hospitalizationRelatedToCurrentServicesDateFrom;
	}

	public void setHospitalizationRelatedToCurrentServicesDateFrom(Date hospitalizationRelatedToCurrentServicesDateFrom) {
		this.hospitalizationRelatedToCurrentServicesDateFrom = hospitalizationRelatedToCurrentServicesDateFrom;
	}

	public Date getHospitalizationRelatedToCurrentServicesDateTo() {
		return hospitalizationRelatedToCurrentServicesDateTo;
	}

	public void setHospitalizationRelatedToCurrentServicesDateTo(Date hospitalizationRelatedToCurrentServicesDateTo) {
		this.hospitalizationRelatedToCurrentServicesDateTo = hospitalizationRelatedToCurrentServicesDateTo;
	}

	public Integer getOutsideLab() {
		return outsideLab;
	}

	public void setOutsideLab(Integer outsideLab) {
		this.outsideLab = outsideLab;
	}

	public double getCharges() {
		return charges;
	}

	public void setCharges(double charges) {
		this.charges = charges;
	}

	public String getMedicaidResubmissionCode() {
		return medicaidResubmissionCode;
	}

	public void setMedicaidResubmissionCode(String medicaidResubmissionCode) {
		this.medicaidResubmissionCode = medicaidResubmissionCode;
	}

	public String getOriginalRefNo() {
		return originalRefNo;
	}

	public void setOriginalRefNo(String originalRefNo) {
		this.originalRefNo = originalRefNo;
	}

	public String getPriorAuthorizationNo() {
		return priorAuthorizationNo;
	}

	public void setPriorAuthorizationNo(String priorAuthorizationNo) {
		this.priorAuthorizationNo = priorAuthorizationNo;
	}

	public String getReferringProviderName() {
		return referringProviderName;
	}

	public void setReferringProviderName(String referringProviderName) {
		this.referringProviderName = referringProviderName;
	}

	public String getReferringProviderNpi() {
		return referringProviderNpi;
	}

	public void setReferringProviderNpi(String referringProviderNpi) {
		this.referringProviderNpi = referringProviderNpi;
	}

	public String getReservedForLocalUse() {
		return reservedForLocalUse;
	}

	public void setReservedForLocalUse(String reservedForLocalUse) {
		this.reservedForLocalUse = reservedForLocalUse;
	}

	public String getDiagnosisOrNatureOfIllnessOrInjuryType1() {
		return diagnosisOrNatureOfIllnessOrInjuryType1;
	}

	public void setDiagnosisOrNatureOfIllnessOrInjuryType1(String diagnosisOrNatureOfIllnessOrInjuryType1) {
		this.diagnosisOrNatureOfIllnessOrInjuryType1 = diagnosisOrNatureOfIllnessOrInjuryType1;
	}

	public String getDiagnosisOrNatureOfIllnessOrInjuryType2() {
		return diagnosisOrNatureOfIllnessOrInjuryType2;
	}

	public void setDiagnosisOrNatureOfIllnessOrInjuryType2(String diagnosisOrNatureOfIllnessOrInjuryType2) {
		this.diagnosisOrNatureOfIllnessOrInjuryType2 = diagnosisOrNatureOfIllnessOrInjuryType2;
	}

	public String getDiagnosisOrNatureOfIllnessOrInjuryType3() {
		return diagnosisOrNatureOfIllnessOrInjuryType3;
	}

	public void setDiagnosisOrNatureOfIllnessOrInjuryType3(String diagnosisOrNatureOfIllnessOrInjuryType3) {
		this.diagnosisOrNatureOfIllnessOrInjuryType3 = diagnosisOrNatureOfIllnessOrInjuryType3;
	}

	public String getDiagnosisOrNatureOfIllnessOrInjuryType4() {
		return diagnosisOrNatureOfIllnessOrInjuryType4;
	}

	public void setDiagnosisOrNatureOfIllnessOrInjuryType4(String diagnosisOrNatureOfIllnessOrInjuryType4) {
		this.diagnosisOrNatureOfIllnessOrInjuryType4 = diagnosisOrNatureOfIllnessOrInjuryType4;
	}

	public String getFederalTaxIdNoSsn() {
		return federalTaxIdNoSsn;
	}

	public void setFederalTaxIdNoSsn(String federalTaxIdNoSsn) {
		this.federalTaxIdNoSsn = federalTaxIdNoSsn;
	}

	public String getFederalTaxIdNoEin() {
		return federalTaxIdNoEin;
	}

	public void setFederalTaxIdNoEin(String federalTaxIdNoEin) {
		this.federalTaxIdNoEin = federalTaxIdNoEin;
	}

	public String getPatientAccountNo() {
		return patientAccountNo;
	}

	public void setPatientAccountNo(String patientAccountNo) {
		this.patientAccountNo = patientAccountNo;
	}

	public Integer getAcceptAssignment() {
		return acceptAssignment;
	}

	public void setAcceptAssignment(Integer acceptAssignment) {
		this.acceptAssignment = acceptAssignment;
	}

	public double getTotalCharge() {
		return totalCharge;
	}

	public void setTotalCharge(double totalCharge) {
		this.totalCharge = totalCharge;
	}

	public double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public double getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	public Document getPhysicianOrSupplierSignatureId() {
		return physicianOrSupplierSignatureId;
	}

	public void setPhysicianOrSupplierSignatureId(Document physicianOrSupplierSignatureId) {
		this.physicianOrSupplierSignatureId = physicianOrSupplierSignatureId;
	}

	public String getServiceFacilityLocationInformationA() {
		return serviceFacilityLocationInformationA;
	}

	public void setServiceFacilityLocationInformationA(String serviceFacilityLocationInformationA) {
		this.serviceFacilityLocationInformationA = serviceFacilityLocationInformationA;
	}

	public String getServiceFacilityLocationInformationB() {
		return serviceFacilityLocationInformationB;
	}

	public void setServiceFacilityLocationInformationB(String serviceFacilityLocationInformationB) {
		this.serviceFacilityLocationInformationB = serviceFacilityLocationInformationB;
	}

	public String getBillingProviderInfoAndPhNoA() {
		return billingProviderInfoAndPhNoA;
	}

	public void setBillingProviderInfoAndPhNoA(String billingProviderInfoAndPhNoA) {
		this.billingProviderInfoAndPhNoA = billingProviderInfoAndPhNoA;
	}

	public String getBillingProviderInfoAndPhNoB() {
		return billingProviderInfoAndPhNoB;
	}

	public void setBillingProviderInfoAndPhNoB(String billingProviderInfoAndPhNoB) {
		this.billingProviderInfoAndPhNoB = billingProviderInfoAndPhNoB;
	}

	public List<PatientDiagnosis> getPatientDiagnosis() {
		return patientDiagnosis;
	}

	public void setPatientDiagnosis(List<PatientDiagnosis> patientDiagnosis) {
		this.patientDiagnosis = patientDiagnosis;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

    public String getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }

}
