package com.healthcare.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by dungtp on 1/8/18.
 */
public enum PeriodTypeEnum {
    WEEKLY(0), ENTIRE_AUTH(1);

    private int value;

    PeriodTypeEnum(int value){
        this.value = value;
    }

    @JsonValue
    public int getValue(){
        return value;
    }
}
