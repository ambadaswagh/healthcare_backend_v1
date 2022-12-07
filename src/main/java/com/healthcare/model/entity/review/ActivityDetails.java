package com.healthcare.model.entity.review;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Model class that contains part data of review
 */
@EqualsAndHashCode
public @Data class ActivityDetails implements Serializable {

	private static final long serialVersionUID = -666418160280233807L;

	@JsonProperty("act_ambulation")
	private String ambulation;
	@JsonProperty("act_fall")
	private String fall;
	@JsonProperty("act_cane")
	private String cane;
	@JsonProperty("act_date_fall")
	private Date dateDall;
}
