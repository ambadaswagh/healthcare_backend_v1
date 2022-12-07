package com.healthcare.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthcare.model.entity.ResourceBusinessHours;
import com.healthcare.model.entity.Seat;

@Repository
public interface ResourceBusinessHoursRepository extends JpaRepository<ResourceBusinessHours, Long>, JpaSpecificationExecutor<ResourceBusinessHours> {

	@Query(value = "select * from business_hours where resource_id = ?1", nativeQuery = true)
    List<ResourceBusinessHours> findByResourceId(Long resourceId);
	
	@Modifying
	@Query(value = "DELETE FROM business_hours WHERE resource_id = ?1", nativeQuery = true)
	void deleteByResourceId(Long resourceId);

}
