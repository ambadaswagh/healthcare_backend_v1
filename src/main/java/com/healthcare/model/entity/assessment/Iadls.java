package com.healthcare.model.entity.assessment;

/**
 * Created by inimn on 25/12/2017.
 */

import com.healthcare.model.entity.Audit;
import com.healthcare.model.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "iadls")
@EqualsAndHashCode(callSuper = true)
public @Data class Iadls extends Audit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "activity_status_housing_cleaning")
    private String activityStatusHousingCleaning;

    @Column(name = "housing_cleaning_self")
    private Long housingCleaningSelf;

    @Column(name = "housing_cleaning_formal_service")
    private Long housingCleaningFormalService;

    @Column(name = "housing_cleaning_informal_support")
    private Long housingCleaningInformalSupport;

    @Column(name = "housing_cleaning_other")
    private Long housingCleaningOther;

    @Column(name = "housing_cleaning_other_comment")
    private String housingCleaningOtherComment;

    @Column(name = "activity_status_housing_cleaning_other")
    private String activityStatusHousingCleaningOther;

    @Column(name = "activity_status_shopping")
    private String activityStatusShopping;

    @Column(name = "shopping_self")
    private Long shoppingSelf;

    @Column(name = "shopping_formal_service")
    private Long shoppingFormalService;

    @Column(name = "shopping_informal_support")
    private Long shoppingInformalSupport;

    @Column(name = "shopping_other")
    private Long shoppingOther;

    @Column(name = "shopping_other_comment")
    private String shoppingOtherComment;

    @Column(name = "activity_status_shopping_other")
    private String activityStatusShoppingOther;

    @Column(name = "activity_status_laundry")
    private String activityStatusLaundry;

    @Column(name = "laundry_self")
    private Long laundrySelf;

    @Column(name = "laundry_formal_service")
    private Long laundryFormalService;

    @Column(name = "laundry_informal_support")
    private Long laundryInformalSupport;

    @Column(name = "laundry_other")
    private Long laundryOther;

    @Column(name = "laundry_other_comment")
    private String laundryOtherComment;

    @Column(name = "activity_status_laundry_other")
    private String activityStatusLaundryOther;

    @Column(name = "activity_status_use_transportation")
    private String activityStatusUseTransportation;

    @Column(name = "transportation_self")
    private Long transportationSelf;

    @Column(name = "transportation_formal_service")
    private Long transportationFormalService;

    @Column(name = "transportation_informal_support")
    private Long transportationInformalSupport;

    @Column(name = "transportation_other")
    private Long transportationOther;

    @Column(name = "transportation_other_comment")
    private String transportationOtherComment;

    @Column(name = "activity_status_use_transportation_other")
    private String activityStatusUseTransportationOther;

    @Column(name = "activity_status_prepare_cook_meal")
    private String activityStatusPrepareCookMeal;

    @Column(name = "prepare_self")
    private Long prepareSelf;

    @Column(name = "prepare_formal_service")
    private Long prepareFormalService;

    @Column(name = "prepare_informal_support")
    private Long prepareInformalSupport;

    @Column(name = "prepare_other")
    private Long prepareOther;

    @Column(name = "prepare_other_comment")
    private String prepareOtherComment;

    @Column(name = "activity_status_prepare_cook_meal_other")
    private String activityStatusPrepareCookMealOther;

    @Column(name = "activity_status_handle_personal_finance_business")
    private String activityStatusHandlePersonalFinanceBusiness;

    @Column(name = "business_self")
    private Long businessSelf;

    @Column(name = "business_formal_service")
    private Long businessFormalService;

    @Column(name = "business_informal_support")
    private Long businessInformalSupport;

    @Column(name = "business_other")
    private Long businessOther;

    @Column(name = "business_other_comment")
    private String businessOtherComment;

    @Column(name = "activity_status_handle_personal_finance_business_other")
    private String activityStatusHandlePersonalFinanceBusinessOther;

    @Column(name = "activity_status_use_template")
    private String activityStatusUseTemplate;

    @Column(name = "use_self")
    private Long useSelf;

    @Column(name = "use_formal_service")
    private Long useFormalService;

    @Column(name = "use_informal_support")
    private Long useInformalSupport;

    @Column(name = "use_other")
    private Long useOther;

    @Column(name = "use_other_comment")
    private String useOtherComment;

    @Column(name = "activity_status_use_template_other")
    private String activityStatusUseTemplateOther;

    @Column(name = "activity_status_administering_of_medication")
    private String activityStatusAdministeringOfMedication;

    @Column(name = "medication_self")
    private Long medicationSelf;

    @Column(name = "medication_formal_service")
    private Long medicationFormalService;

    @Column(name = "medication_informal_support")
    private Long medicationInformalSupport;

    @Column(name = "medication_other")
    private Long medicationOther;

    @Column(name = "medication_other_comment")
    private String medicationOtherComment;

    @Column(name = "activity_status_administering_of_medication_other")
    private String activityStatusAdministeringOfMedicationOther;

    @Column(name = "bathing")
    private String bathing;

    @Column(name = "bathing_no_supervision")
    private Long bathingNoSupervision;

    @Column(name = "bathing_intermittent_supervision")
    private Long bathingIntermittentSupervision;

    @Column(name = "bathing_physical_assistance")
    private Long bathingPhysicalAssistance;

    @Column(name = "bathing_not_participater")
    private Long bathingNotParticipate;

    @Column(name = "personal_hygiene")
    private String personalHygiene;

    @Column(name = "hygiene_no_supervision")
    private Long hygieneNoSupervision;

    @Column(name = "hygiene_intermittent_supervision")
    private Long hygieneIntermittentSupervision;

    @Column(name = "hygiene_physical_assistance")
    private Long hygienePhysicalAssistance;

    @Column(name = "hygiene_not_participater")
    private Long hygieneNotParticipate;

    @Column(name = "dressing")
    private String dressing;

    @Column(name = "dressing_no_supervision")
    private Long dressingNoSupervision;

    @Column(name = "dressing_intermittent_supervision")
    private Long dressingIntermittentSupervision;

    @Column(name = "dressing_physical_assistance")
    private Long dressingPhysicalAssistance;

    @Column(name = "dressing_not_participater")
    private Long dressingNotParticipate;

    @Column(name = "mobility")
    private String mobility;

    @Column(name = "mobility_no_supervision")
    private Long mobilityNoSupervision;

    @Column(name = "mobility_intermittent_supervision")
    private Long mobilityIntermittentSupervision;

    @Column(name = "mobility_physical_assistance")
    private Long mobilityPhysicalAssistance;

    @Column(name = "mobility_not_participater")
    private Long mobilityNotParticipate;

    @Column(name = "transfer")
    private String transfer;

    @Column(name = "transfer_no_supervision")
    private Long transferNoSupervision;

    @Column(name = "transfer_intermittent_supervision")
    private Long transferIntermittentSupervision;

    @Column(name = "transfer_physical_assistance")
    private Long transferPhysicalAssistance;

    @Column(name = "transfer_not_participater")
    private Long transferNotParticipate;

    @Column(name = "toileting")
    private String toileting;

    @Column(name = "toileting_no_supervision")
    private Long toiletingNoSupervision;

    @Column(name = "toileting_intermittent_supervision")
    private Long toiletingIntermittentSupervision;

    @Column(name = "toileting_physical_assistance")
    private Long toiletingPhysicalAssistance;

    @Column(name = "toileting_not_participater")
    private Long toiletingNotParticipate;

    @Column(name = "eating")
    private String eating;

    @Column(name = "eating_no_supervision")
    private Long eatingNoSupervision;

    @Column(name = "eating_intermittent_supervision")
    private Long eatingIntermittentSupervision;

    @Column(name = "eating_physical_assistance")
    private Long eatingPhysicalAssistance;

    @Column(name = "eating_not_participater")
    private Long eatingNotParticipate;

    @Column(name = "reserved_1")
    private String reserved_1;

    @Column(name = "reserved_2")
    private String reserved_2;

    @Column(name = "reserved_3")
    private String reserved_3;

    @Column(name = "user_id")
    private Long userId;

}