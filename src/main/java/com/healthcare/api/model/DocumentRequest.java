package com.healthcare.api.model;

import lombok.Data;

public @Data class DocumentRequest {

	private Long id;
	private String entity;
	private Long entityId;
	private String fileClass;
}
