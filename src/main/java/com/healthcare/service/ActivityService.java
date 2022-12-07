package com.healthcare.service;

import java.util.List;

import com.healthcare.dto.BasicReportFilterDTO;
import com.healthcare.model.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Activity service methods
 */
public interface ActivityService extends IService<Activity>, IFinder<Activity> {
	List<Activity> findAll();
	public List<Activity> getActivityByUser(BasicReportFilterDTO brf);

	Page<Activity> findAllByAgency(Long agencyId, Pageable pageable);

	List<Activity> findAllByAgencyList(Long agencyId);

	Page<Activity> findAllByAgencies(List<Long> agencies, Pageable pageable);

	List<Activity> findAllByAgenciesList(List<Long> agencies);
}
