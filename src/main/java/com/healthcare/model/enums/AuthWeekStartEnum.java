package com.healthcare.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by dungtp on 1/8/18.
 */
public enum AuthWeekStartEnum {
    MONDAY_WEEK(0), TUESDAY_WEEK(1), WEBNESDAY_WEEK(2), THURSDAY_WEEK(3), FRIDAY_WEEK(4), SATURDAY_WEEK(5), SUNDAY_WEEK(6);

    private int value;

    AuthWeekStartEnum(int value){
        this.value = value;
    }

    @JsonValue
    public int getValue(){
        return value;
    }
}
