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
@Table(name = "information_support")
public @Data class InformationSupport extends Audit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "have_help_with_care")
    private Long haveHelpWithCare;

    @Column(name = "support_people_first_name")
    private String supportPeopleFirstName;

    @Column(name = "support_people_middle_name")
    private String supportPeopleMiddleName;

    @Column(name = "support_people_last_name")
    private String supportPeopleLastName;

    @Column(name = "support_people_relation")
    private String supportPeopleRelation;

    @Column(name = "support_people_email")
    private String supportPeopleEmail;

    @Column(name = "support_people")
    private String supportPeople;

    @Column(name = "support_people_home_phone")
    private String supportPeopleHomePhone;

    @Column(name = "support_people_work_phone")
    private String supportPeopleWorkPhone;

    @Column(name = "support_people_cell_phone")
    private String supportPeopleCellPhone;

    @Column(name = "involvement")
    private String involvement;

    @Column(name = "customer_has_good_relationship_with_support")
    private Long customerHasGoodRelationshipWithSupport;

    @Column(name = "customer_will_accept_help_to_remain_home_or_independence")
    private Long customerWillAcceptHelpToRemainHomeOrIndependence;

    @Column(name = "factors_limit_informal_support_involvement")
    private String factorsLimitInformalSupportInvolvement;

    @Column(name = "sercice_as_respite_for_caregiver")
    private String serciceAsRespiteForCaregiver;

    @Column(name = "can_other_informal_support_help_to_relieve_caregiver")
    private Long canOtherInformalSupportHelpToRelieveCaregiver;

    @Column(name = "can_other_informal_support_help_to_relieve_caregiver_comment")
    private String canOtherInformalSupportHelpToRelieveCaregiverComment;

    @Column(name = "has_affiliation_to_provide_assistance")
    private Long hasAffiliationToProvideAssistance;

    @Column(name = "has_affiliation_to_provide_assistance_comment")
    private String hasAffiliationToProvideAssistanceComment;

    @Column(name = "comment")
    private String comment;

    @Column(name = "reserved_1")
    private String reserved_1;

    @Column(name = "reserved_2")
    private String reserved_2;

    @Column(name = "reserved_3")
    private String reserved_3;

    @Column(name = "support_people_address")
    private String supportPeopleAddress;

    @Column(name = "user_id")
    private Long userId;
}
