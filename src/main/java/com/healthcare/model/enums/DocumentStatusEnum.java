package com.healthcare.model.enums;

public enum DocumentStatusEnum {
	ACTIVE("1");
	
	private String value;

	DocumentStatusEnum(final String newValue) {
		value = newValue;
	}

	public String getValue() {
		return value;
	}
}
