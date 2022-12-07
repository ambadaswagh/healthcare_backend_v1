package com.healthcare.model.entity.review;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Model class that contains part data of review
 */
@EqualsAndHashCode
public @Data class PsychologicalSocialCondition implements Serializable {

	private static final long serialVersionUID = -7682665445088811591L;

	@JsonProperty("psysoc_shortterm_memory_deficit")
	private Boolean shortTermMemoryDeficit;
	@JsonProperty("psysoc_sadness_depression")
	private Boolean sadnessDepression;
	@JsonProperty("psysoc_physical_aggression")
	private Boolean physicalAggression;
	@JsonProperty("psysoc_anxiety")
	private Boolean anxiety;
	@JsonProperty("psysoc_verbal_abuse")
	private Boolean verbalAbuse;
	@JsonProperty("psysoc_anger")
	private Boolean anger;
	@JsonProperty("psysoc_crying")
	private Boolean crying;
	@JsonProperty("psysoc_isolation")
	private Boolean isolation;
	@JsonProperty("psysoc_inappropriate_behavior")
	private Boolean inappropriateBehavior;
	@JsonProperty("psysoc_received_mental_treatment")
	private Boolean receivedMentalTreatment;
	@JsonProperty("psysoc_diagnosed_mental_problem")
	private Boolean diagnosedMentalProblem;
}
