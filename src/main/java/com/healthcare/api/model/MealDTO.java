package com.healthcare.api.model;

import com.healthcare.model.entity.Document;
import com.healthcare.model.entity.Ingredient;
import com.healthcare.model.entity.Meal;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class MealDTO implements Serializable{
	
	private static final long serialVersionUID = 8109925376598549926L;
	private Long id;
	private String mealClass;
	private String name;
	private Double price;
	private String ingredients;
	private String notes;
	private String cuisine;
	private Integer status = 0;
	private List<Ingredient> ingredientList;
	private Document mealPhoto;
	private Integer mealStatus;
	private int selected;
	private int allergyConflicts = 0;
}
