package com.healthcare.model.entity.review;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Model class that contains part data of review
 */
@EqualsAndHashCode
public @Data class HealthCondition implements Serializable {

	private static final long serialVersionUID = 6305521928553443251L;

	@JsonProperty("type_medication_taking")
	private String typeMedicationTaking;
	@JsonProperty("hc_req_cueing_take_medication")
	private Boolean requireCueingForMedication;
	@JsonProperty("hc_food_allergies")
	private String foodAllergies;
	@JsonProperty("hc_hobbies_interests")
	private String hobbiesInterests;
	@JsonProperty("hc_smoker")
	private Boolean smoker;

	@JsonUnwrapped
	private HealthConditionIndicators healthConditionIndicators;

	@JsonUnwrapped
	private BodyStatus bodyStatus;

	@JsonUnwrapped
	private HealthProblems healthProblems;
}
