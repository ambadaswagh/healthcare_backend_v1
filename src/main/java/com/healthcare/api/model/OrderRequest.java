package com.healthcare.api.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.healthcare.util.CustomJsonDateSerializer;

import lombok.Data;

@Data
public class OrderRequest implements Serializable {

	private Long id;
	private Long mealId;
	private Long userId;
	private Long organizationId;
	
	@JsonSerialize(using=CustomJsonDateSerializer.class)
	private Date orderTime;
	
	@JsonSerialize(using=CustomJsonDateSerializer.class)
	private Date deliveryTime;
	private Integer status;
	private Date createdAt;
	private Date updatedAt;
}
