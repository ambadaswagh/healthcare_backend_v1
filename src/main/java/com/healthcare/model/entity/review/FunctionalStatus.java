package com.healthcare.model.entity.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Model class that contains part data of review
 */
@EqualsAndHashCode
public @Data class FunctionalStatus implements Serializable {

    private static final long serialVersionUID = 2621992055909181031L;

    @JsonProperty("fs_grooming")
    private Boolean grooming;
    @JsonProperty("fs_dental_hygiene")
    private Boolean dentalHygiene;
    @JsonProperty("fs_mobility_assistance")
    private Boolean mobilityAssistance;
    @JsonProperty("fs_washing")
    private Boolean washing;
    @JsonProperty("fs_toileting")
    private Boolean toileting;
    @JsonProperty("fs_haircut")
    private Boolean haircut;
    @JsonProperty("fs_skincare")
    private Boolean skincare;
    @JsonProperty("fs_eating")
    private Boolean eating;
    @JsonProperty("fs_none")
    private Boolean none;
    @JsonProperty("fs_incontinence_urine")
    private Boolean incontinenceUrine;
    @JsonProperty("fs_use_bowel")
    private Boolean useBowel;
}
