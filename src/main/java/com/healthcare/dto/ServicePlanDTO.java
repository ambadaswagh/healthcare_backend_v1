package com.healthcare.dto;

import com.healthcare.model.entity.Employee;
import com.healthcare.model.entity.ServicePlan;
import com.healthcare.model.entity.User;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Hitesh on 10/13/2017.
 */
@Data
public class ServicePlanDTO {

	private Long id;
	//private String approvedBy;
	private Timestamp planStart;
	private Timestamp planEnd;
	private String days;
	private String docUrl;

	public ServicePlanDTO(ServicePlan s) {
		this.id = s.getId();
	  //this.approvedBy = s.getApprovedBy();
		this.planStart = s.getPlanStart();
		this.planEnd = s.getPlanEnd();
		this.days = s.getDays();
	}
}
