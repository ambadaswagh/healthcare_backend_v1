package com.healthcare.model.entity.review;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Model class that contains part data of review
 */
@EqualsAndHashCode
public @Data class BodyStatus implements Serializable {

	private static final long serialVersionUID = -4398946972871596434L;

	@JsonProperty("bs_blood_pressure_systolic")
	private String bloodPressureSystolic;
	@JsonProperty("bs_blood_pressure_diastolic")
	private String bloodPressureDiastolic;
	@JsonProperty("bs_pulse")
	private String pulse;
	@JsonProperty("bs_weight")
	private String weight;
	@JsonProperty("bs_height")
	private String height;
	@JsonProperty("bs_comments")
	private String comments;
}
