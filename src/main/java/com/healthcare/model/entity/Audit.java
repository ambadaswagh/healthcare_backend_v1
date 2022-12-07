package com.healthcare.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

@MappedSuperclass
public @Data class Audit {
	@Column(name = "created_at")
	protected Timestamp createdAt;
	@Column(name = "updated_at")
	protected Timestamp updatedAt;

}
