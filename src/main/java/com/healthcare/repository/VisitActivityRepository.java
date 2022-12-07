package com.healthcare.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.healthcare.model.entity.VisitActivity;
import com.healthcare.model.entity.VisitActivityPK;

@Repository
public interface VisitActivityRepository extends JpaRepository<VisitActivity, VisitActivityPK>, JpaSpecificationExecutor<VisitActivity> {
	List<VisitActivity> findVisitActivityByActivityId(Long id);

	List<VisitActivity> findVisitActivityByVisitId(Long id);

	Page<VisitActivity> findVisitActivityByActivityId(Long id, Pageable pageable);

	Page<VisitActivity> findVisitActivityByVisitId(Long id, Pageable pageable);
} 
