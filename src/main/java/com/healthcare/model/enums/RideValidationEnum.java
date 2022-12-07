package com.healthcare.model.enums;

public enum RideValidationEnum {
    TYPE1("Valid rides"),
    TYPE2("Invalid visits - Exceed the maximum units"),
    TYPE3("Unauthorized visit"),
    TYPE4("TYPE is not defined");

    private String value;

    RideValidationEnum(final String newValue) {
        value = newValue;
    }

    public String getValue() {
        return value;
    }


}
