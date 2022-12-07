package com.healthcare.model.entity.review;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Model class that contains part data of review
 */
@EqualsAndHashCode
public @Data class CardPulmCondition implements Serializable {

	private static final long serialVersionUID = -109068866504327381L;

	@JsonProperty("cp_chest_pain")
	private Boolean chestPain;
	@JsonProperty("cp_dizzy")
	private Boolean dizzy;
	@JsonProperty("cp_shortness_breath")
	private Boolean shortnessOfBreath;
	@JsonProperty("cp_cough")
	private Boolean cough;
	@JsonProperty("cp_headache")
	private Boolean headache;
}
