package com.healthcare.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by dungtp on 1/8/18.
 */
public enum UnitTypeEnum {
    HOUR(0), DAY(1);

    private int value;

    UnitTypeEnum(int value){
        this.value = value;
    }

    @JsonValue
    public int getValue(){
        return value;
    }
}
