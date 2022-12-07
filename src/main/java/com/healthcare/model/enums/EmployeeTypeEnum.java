package com.healthcare.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EmployeeTypeEnum {

	HOURLY("HOURLY"), SALARY("SALARY"),ANNUALLY("ANNUALLY");
	private String value;

	EmployeeTypeEnum(String value){
		this.value = value;
	}

	@JsonValue
    public String getValue(){
		return value;
	}

}
