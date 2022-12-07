package com.healthcare.model.entity.usertype.review;

import com.healthcare.model.entity.review.NutritionCondition;
import com.healthcare.model.entity.usertype.StringSerializableUserType;
import com.healthcare.util.UserTypeJsonConverter;

/**
 * Implementation of Hibernate User Type for
 * {@link com.healthcare.model.entity.review.NutritionCondition}
 */
public class NutritionConditionUserType extends StringSerializableUserType<NutritionCondition> {

	@Override
	public Class returnedClass() {
		return NutritionCondition.class;
	}

	@Override
	protected NutritionCondition cast(Object obj) {
		return (NutritionCondition) obj;
	}

	@Override
	protected NutritionCondition deserializeConcrete(String string) {
		return (NutritionCondition) UserTypeJsonConverter.fromJsonString(string, NutritionCondition.class);
	}

	@Override
	protected String serializeConcrete(NutritionCondition nutritionCondition) {
		return UserTypeJsonConverter.toJsonString(nutritionCondition);
	}
}
