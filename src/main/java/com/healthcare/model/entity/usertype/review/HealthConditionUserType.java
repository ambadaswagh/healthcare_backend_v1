package com.healthcare.model.entity.usertype.review;

import com.healthcare.model.entity.review.HealthCondition;
import com.healthcare.model.entity.usertype.StringSerializableUserType;
import com.healthcare.util.UserTypeJsonConverter;

/**
 * Implementation of Hibernate User Type for
 * {@link com.healthcare.model.entity.review.HealthCondition}
 */
public class HealthConditionUserType extends StringSerializableUserType<HealthCondition> {

	@Override
	public Class returnedClass() {
		return HealthCondition.class;
	}

	@Override
	protected HealthCondition cast(Object obj) {
		return (HealthCondition) obj;
	}

	@Override
	protected HealthCondition deserializeConcrete(String string) {
		return (HealthCondition) UserTypeJsonConverter.fromJsonString(string, HealthCondition.class);
	}

	@Override
	protected String serializeConcrete(HealthCondition healthCondition) {
		return UserTypeJsonConverter.toJsonString(healthCondition);
	}
}
