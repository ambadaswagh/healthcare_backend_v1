package com.healthcare.validator;

import java.util.Arrays;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.AdminAgencyCompanyOrganization;

public class AdminAgencyCompanyOrganizationValidator implements Validator {
	private static final List<String> GENDER = Arrays.asList(new String[] { "male", "female", "other" });

	@Override
	public boolean supports(Class<?> clazz) {
		return AdminAgencyCompanyOrganization.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors e) {
		AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = 
				(AdminAgencyCompanyOrganization) target;
		
		if (adminAgencyCompanyOrganization.getAdmin().getUsername() == null || 
				adminAgencyCompanyOrganization.getAdmin().getUsername().isEmpty()) {
			e.reject("username", "Username is blank");
		}
		ValidationUtils.rejectIfEmpty(e, "admin.gender", "Gender empty");
		if ((adminAgencyCompanyOrganization.getAdmin().getUsername() == null || 
				adminAgencyCompanyOrganization.getAdmin().getUsername().isEmpty()) && 
				GENDER.indexOf(adminAgencyCompanyOrganization.getAdmin().getUsername()) < 0) {
			e.reject("gender", "Gender need to be male, female, other");
		}
		ValidationUtils.rejectIfEmpty(e, "admin.email", "Email empty");
		ValidationUtils.rejectIfEmpty(e, "admin.phone", "Phone empty");
		ValidationUtils.rejectIfEmpty(e, "admin.firstName", "firstname empty");
		ValidationUtils.rejectIfEmpty(e, "admin.lastName", "lastname empty");
		ValidationUtils.rejectIfEmpty(e, "admin.menulist", "menulist empty");
	}

}
