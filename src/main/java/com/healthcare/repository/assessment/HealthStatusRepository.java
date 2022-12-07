package com.healthcare.repository.assessment;

import com.healthcare.model.entity.assessment.HealthStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface HealthStatusRepository extends JpaRepository<HealthStatus, Long>, JpaSpecificationExecutor<HealthStatus> {

	@Query(value = " select h.* from health_status h",nativeQuery=true)
	HealthStatus findAllHealthStatus();

	List<HealthStatus> findByUserId(Long userId);
}
