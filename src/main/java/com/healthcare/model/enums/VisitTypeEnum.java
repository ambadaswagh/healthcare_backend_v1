package com.healthcare.model.enums;

public enum VisitTypeEnum {
    TYPE1("Valid visits"),
    TYPE2("Invalid visits (With out check - out time)"),
    TYPE3("Invalid visits -Visit shorter than 4 hours per day(Daily Pay)"),
    TYPE4("Invalid visits - Exceed the maximum units"),
    TYPE5("Unauthorized visit"),
    TYPE6("TYPE is not defined");

    private String value;

    VisitTypeEnum(final String newValue) {
        value = newValue;
    }

    public String getValue() {
        return value;
    }


}
