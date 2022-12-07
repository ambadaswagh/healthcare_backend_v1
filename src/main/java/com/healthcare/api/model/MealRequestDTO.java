package com.healthcare.api.model;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class MealRequestDTO implements Serializable{
	
	private static final long serialVersionUID = 8109925376598549926L;

	private Long agencyId;
	private Integer userId;
	private Timestamp date;

}
