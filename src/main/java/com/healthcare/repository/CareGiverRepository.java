package com.healthcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.healthcare.model.entity.CareGiver;

@Repository
public interface CareGiverRepository extends JpaRepository<CareGiver, Long>, JpaSpecificationExecutor<CareGiver> {
	
}