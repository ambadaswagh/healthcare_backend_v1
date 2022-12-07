package com.healthcare.model.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

/**
 * Created by pazfernando on 4/30/17.
 */
@Embeddable
public @Data class VisitActivityPK implements Serializable {

	private static final long serialVersionUID = -4912913092398276257L;
	private Long visitId;
	private Long activityId;

	public VisitActivityPK() {
	}

	public VisitActivityPK(Long visitId, Long activityId) {
		this.visitId = visitId;
		this.activityId = activityId;
	}
}
