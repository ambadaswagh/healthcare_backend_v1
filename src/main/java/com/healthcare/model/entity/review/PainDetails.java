package com.healthcare.model.entity.review;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Model class that contains part data of review
 */
@EqualsAndHashCode
public @Data class PainDetails implements Serializable {

	private static final long serialVersionUID = 3561507402159144522L;

	@JsonProperty("pain_most")
	private String mostPain;
	@JsonProperty("pain_least")
	private String leastPain;
	@JsonProperty("pain_exacerbated_by")
	private String exacerbatedBy;
	@JsonProperty("pain_body_part")
	private String bodyPart;
	@JsonProperty("pain_body_side")
	private String bodySide;
	@JsonProperty("pain_body_location")
	private String bodyLocation;
	@JsonProperty("pain_relieved_by")
	private String relievedBy;
	@JsonProperty("pain_effect_of_control")
	private String effectOfControl;
}
