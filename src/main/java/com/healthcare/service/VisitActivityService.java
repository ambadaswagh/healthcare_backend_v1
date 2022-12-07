package com.healthcare.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.healthcare.model.entity.VisitActivity;
import com.healthcare.model.entity.VisitActivityPK;

public interface VisitActivityService extends IService<VisitActivity> {

	VisitActivity findById(VisitActivityPK pk);

	Long deleteById(VisitActivityPK pk);

	List<VisitActivity> findVisitActivityByActivityId(Long id);

	List<VisitActivity> findVisitActivityByVisitId(Long id);

	Page<VisitActivity> findVisitActivityByVisitId(Long visitId, Pageable pageable);

	Page<VisitActivity> findVisitActivityByActivityId(Long activityId, Pageable pageable);

}