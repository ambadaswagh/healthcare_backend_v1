package com.healthcare.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

import javax.persistence.Table;

/**
 * Alter by Jean Antunes on 7/6/17.
 */
@Entity
@Table(name = "training_has_employee", schema = "health_care_v1_dev", catalog = "")
public @Data class TrainingEmployee implements Serializable {

	private static final long serialVersionUID = 5555550953790346395L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "training_id")
	private Training training;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	private String notes;

}
