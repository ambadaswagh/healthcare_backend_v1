package com.healthcare.repository;

import java.util.List;

import com.healthcare.model.entity.ServicePlan;

public interface ServicePlanRepositoryCustom {
	List<String> getServiceCalendar(ServicePlan servicePlan);
}
