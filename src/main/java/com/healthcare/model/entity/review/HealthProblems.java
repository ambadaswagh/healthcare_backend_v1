package com.healthcare.model.entity.review;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Model class that contains part data of review - health problems
 */
@EqualsAndHashCode
public @Data class HealthProblems implements Serializable {

	private static final long serialVersionUID = 4720934996534407855L;

	@JsonProperty("hp_hypertension")
	private Boolean hypertension;
	@JsonProperty("hp_asthma")
	private Boolean asthma;
	@JsonProperty("hp_osteoporosis")
	private Boolean osteoporosis;
	@JsonProperty("hp_cancer")
	private Boolean cancer;
	@JsonProperty("hp_dementia_disorder")
	private Boolean dementiaDisorder;
	@JsonProperty("hp_cardiovascular_disorder")
	private Boolean cardiovascularDisorder;
	@JsonProperty("hp_pain_other")
	private Boolean painOther;
	@JsonProperty("hp_chronic_obstructive_lung_disease")
	private Boolean chronicObstructiveLungDisease;
}
