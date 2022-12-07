package com.healthcare.model.entity.review;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Model class that contains part data of review - indicators of diseases
 */
@EqualsAndHashCode
public @Data class HealthConditionIndicators implements Serializable {

	private static final long serialVersionUID = -7485622969856051258L;

	@JsonProperty("hc_high_blood_pressure")
	private Boolean highBloodPressure;
	@JsonProperty("hc_low_blood_pressure")
	private Boolean lowBloodPressure;
	@JsonProperty("hc_neck_pain")
	private Boolean neckPain;
	@JsonProperty("hc_back_pain")
	private Boolean backPain;
	@JsonProperty("hc_shoulder_pain")
	private Boolean shoulderPain;
	@JsonProperty("hc_hearing")
	private Boolean hearing;
	@JsonProperty("hc_dizziness")
	private Boolean dizziness;
	@JsonProperty("hc_alzheimer")
	private Boolean alzheimer;
	@JsonProperty("hc_difficulty_walking")
	private Boolean difficultyOfWalking;
	@JsonProperty("hc_rheumatism")
	private Boolean rheumatism;
	@JsonProperty("hc_diabetes")
	private Boolean diabetes;
	@JsonProperty("hc_leg_pain")
	private Boolean legPain;
	@JsonProperty("hc_high_cholesterol")
	private Boolean highCholesterol;
	@JsonProperty("hc_low_cholesterol")
	private Boolean lowCholesterol;
	@JsonProperty("hc_high_triglyceride")
	private Boolean highTriglyceride;
	@JsonProperty("hc_low_triglyceride")
	private Boolean lowTriglyceride;
	@JsonProperty("hc_heart_problems")
	private Boolean heartProblems;
	@JsonProperty("hc_vision")
	private Boolean vision;
	@JsonProperty("hc_parkinson")
	private Boolean parkinson;
	@JsonProperty("hc_arthritis")
	private Boolean arthritis;
	@JsonProperty("hc_history_falling")
	private Boolean historyOfFailing;
	@JsonProperty("hc_wheel_chair")
	private Boolean wheelchair;
	@JsonProperty("hc_prostate")
	private Boolean prostate;
	@JsonProperty("hc_thyroid")
	private Boolean thyroid;
}
