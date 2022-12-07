package com.healthcare.model.entity.usertype.review;

import com.healthcare.model.entity.review.PainDetails;
import com.healthcare.model.entity.usertype.StringSerializableUserType;
import com.healthcare.util.UserTypeJsonConverter;

/**
 * Implementation of Hibernate User Type for
 * {@link com.healthcare.model.entity.review.PainDetails}
 */
public class PainDetailsUserType extends StringSerializableUserType<PainDetails> {

	@Override
	public Class returnedClass() {
		return PainDetails.class;
	}

	@Override
	protected PainDetails cast(Object obj) {
		return (PainDetails) obj;
	}

	@Override
	protected PainDetails deserializeConcrete(String string) {
		return (PainDetails) UserTypeJsonConverter.fromJsonString(string, PainDetails.class);
	}

	@Override
	protected String serializeConcrete(PainDetails painDetails) {
		return UserTypeJsonConverter.toJsonString(painDetails);
	}
}
