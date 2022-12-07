package com.healthcare.service;

import java.util.List;

import com.healthcare.model.entity.Billing;

public interface BillingService extends IService<Billing>, IFinder<Billing> {
	Billing findByVisitId(Long visitId);
	Billing findByTripId(Long tripId);
	Billing save(Billing entity, Long adminId);
}
