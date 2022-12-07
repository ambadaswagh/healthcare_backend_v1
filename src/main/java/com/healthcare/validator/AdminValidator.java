package com.healthcare.validator;

import java.util.Arrays;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.healthcare.model.entity.Admin;

public class AdminValidator implements Validator {
	private static final List<String> GENDER = Arrays.asList(new String[] { "male", "female", "other" });

	@Override
	public boolean supports(Class<?> clazz) {
		return Admin.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors e) {
		Admin admin = (Admin) target;
		
		if (admin.getUsername() == null || admin.getUsername().isEmpty()) {
			e.reject("username", "Username is blank");
		}
		ValidationUtils.rejectIfEmpty(e, "gender", "Gender empty");
		if ((admin.getUsername() == null || admin.getUsername().isEmpty()) && GENDER.indexOf(admin.getUsername()) < 0) {
			e.reject("gender", "Gender need to be male, female, other");
		}
		ValidationUtils.rejectIfEmpty(e, "email", "Email empty");
		ValidationUtils.rejectIfEmpty(e, "phone", "Phone empty");
		ValidationUtils.rejectIfEmpty(e, "firstName", "firstname empty");
		ValidationUtils.rejectIfEmpty(e, "lastName", "lastname empty");
		ValidationUtils.rejectIfEmpty(e, "menulist", "menulist empty");
	}

}
