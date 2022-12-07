package com.healthcare.model.entity.usertype.review;

import com.healthcare.model.entity.review.ActivityDetails;
import com.healthcare.model.entity.usertype.StringSerializableUserType;
import com.healthcare.util.UserTypeJsonConverter;

/**
 * Implementation of Hibernate User Type for
 * {@link com.healthcare.model.entity.review.ActivityDetails}
 */
public class ActivityDetailsUserType extends StringSerializableUserType<ActivityDetails> {

	@Override
	public Class returnedClass() {
		return ActivityDetails.class;
	}

	@Override
	protected ActivityDetails cast(Object obj) {
		return (ActivityDetails) obj;
	}

	@Override
	protected ActivityDetails deserializeConcrete(String string) {
		return (ActivityDetails) UserTypeJsonConverter.fromJsonString(string, ActivityDetails.class);
	}

	@Override
	protected String serializeConcrete(ActivityDetails activityDetails) {
		return UserTypeJsonConverter.toJsonString(activityDetails);
	}
}
