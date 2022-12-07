package com.healthcare.api.model;

import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @author Moustafa Hamed
 *
 */
@Data
public class MobileCheckinRequestDTO implements Serializable{
	
	private static final long serialVersionUID = 3896584981399983498L;
	
	private Integer agencyId;
	private Integer userId;
	private Integer breakfastMealId;
	private Integer lunchMealId;
	private Integer dinnerMealId;
	private Integer activityId;
	private Integer tableId;
	private Integer seatId;
	private String signature;
	private int active;
	private String pin;

}
