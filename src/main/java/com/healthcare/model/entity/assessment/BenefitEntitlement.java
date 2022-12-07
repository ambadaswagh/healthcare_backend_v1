package com.healthcare.model.entity.assessment;

import com.healthcare.model.entity.Audit;
import com.healthcare.model.entity.User;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by inimn on 25/12/2017.
 */
@Entity
@Table(name = "benefit_entitlement")
public @Data class BenefitEntitlement extends Audit implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "benefit_status_code")
    private String benefitStatusCode;

    @Column(name = "social_security_code")
    private String socialSecurityCode;

    @Column(name = "social_security_comment")
    private String socialSecurityComment;

    @Column(name = "ssl_code")
    private String sslCode;

    @Column(name = "sslComment")
    private String sslComment;

    @Column(name = "railroad_retirement_code")
    private String railroadRetirementCode;

    @Column(name = "railroad_retirement_comment")
    private String railroadRetirementComment;

    @Column(name = "ssd_code")
    private String ssdCode;

    @Column(name = "ssd_comment")
    private  String ssdComment;

    @Column(name = "veteran_benefits_specify_code")
    private String veteranBenefitsSpecifyCode;

    @Column(name = "veteran_benefits_specify_comment")
    private String veteranBenefitsSpecifyComment;

    @Column(name = "other_income_related_benefits_code")
    private String otherIncomeRelatedBenefitsCode;

    @Column(name = "other_income_related_benefits_comment")
    private String otherIncomeRelatedBenefitsComment;

    @Column(name = "medicaid_num_code")
    private String medicaidNumCode;

    @Column(name = "medicaid_num_comment")
    private String medicaidNumComment;

    @Column(name = "food_stamp_snap_code")
    private String foodStampSnapCode;

    @Column(name = "food_stamp_snap_comment")
    private String foodStampSnapComment;

    @Column(name = "public_assistance_specify_code")
    private String publicAssistanceSpecifyCode;

    @Column(name = "public_assistance_specify_comment")
    private String publicAssistanceSpecifyComment;

    @Column(name = "other_specify_code")
    private String otherSpecifyCode;

    @Column(name = "other_specify_comment")
    private String otherSpecifyComment;

    @Column(name = "other_entitlement_code")
    private String otherEntitlementCode;

    @Column(name = "other_entitlement_comment")
    private String otherEntitlementComment;

    @Column(name = "medicare_num_code")
    private String medicareNumCode;

    @Column(name = "medicare_num_comment")
    private String medicareNumComment;

    @Column(name = "qmb_code")
    private String qmbCode;

    @Column(name = "qmb_comment")
    private String qmbComment;

    @Column(name = "slimb_code")
    private String slimbCode;

    @Column(name = "slimb_comment")
    private String slimbComment;

    @Column(name = "epic_code")
    private String epicCode;

    @Column(name = "epic_comment")
    private String epicComment;

    @Column(name = "medicare_part_d_code")
    private String medicarePartCode;

    @Column(name = "medicare_part_d_comment")
    private String medicarePartComment;

    @Column(name = "medigap_insurance_hmo_specify_code")
    private String medigapInsuranceHmoSpecifyCode;

    @Column(name = "medigap_insurance_hmo_specify_comment")
    private String medigapInsuranceHmoSpecifyComment;

    @Column(name = "long_term_care_insurance_specify_code")
    private String longTermCareInsuranceSpecifyCode;

    @Column(name = "long_term_care_insurance_specify_comment")
    private String longTermCareInsuranceSpecifyComment;

    @Column(name = "other_health_insurance_specify_code")
    private String otherHealthInsuranceSpecifyCode;

    @Column(name = "other_health_insurance_specify_comment")
    private String otherHealthInsuranceSpecifyComment;

    @Column(name = "scrie_code")
    private String scrieCode;

    @Column(name = "scrie_comment")
    private String scrieComment;

    @Column(name = "section_eight_code")
    private String sectionEightCode;

    @Column(name = "section_eight_comment")
    private String sectionEightComment;

    @Column(name = "ittwo_one_four_code")
    private String ittwoOneFourCode;

    @Column(name = "ittwo_one_four_comment")
    private String ittwoOneFourComment;

    @Column(name = "veteran_tax_exemption_code")
    private String veteranTaxExemptionCode;

    @Column(name = "veteran_tax_exemption_comment")
    private String veteranTaxExemptionComment;

    @Column(name = "reverse_mortgage_code")
    private String reverseMortgageCode;

    @Column(name = "reverse_mortgage_comment")
    private String reverseMortgageComment;

    @Column(name = "real_property_tax_exemption_star_code")
    private String realPropertyTaxExemptionStarCode;

    @Column(name = "real_property_tax_exemption_star_comment")
    private String realPropertyTaxExemptionStarComment;

    @Column(name = "heap_code")
    private String heapCode;

    @Column(name = "heap_comment")
    private String heapComment;

    @Column(name = "other_housing_related_benefit_code")
    private String otherHousingRelatedBenefitCode;

    @Column(name = "other_housing_related_benefit_comment")
    private String otherHousingRelatedBenefitComment;

    @Column(name = "comment")
    private String comment;

    @Column(name = "reserved_1")
    private String reserved_1;

    @Column(name = "reserved_2")
    private String reserved_2;

    @Column(name = "reserved_3")
    private String reserved_3;

    @Column(name = "user_id")
    private Long userId;

}
