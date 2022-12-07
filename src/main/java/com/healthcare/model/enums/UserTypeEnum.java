package com.healthcare.model.enums;

public enum UserTypeEnum {
	SENIORS(0),PATIENT(1),PRE_REGISTER_SENIOR(2),PRE_REGISTER_PATIENT(3);

    private int value;

    UserTypeEnum(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}
