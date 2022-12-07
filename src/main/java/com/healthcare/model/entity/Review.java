package com.healthcare.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthcare.model.entity.review.ActivityDetails;
import com.healthcare.model.entity.review.CardPulmCondition;
import com.healthcare.model.entity.review.CommunicationHearingCondition;
import com.healthcare.model.entity.review.FunctionalStatus;
import com.healthcare.model.entity.review.HealthCondition;
import com.healthcare.model.entity.review.NutritionCondition;
import com.healthcare.model.entity.review.PainDetails;
import com.healthcare.model.entity.review.PsychologicalSocialCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "review")
@JsonInclude(Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
public @Data class Review extends Audit implements Serializable {

    private static final long serialVersionUID = 8451585526769991568L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "health_condition")
    @Type(type = "com.healthcare.model.entity.usertype.review.HealthConditionUserType")
    private HealthCondition healthCondition;
    @Column(name = "card_pulm_condition")
    @Type(type = "com.healthcare.model.entity.usertype.review.CardPulmConditionUserType")
    private CardPulmCondition cardPulmCondition;
    @Column(name = "activity_details")
    @Type(type = "com.healthcare.model.entity.usertype.review.ActivityDetailsUserType")
    private ActivityDetails activityDetails;
    @Column(name = "pain_details")
    @Type(type = "com.healthcare.model.entity.usertype.review.PainDetailsUserType")
    private PainDetails painDetails;
    @Column(name = "fuctional_status_adls_iadls")
    @Type(type = "com.healthcare.model.entity.usertype.review.FunctionalStatusUserType")
    private FunctionalStatus functionalStatus;
    @Column(name = "nutrition_condition")
    @Type(type = "com.healthcare.model.entity.usertype.review.NutritionConditionUserType")
    private NutritionCondition nutritionCondition;
    @Column(name = "communication_hearing_condition")
    @Type(type = "com.healthcare.model.entity.usertype.review.CommunicationHearingConditionUserType")
    private CommunicationHearingCondition communicationHearingCondition;
    @Column(name = "psychological_social_condition")
    @Type(type = "com.healthcare.model.entity.usertype.review.PsychologicalSocialConditionUserType")
    private PsychologicalSocialCondition psychologicalSocialCondition;

    @Column(name = "assessment_reason")
    private String assessmentReason;
    @Column(name = "assessment_reason_other")
    private String assessmentReasonOther;
    @Column(name = "assessment_source_information")
    private String assessmentSourceInformation;
    @Column(name = "assessment_date")
    private Timestamp assessmentDate;

    @Column(name = "discharge_reason")
    private String dischargeReason;
    @Column(name = "discharge_comments")
    private String dischargeComments;
    @Column(name = "discharge_date")
    private Timestamp dischargeDate;

    @Column(name = "status")
    private String status;

    @Column(name = "bp")
    private String bp;

    @Column(name = "respiration")
    private String respiration;

    @Column(name = "source_of_information")
    private String sourceOfInformation;

    @Column(name = "proxy")
    private String proxy;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "dnr")
    private String dnr;

    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "race_ethnicity")
    private String raceEthnicity;

    @Column(name = "creed")
    private String creed;

    @Column(name = "habbits")
    private String habbits;

    @Column(name = "skills")
    private String skills;

    @Column(name = "primary_speaking_language")
    private String primarySpeakingLanguage;

    @Column(name = "living_arrangement")
    private String livingArrangement;

    @Column(name = "possible_abuse")
    private String possibleAbuse;

    @Column(name = "was_abuse_refer_to")
    private String wasAbuseReferTo;

    @Column(name = "is_client_frail")
    private String isClientFrail;

    @Column(name = "is_client_disabled")
    private String isClientDisabled;

    @Column(name = "home_phone_number")
    private String homePhoneNumber;

    @Column(name = "cellphone_number")
    private String cellphoneNumber;

    @Column(name = "pulse")
    private String pulse;

    @Column(name = "assessment_user_id")
    private Long assessmentUserId;
}