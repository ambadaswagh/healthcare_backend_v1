package com.healthcare.model.entity.usertype.review;

import com.healthcare.model.entity.review.CommunicationHearingCondition;
import com.healthcare.model.entity.usertype.StringSerializableUserType;
import com.healthcare.util.UserTypeJsonConverter;

/**
 * Implementation of Hibernate User Type for {@link com.healthcare.model.entity.review.CommunicationHearingCondition}
 */
public class CommunicationHearingConditionUserType extends StringSerializableUserType<CommunicationHearingCondition> {

    @Override
    public Class returnedClass() {
        return CommunicationHearingCondition.class;
    }

    @Override
    protected CommunicationHearingCondition cast(Object obj) {
        return (CommunicationHearingCondition) obj;
    }

    @Override
    protected CommunicationHearingCondition deserializeConcrete(String string) {
        return (CommunicationHearingCondition) UserTypeJsonConverter.fromJsonString(string, CommunicationHearingCondition.class);
    }

    @Override
    protected String serializeConcrete(CommunicationHearingCondition communicationHearingCondition) {
        return UserTypeJsonConverter.toJsonString(communicationHearingCondition);
    }
}
