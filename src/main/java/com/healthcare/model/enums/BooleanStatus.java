package com.healthcare.model.enums;

/**
 * Created by Mostapha on 08/01/2018.
 */
public enum BooleanStatus {
    TRUE(1),
    FALSE(0);

    private int value;

    BooleanStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
