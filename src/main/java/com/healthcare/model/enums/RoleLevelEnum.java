package com.healthcare.model.enums;

public enum RoleLevelEnum {
    SUPER_ADMIN(1),
    SUB_SUPER_ADMIN(2),
    COMPANY_ADMIN(3),
    SUB_COMPANY_ADMIN(4),
    SENIOR_CENTER_ADMIN(5),
    SUB_SENIOR_CENTER_ADMIN(6);

    private int value;

    private RoleLevelEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RoleLevelEnum getRoleLevelEnum(int value) {
        for(RoleLevelEnum roleLevelEnum : RoleLevelEnum.values()) {
            if(roleLevelEnum.getValue() == value) {
                return roleLevelEnum;
            }
        }
        return null;
    }
}
