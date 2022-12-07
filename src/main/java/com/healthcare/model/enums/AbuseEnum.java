package com.healthcare.model.enums;

public enum AbuseEnum {
    PHYSICAL_ABUSE("pa"),
    ACTIVE_AND_PASSIVE_NEGLECT("aapn"),
    SEXUAL_ABUSE("sa"),
    FINANCIAL_EXPLOITATION("fe"),
    EMOTIONAL_ABUSE("ea"),
    DOMESTIC_VIOLENCE("dv"),
    SELF_NEGLECT("sn");

    private String id;

    AbuseEnum(String id) {
        this.id = id;
    }
}
