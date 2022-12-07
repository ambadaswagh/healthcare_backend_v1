package com.healthcare.model.entity.assessment;

/**
 * Created by inimn on 25/12/2017.
 */

import com.healthcare.model.entity.Audit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "medicine_taken")
@EqualsAndHashCode(callSuper = true)
public @Data class MedicineTaken extends Audit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "medication_name_one")
    private String medicationNameOne;

    @Column(name = "medication_dose_one")
    private String medicationDoseOne;

    @Column(name = "reason_taken_one")
    private String reasonTakenOne;

    @Column(name = "medication_name_two")
    private String medicationNameTwo;

    @Column(name = "medication_dose_two")
    private String medicationDoseTwo;

    @Column(name = "reason_taken_two")
    private String reasonTakenTwo;

    @Column(name = "medication_name_three")
    private String medicationNameThree;

    @Column(name = "medication_dose_three")
    private String medicationDoseThree;

    @Column(name = "reason_taken_three")
    private String reasonTakenThree;

    @Column(name = "medication_name_four")
    private String medicationNameFour;

    @Column(name = "medication_dose_four")
    private String medicationDoseFour;

    @Column(name = "reason_taken_four")
    private String reasonTakenFour;

    @Column(name = "medication_name_five")
    private String medicationNameFive;

    @Column(name = "medication_dose_five")
    private String medicationDoseFive;

    @Column(name = "reason_taken_five")
    private String reasonTakenFive;

    @Column(name = "medication_name_six")
    private String medicationNameSix;

    @Column(name = "medication_dose_six")
    private String medicationDoseSix;

    @Column(name = "reason_taken_six")
    private String reasonTakenSix;

    @Column(name = "medication_name_seven")
    private String medicationNameSeven;

    @Column(name = "medication_dose_seven")
    private String medicationDoseSeven;

    @Column(name = "reason_taken_seven")
    private String reasonTakenSeven;

    @Column(name = "medication_name_eight")
    private String medicationNameEight;

    @Column(name = "medication_dose_eight")
    private String medicationDoseEight;

    @Column(name = "reason_taken_eight")
    private String reasonTakenEight;

    @Column(name = "adverser_reaction_allergy_sensitivity")
    private Long adverserReactionAllergySensitivity;

    @Column(name = "adverser_reaction_allergy_sensitivity_comment")
    private String adverserReactionRllergySensitivityComment;

    @Column(name = "reserved_1")
    private String reserved_1;

    @Column(name = "reserved_2")
    private String reserved_2;

    @Column(name = "reserved_3")
    private String reserved_3;

    @Column(name = "user_id")
    private Long userId;

}