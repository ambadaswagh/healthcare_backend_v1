package com.healthcare.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.healthcare.model.entity.Leave;
import lombok.Data;
import springfox.documentation.spring.web.json.Json;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class EmployeeLeaveBatchDTO {

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String status;

	private CopyOnWriteArrayList<Leave> employeeLeaveList;

    public EmployeeLeaveBatchDTO(){}

    public EmployeeLeaveBatchDTO(String status, List <Leave> employeeLeaves){
        this.status = status;
        setEmployeeLeaveList(employeeLeaves);
    }

    public void setEmployeeLeaveList(List <Leave> employeeLeaves){
        this.employeeLeaveList = new CopyOnWriteArrayList <Leave>(employeeLeaves);
    }
}
