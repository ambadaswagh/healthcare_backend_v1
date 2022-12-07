package com.healthcare.model.entity.usertype.review;

import com.healthcare.model.entity.review.CardPulmCondition;
import com.healthcare.model.entity.usertype.StringSerializableUserType;
import com.healthcare.util.UserTypeJsonConverter;

/**
 * Implementation of Hibernate User Type for {@link com.healthcare.model.entity.review.CardPulmCondition}
 */
public class CardPulmConditionUserType extends StringSerializableUserType<CardPulmCondition> {

    @Override
    public Class returnedClass() {
        return CardPulmCondition.class;
    }

    @Override
    protected CardPulmCondition cast(Object obj) {
        return (CardPulmCondition) obj;
    }

    @Override
    protected CardPulmCondition deserializeConcrete(String string) {
        return (CardPulmCondition) UserTypeJsonConverter.fromJsonString(string, CardPulmCondition.class);
    }

    @Override
    protected String serializeConcrete(CardPulmCondition cardPulmCondition) {
        return UserTypeJsonConverter.toJsonString(cardPulmCondition);
    }
}
