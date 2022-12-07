package com.healthcare.model.entity.review;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Model class that contains part data of review
 */
@EqualsAndHashCode
public @Data class NutritionCondition implements Serializable {

	private static final long serialVersionUID = 8230344492963313037L;

	@JsonProperty("nutrition_progress_weight")
	private String progressWeight;
	@JsonProperty("nutrition_appetite")
	private String appetite;
	@JsonProperty("nutrition_changed")
	private Boolean appetiteChanged;
	@JsonProperty("nutrition_vomit")
	private Boolean appetiteVomit;
	@JsonProperty("nutrition_liquid_daily_min")
	private String liquidDailyMin;
	@JsonProperty("nutrition_liquid_daily_max")
	private String liquidDailyMax;
	@JsonProperty("nutrition_fruit_snack_times_min")
	private String fruitSnackTimesMin;
	@JsonProperty("nutrition_fruit_snack_times_max")
	private String fruitSnackTimesMax;
}
