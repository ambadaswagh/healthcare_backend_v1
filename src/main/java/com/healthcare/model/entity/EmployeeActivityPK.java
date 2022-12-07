package com.healthcare.model.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by pazfernando on 4/30/17.
 */
public @Data class EmployeeActivityPK implements Serializable {
	
	private static final long serialVersionUID = 8388193881984907193L;
	private Employee employee;
	private Activity activity;

}
