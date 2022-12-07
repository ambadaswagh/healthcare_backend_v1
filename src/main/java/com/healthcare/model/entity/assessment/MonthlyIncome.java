package com.healthcare.model.entity.assessment;

/**
 * Created by inimn on 25/12/2017.
 */

import com.healthcare.model.entity.Audit;
import com.healthcare.model.entity.User;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "monthly_income")
public @Data class MonthlyIncome extends Audit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "individual_social_security_net")
    private String individualSocialSecurityNet;

    @Column(name = "individual_supplemental_security_income")
    private String individualSupplementalSecurityIncome;

    @Column(name = "individual_personal_retirement_income")
    private String individualPersonalRetirementIncome;

    @Column(name = "individual_interest")
    private String individualInterest;

    @Column(name = "individual_dividends")
    private String individualDividends;

    @Column(name = "individual_salary_wage")
    private String individualSalaryWage;

    @Column(name = "individual_other")
    private String individualOther;

    @Column(name = "individual_total")
    private String individualTotal;

    @Column(name = "spouse_social_security_net")
    private String spouseSocialSecurityNet;

    @Column(name = "spouse_supplemental_security_income")
    private String spouseSupplementalSecurityIncome;

    @Column(name = "spouse_personal_retirement_income")
    private String spousePersonalRetirementIncome;

    @Column(name = "spouse_interest")
    private String spouseInterest;

    @Column(name = "spouse_dividends")
    private String spouseDividends;

    @Column(name = "spouse_salary_wage")
    private String spouseSalaryWage;

    @Column(name = "spouse_other")
    private String spouseOther;

    @Column(name = "spouse_total")
    private String spouseTotal;

    @Column(name = "other_family_household_social_security_net")
    private String otherFamilyHouseholdSocialSecurityNet;

    @Column(name = "other_family_household_supplemental_security_income")
    private String otherFamilyHouseholdSupplementalSecurityIncome;

    @Column(name = "other_family_household_personal_retirement_income")
    private String otherFamilyHouseholdPersonalRetirementIncome;

    @Column(name = "other_family_household_interest")
    private String otherFamilyHouseholdInterest;

    @Column(name = "other_family_household_dividends")
    private String otherFamilyHouseholdDividends;

    @Column(name = "other_family_household_salary_wage")
    private String otherFamilyHouseholdSalaryWage;

    @Column(name = "other_family_household_other")
    private String otherFamilyHouseholdOther;

    @Column(name = "other_family_household_total")
    private String otherFamilyHouseholdTotal;

    @Column(name = "total_family_household_social_security_net")
    private String totalFamilyHouseholdSocialSecurityNet;

    @Column(name = "total_family_household_supplemental_security_income")
    private String totalFamilyHouseholdSupplementalSecurityIncome;

    @Column(name = "total_family_household_personal_retirement_income")
    private String totalFamilyHouseholdPersonalRetirementIncome;

    @Column(name = "total_family_household_interest")
    private String totalFamilyHouseholdInterest;

    @Column(name = "total_family_household_dividends")
    private String totalFamilyHouseholdDividends;

    @Column(name = "total_family_household_salary_wage")
    private String totalFamilyHouseholdSalaryWage;

    @Column(name = "total_family_household_other")
    private String totalFamilyHouseholdOther;

    @Column(name = "total_family_household_total")
    private String totalFamilyHouseholdTotal;

    @Column(name = "reserved_1")
    private String reserved_1;

    @Column(name = "reserved_2")
    private String reserved_2;

    @Column(name = "reserved_3")
    private String reserved_3;

    @Column(name = "user_id")
    private Long userId;
}
