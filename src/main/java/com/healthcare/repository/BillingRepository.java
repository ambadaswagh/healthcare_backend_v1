package com.healthcare.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.healthcare.model.entity.Billing;

public interface BillingRepository extends JpaRepository<Billing, Long>, JpaSpecificationExecutor<Billing>  {

	@Query(value = "select v from Billing v where v.visitId = ?1")
    Billing findByVisit(Long visitId);
	@Query(value = "select v from Billing v where v.tripId = ?1")
    Billing findByTrip(Long tripId);
}
