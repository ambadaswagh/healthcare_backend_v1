package com.healthcare.model.entity.usertype.review;

import com.healthcare.model.entity.review.PsychologicalSocialCondition;
import com.healthcare.model.entity.usertype.StringSerializableUserType;
import com.healthcare.util.UserTypeJsonConverter;

/**
 * Implementation of Hibernate User Type for
 * {@link com.healthcare.model.entity.review.PsychologicalSocialCondition}
 */
public class PsychologicalSocialConditionUserType extends StringSerializableUserType<PsychologicalSocialCondition> {

	@Override
	public Class returnedClass() {
		return PsychologicalSocialCondition.class;
	}

	@Override
	protected PsychologicalSocialCondition cast(Object obj) {
		return (PsychologicalSocialCondition) obj;
	}

	@Override
	protected PsychologicalSocialCondition deserializeConcrete(String string) {
		return (PsychologicalSocialCondition) UserTypeJsonConverter.fromJsonString(string,
				PsychologicalSocialCondition.class);
	}

	@Override
	protected String serializeConcrete(PsychologicalSocialCondition psychologicalSocialCondition) {
		return UserTypeJsonConverter.toJsonString(psychologicalSocialCondition);
	}
}
