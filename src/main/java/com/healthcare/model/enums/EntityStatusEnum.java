package com.healthcare.model.enums;

public enum EntityStatusEnum {
    DISABLE(0), ENABLE(1);
    private final int value;

    private EntityStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
