package com.healthcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.healthcare.model.entity.HomeVisit;
import com.healthcare.model.entity.ServicePlan;

@Repository
public interface HomeVisitRepository extends JpaRepository<HomeVisit, Long>, JpaSpecificationExecutor<HomeVisit> {
	
	List<HomeVisit> findAllByServiceplan(ServicePlan servicePlan);
	
}