package com.healthcare.model.enums;

public enum AbuseReferrer {

    ADULT_PROTECTIVE_SERVICES("aps"),
    AAA("aaa"),
    POLICE_AGENCY("police_agency");

    private String id;

    AbuseReferrer(String id) {
        this.id = id;
    }
}
