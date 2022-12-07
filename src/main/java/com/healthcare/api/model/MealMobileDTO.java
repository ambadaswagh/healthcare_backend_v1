package com.healthcare.api.model;

import com.healthcare.model.entity.Meal;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class MealMobileDTO implements Serializable{
	
	private static final long serialVersionUID = 8109925376598549926L;

	private List<MealDTO> breakfast = new ArrayList<>();
	private List<MealDTO> lunch = new ArrayList<>();
	private List<MealDTO> dinner = new ArrayList<>();

}
