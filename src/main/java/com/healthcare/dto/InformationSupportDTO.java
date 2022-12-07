package com.healthcare.dto;

import com.healthcare.model.entity.Audit;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

public class InformationSupportDTO extends Audit implements Serializable {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHaveHelpWithCare() {
        return haveHelpWithCare;
    }

    public void setHaveHelpWithCare(Long haveHelpWithCare) {
        this.haveHelpWithCare = haveHelpWithCare;
    }

    public String getSupportPeopleFirstName() {
        return supportPeopleFirstName;
    }

    public void setSupportPeopleFirstName(String supportPeopleFirstName) {
        this.supportPeopleFirstName = supportPeopleFirstName;
    }

    public String getSupportPeopleMiddleName() {
        return supportPeopleMiddleName;
    }

    public void setSupportPeopleMiddleName(String supportPeopleMiddleName) {
        this.supportPeopleMiddleName = supportPeopleMiddleName;
    }

    public String getSupportPeopleLastName() {
        return supportPeopleLastName;
    }

    public void setSupportPeopleLastName(String supportPeopleLastName) {
        this.supportPeopleLastName = supportPeopleLastName;
    }

    public String getSupportPeopleRelation() {
        return supportPeopleRelation;
    }

    public void setSupportPeopleRelation(String supportPeopleRelation) {
        this.supportPeopleRelation = supportPeopleRelation;
    }

    public String getSupportPeopleEmail() {
        return supportPeopleEmail;
    }

    public void setSupportPeopleEmail(String supportPeopleEmail) {
        this.supportPeopleEmail = supportPeopleEmail;
    }

    public String getSupportPeople() {
        return supportPeople;
    }

    public void setSupportPeople(String supportPeople) {
        this.supportPeople = supportPeople;
    }

    public String getSupportPeopleHomePhone() {
        return supportPeopleHomePhone;
    }

    public void setSupportPeopleHomePhone(String supportPeopleHomePhone) {
        this.supportPeopleHomePhone = supportPeopleHomePhone;
    }

    public String getSupportPeopleWorkPhone() {
        return supportPeopleWorkPhone;
    }

    public void setSupportPeopleWorkPhone(String supportPeopleWorkPhone) {
        this.supportPeopleWorkPhone = supportPeopleWorkPhone;
    }

    public String getSupportPeopleCellPhone() {
        return supportPeopleCellPhone;
    }

    public void setSupportPeopleCellPhone(String supportPeopleCellPhone) {
        this.supportPeopleCellPhone = supportPeopleCellPhone;
    }

    public String getInvolvement() {
        return involvement;
    }

    public void setInvolvement(String involvement) {
        this.involvement = involvement;
    }

    public Long getCustomerHasGoodRelationshipWithSupport() {
        return customerHasGoodRelationshipWithSupport;
    }

    public void setCustomerHasGoodRelationshipWithSupport(Long customerHasGoodRelationshipWithSupport) {
        this.customerHasGoodRelationshipWithSupport = customerHasGoodRelationshipWithSupport;
    }

    public Long getCustomerWillAcceptHelpToRemainHomeOrIndependence() {
        return customerWillAcceptHelpToRemainHomeOrIndependence;
    }

    public void setCustomerWillAcceptHelpToRemainHomeOrIndependence(Long customerWillAcceptHelpToRemainHomeOrIndependence) {
        this.customerWillAcceptHelpToRemainHomeOrIndependence = customerWillAcceptHelpToRemainHomeOrIndependence;
    }

    public String getFactorsLimitInformalSupportInvolvement() {
        return factorsLimitInformalSupportInvolvement;
    }

    public void setFactorsLimitInformalSupportInvolvement(String factorsLimitInformalSupportInvolvement) {
        this.factorsLimitInformalSupportInvolvement = factorsLimitInformalSupportInvolvement;
    }

    public boolean isFactorsTransportation() {
        return factorsTransportation;
    }

    public void setFactorsTransportation(boolean factorsTransportation) {
        this.factorsTransportation = factorsTransportation;
    }

    public boolean isFactorsFinances() {
        return factorsFinances;
    }

    public void setFactorsFinances(boolean factorsFinances) {
        this.factorsFinances = factorsFinances;
    }

    public boolean isFactorsFamily() {
        return factorsFamily;
    }

    public void setFactorsFamily(boolean factorsFamily) {
        this.factorsFamily = factorsFamily;
    }

    public boolean isFactorsJob() {
        return factorsJob;
    }

    public void setFactorsJob(boolean factorsJob) {
        this.factorsJob = factorsJob;
    }

    public boolean isFactorsResponsibilities() {
        return factorsResponsibilities;
    }

    public void setFactorsResponsibilities(boolean factorsResponsibilities) {
        this.factorsResponsibilities = factorsResponsibilities;
    }

    public boolean isFactorsPhysicalBurden() {
        return factorsPhysicalBurden;
    }

    public void setFactorsPhysicalBurden(boolean factorsPhysicalBurden) {
        this.factorsPhysicalBurden = factorsPhysicalBurden;
    }

    public boolean isFactorsReliability() {
        return factorsReliability;
    }

    public void setFactorsReliability(boolean factorsReliability) {
        this.factorsReliability = factorsReliability;
    }

    public boolean isFactorsHealthProblems() {
        return factorsHealthProblems;
    }

    public void setFactorsHealthProblems(boolean factorsHealthProblems) {
        this.factorsHealthProblems = factorsHealthProblems;
    }

    public boolean isFactorsEmotionalBurden() {
        return factorsEmotionalBurden;
    }

