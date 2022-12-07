package com.healthcare.model.entity.usertype.review;

import com.healthcare.model.entity.review.FunctionalStatus;
import com.healthcare.model.entity.usertype.StringSerializableUserType;
import com.healthcare.util.UserTypeJsonConverter;

/**
 * Implementation of Hibernate User Type for
 * {@link com.healthcare.model.entity.review.FunctionalStatus}
 */
public class FunctionalStatusUserType extends StringSerializableUserType<FunctionalStatus> {

	@Override
	public Class returnedClass() {
		return FunctionalStatus.class;
	}

	@Override
	protected FunctionalStatus cast(Object obj) {
		return (FunctionalStatus) obj;
	}

	@Override
	protected FunctionalStatus deserializeConcrete(String string) {
		return (FunctionalStatus) UserTypeJsonConverter.fromJsonString(string, FunctionalStatus.class);
	}

	@Override
	protected String serializeConcrete(FunctionalStatus functionalStatus) {
		return UserTypeJsonConverter.toJsonString(functionalStatus);
	}
}
