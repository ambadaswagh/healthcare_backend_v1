package com.healthcare.api.model;

import java.io.Serializable;

import com.healthcare.model.entity.Document;

import lombok.Data;

public @Data class TimeClockRequest implements Serializable {

	private static final long serialVersionUID = -3744530234267489863L;
	
	private Document docSignature;
	private Long employeeId;
	private String signature;
}
