package com.healthcare.model.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by pazfernando on 4/30/17.
 */
public @Data class TrainingEmployeePK implements Serializable {

	private static final long serialVersionUID = 5024837118478915068L;
	private Training training;
	private Employee employee;

}
