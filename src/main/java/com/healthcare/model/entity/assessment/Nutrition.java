package com.healthcare.model.entity.assessment;

/**
 * Created by inimn on 25/12/2017.
 */

import com.healthcare.model.entity.Audit;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "nutrition")
public @Data class Nutrition extends Audit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "height")
    private String height;
    
    @Column(name = "weight")
    private String weight;
    
    @Column(name = "allergy")
    private String allergy;
    
    @Column(name = "body_mass_index")
    private String bodyMassIndex;

    @Column(name = "refrigerator_or_freezer_facilities_adequate")
    private Long refrigeratorOrFreezerFacilitiesAdequate;

    @Column(name = "open_containers_or_cartons_cut_food")
    private Long openContainersOrCartonsCutFood;

    @Column(name = "physician_prescribed_therapeutic_diet")
    private String physicianPrescribedTherapeuticDiet;

    @Column(name = "alcohol_screening_test")
    private String alcoholScreeningTest;

    @Column(name = "felt_cut_down_drinking")
    private String feltCutDownDrinking;

    @Column(name = "annoyed_you_by_criticize_your_drinking")
    private String annoyedYouByCriticizeYourDrinking;

    @Column(name = "felt_bad_or_guilty_about_drinking")
    private String feltBadOrQuiltyAboutDrinking;

    @Column(name = "need_drink_in_morning")
    private String needDrinkInMorning;

    @Column(name = "nutritional_risk_status")
    private String nutritionalRiskStatus;

    @Column(name = "nutritional_score_one")
    private Long nutritionalScoreOne;

    @Column(name = "nutritional_score_two")
    private Long nutritionalScoreTwo;

    @Column(name = "nutritional_score_three")
    private Long nutritionalScoreThree;

    @Column(name = "nutritional_score_four")
    private Long nutritionalScoreFour;

    @Column(name = "nutritional_score_five")
    private Long nutritionalScoreFive;

    @Column(name = "nutritional_score_six")
    private Long nutritionalScoreSix;

    @Column(name = "nutritional_score_seven")
    private Long nutritionalScoreSeven;

    @Column(name = "nutritional_score_eight")
    private Long nutritionalScoreEight;

    @Column(name = "nutritional_score_nine")
    private Long nutritionalScoreNine;

    @Column(name = "nutritional_score_ten")
    private Long nutritionalScoreTen;

    @Column(name = "nsi_score")
    private String nsiScore;

    @Column(name = "conclusion")
    private String conclusion;

    @Column(name = "reserved_1")
    private String reserved_1;

    @Column(name = "reserved_2")
    private String reserved_2;

    @Column(name = "reserved_3")
    private String reserved_3;
    
    @Column(name = "user_id")
    private Long userId;

}
