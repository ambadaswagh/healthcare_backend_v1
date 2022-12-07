package com.healthcare.service;

import java.util.List;

import com.healthcare.model.entity.ResourceBusinessHours;

public interface ResourceBusinessHoursService extends IService<ResourceBusinessHours>, IFinder<ResourceBusinessHours> {
	List<ResourceBusinessHours> findByResourceId(Long resourceId);
	void deleteByResourceId(Long resourceId);
}
