package com.healthcare.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusEnum {
	REGISTERED("REGISTERED"), ACTIVE("ACTIVE"), INACTIVE("INACTIVE"), PENDING("PENDING");
	private String value;

	StatusEnum(String value){
		this.value = value;
	}

	@JsonValue
    public String getValue(){
		return value;
	}
}
