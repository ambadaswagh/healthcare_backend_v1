package com.healthcare.model.entity.assessment;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.Audit;
import com.healthcare.model.entity.Review;
import com.healthcare.model.entity.User;

import lombok.Data;

@Entity
@Table(name = "assessment_user")
public @Data class AssessmentUser extends Audit implements Serializable {

    private static final long serialVersionUID = -7736516054335483358L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne
    @JoinColumn(name = "housing_id")
    private Housing housing;

    @ManyToOne
    @JoinColumn(name = "health_status_id")
    private HealthStatus healthStatus;

    @ManyToOne
    @JoinColumn(name = "nutrition_id")
    private Nutrition nutrition;

    @ManyToOne
    @JoinColumn(name = "psycho_social_status_id")
    private PsychoSocialStatus psychoSocialStatus;

    @ManyToOne
    @JoinColumn(name = "medicine_taken_id")
    private MedicineTaken medicineTaken;

    @ManyToOne
    @JoinColumn(name = "iadls_id")
    private Iadls iadls;

    @ManyToOne
    @JoinColumn(name = "service_for_client_id")
    private ServiceForClient serviceForClient;

    @ManyToOne
    @JoinColumn(name = "information_support_id")
    private InformationSupport informationSupport;

    @ManyToOne
    @JoinColumn(name = "monthly_income_id")
    private MonthlyIncome monthlyIncome;

    @ManyToOne
    @JoinColumn(name = "benefit_entitlement_id")
    private BenefitEntitlement BenefitEntitlement;

}