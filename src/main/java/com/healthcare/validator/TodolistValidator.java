package com.healthcare.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.healthcare.dto.TodolistDTO;

public class TodolistValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return TodolistDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors e) {
		TodolistDTO dto = (TodolistDTO) target;
		if(dto.getContent() == null || dto.getContent().isEmpty()){
			e.rejectValue("content", "02", "content is required");
		}
		if(dto.getAdminId() == null || dto.getAdminId() <= 0){
			e.rejectValue("adminId", "01", "adminId larger than 0");
		}
	}

}
