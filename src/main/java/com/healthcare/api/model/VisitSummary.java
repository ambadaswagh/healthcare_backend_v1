package com.healthcare.api.model;

import java.io.Serializable;
import java.math.BigInteger;

import lombok.Data;

public @Data class VisitSummary implements Serializable{

	private Integer id;
	private String restaurantName;
	private String mealName;
	private String mealClass;
	private BigInteger quantity;
	private String status;
  private Integer mealStatus;
}