    public void setFactorsEmotionalBurden(boolean factorsEmotionalBurden) {
        this.factorsEmotionalBurden = factorsEmotionalBurden;
    }

    public boolean isFactorsLivingDistance() {
        return factorsLivingDistance;
    }

    public void setFactorsLivingDistance(boolean factorsLivingDistance) {
        this.factorsLivingDistance = factorsLivingDistance;
    }

    public boolean isSerciceAdultDayServices() {
        return serciceAdultDayServices;
    }

    public void setSerciceAdultDayServices(boolean serciceAdultDayServices) {
        this.serciceAdultDayServices = serciceAdultDayServices;
    }

    public boolean isSercicePersonalCareLevelOne() {
        return sercicePersonalCareLevelOne;
    }

    public void setSercicePersonalCareLevelOne(boolean sercicePersonalCareLevelOne) {
        this.sercicePersonalCareLevelOne = sercicePersonalCareLevelOne;
    }

    public boolean isSercicePersonalCareLevelTwo() {
        return sercicePersonalCareLevelTwo;
    }

    public void setSercicePersonalCareLevelTwo(boolean sercicePersonalCareLevelTwo) {
        this.sercicePersonalCareLevelTwo = sercicePersonalCareLevelTwo;
    }

    public boolean isSerciceHomeContact() {
        return serciceHomeContact;
    }

    public void setSerciceHomeContact(boolean serciceHomeContact) {
        this.serciceHomeContact = serciceHomeContact;
    }

    public String getSerciceAsRespiteForCaregiver() {
        return serciceAsRespiteForCaregiver;
    }

    public void setSerciceAsRespiteForCaregiver(String serciceAsRespiteForCaregiver) {
        this.serciceAsRespiteForCaregiver = serciceAsRespiteForCaregiver;
    }

    public Long getCanOtherInformalSupportHelpToRelieveCaregiver() {
        return canOtherInformalSupportHelpToRelieveCaregiver;
    }

    public void setCanOtherInformalSupportHelpToRelieveCaregiver(Long canOtherInformalSupportHelpToRelieveCaregiver) {
        this.canOtherInformalSupportHelpToRelieveCaregiver = canOtherInformalSupportHelpToRelieveCaregiver;
    }

    public String getCanOtherInformalSupportHelpToRelieveCaregiverComment() {
        return canOtherInformalSupportHelpToRelieveCaregiverComment;
    }

    public void setCanOtherInformalSupportHelpToRelieveCaregiverComment(String canOtherInformalSupportHelpToRelieveCaregiverComment) {
        this.canOtherInformalSupportHelpToRelieveCaregiverComment = canOtherInformalSupportHelpToRelieveCaregiverComment;
    }

    public Long getHasAffiliationToProvideAssistance() {
        return hasAffiliationToProvideAssistance;
    }

    public void setHasAffiliationToProvideAssistance(Long hasAffiliationToProvideAssistance) {
        this.hasAffiliationToProvideAssistance = hasAffiliationToProvideAssistance;
    }

    public String getHasAffiliationToProvideAssistanceComment() {
        return hasAffiliationToProvideAssistanceComment;
    }

    public void setHasAffiliationToProvideAssistanceComment(String hasAffiliationToProvideAssistanceComment) {
        this.hasAffiliationToProvideAssistanceComment = hasAffiliationToProvideAssistanceComment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReserved_1() {
        return reserved_1;
    }

    public void setReserved_1(String reserved_1) {
        this.reserved_1 = reserved_1;
    }

    public String getReserved_2() {
        return reserved_2;
    }

    public void setReserved_2(String reserved_2) {
        this.reserved_2 = reserved_2;
    }

    public String getReserved_3() {
        return reserved_3;
    }

    public void setReserved_3(String reserved_3) {
        this.reserved_3 = reserved_3;
    }

    public String getSupportPeopleAddress() {
        return supportPeopleAddress;
    }

    public void setSupportPeopleAddress(String supportPeopleAddress) {
        this.supportPeopleAddress = supportPeopleAddress;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    private Long id;

    private Long haveHelpWithCare;

    private String supportPeopleFirstName;

    private String supportPeopleMiddleName;

    private String supportPeopleLastName;

    private String supportPeopleRelation;

    private String supportPeopleEmail;

    private String supportPeople;

    private String supportPeopleHomePhone;

    private String supportPeopleWorkPhone;

    private String supportPeopleCellPhone;

    private String involvement;

    private Long customerHasGoodRelationshipWithSupport;

    private Long customerWillAcceptHelpToRemainHomeOrIndependence;

    private String factorsLimitInformalSupportInvolvement;

    private boolean factorsTransportation;

    private boolean factorsFinances;

    private boolean factorsFamily;

    private boolean factorsJob;

    private boolean factorsResponsibilities;

    private boolean factorsPhysicalBurden;

    private boolean factorsReliability;

    private boolean factorsHealthProblems;

    private boolean factorsEmotionalBurden;

    private boolean factorsLivingDistance;

    private boolean serciceAdultDayServices;

    private boolean sercicePersonalCareLevelOne;

    private boolean sercicePersonalCareLevelTwo;

    private boolean serciceHomeContact;

    private String serciceAsRespiteForCaregiver;

    private Long canOtherInformalSupportHelpToRelieveCaregiver;

    private String canOtherInformalSupportHelpToRelieveCaregiverComment;

    private Long hasAffiliationToProvideAssistance;

    private String hasAffiliationToProvideAssistanceComment;

    private String comment;

    private String reserved_1;

    private String reserved_2;

    private String reserved_3;

    private String supportPeopleAddress;

    private Long userId;
}
