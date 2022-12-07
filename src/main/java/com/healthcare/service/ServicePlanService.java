package com.healthcare.service;

import java.util.Date;
import java.util.List;

import com.healthcare.dto.BasicReportFilterDTO;
import com.healthcare.model.entity.ServicePlan;

public interface ServicePlanService extends IService<ServicePlan>, IFinder<ServicePlan> {
	ServicePlan save(ServicePlan servicePlan);
	List<ServicePlan> findAll();
	List<Date> serviceCalendarGeneration(Long serviceplanId);
	
	List<String> getServiceCalendar(Long servicePlanId);

	List<ServicePlan> getServicePlanByUser(BasicReportFilterDTO brf);
	
	List<ServicePlan> getServicePlanByUserId(Long userId);
}
