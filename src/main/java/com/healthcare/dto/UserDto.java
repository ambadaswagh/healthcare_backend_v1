package com.healthcare.dto;

import lombok.Data;

@Data
public class UserDto {
	
	private long totalRegisteredSeniors;
	private long totalCheckedInSeniors;
	private long totalActiveSeniorsAmongCheckedIn;

}
