package com.healthcare.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.healthcare.model.entity.Employee;
import com.healthcare.service.EmployeeService;

@Component
public class UpdateEmployeeValidator implements Validator{
	
	@Autowired private EmployeeService employeeService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Employee.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors e) {
		Employee employee = (Employee) obj;
		
		boolean isValid = employeeService.validPin(employee.getPin());
		Employee tmpEmployee  = employeeService.findById(employee.getId());
		if(tmpEmployee.getPin() != null && !tmpEmployee.getPin().equals(employee.getPin())){
			if(!isValid){
				e.reject("pin", "pin already exists");
			}			
		}
	}

}
