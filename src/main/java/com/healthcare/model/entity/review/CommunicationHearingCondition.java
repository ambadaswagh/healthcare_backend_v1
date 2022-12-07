package com.healthcare.model.entity.review;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Model class that contains part data of review
 */
@EqualsAndHashCode
public @Data class CommunicationHearingCondition implements Serializable {

	private static final long serialVersionUID = -8601355973953827705L;

	@JsonProperty("comhea_hearing_appliance")
	private String hearingAppliance;
	@JsonProperty("comhea_quality")
	private String quality;
	@JsonProperty("comhea_making_self_understood")
	private String makingSelfUnderstood;
	@JsonProperty("comhea_ability_understand_others")
	private String abilityUnderstandOthers;
}
