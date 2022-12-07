package com.healthcare.service;

import java.util.List;

import com.healthcare.dto.BasicReportFilterDTO;
import com.healthcare.model.entity.HealthInsuranceClaim;

/**
 * Created by Hitesh on 07/14/17.
 */
public interface HealthInsuranceClaimService extends IService<HealthInsuranceClaim>, IFinder<HealthInsuranceClaim> {
	List<HealthInsuranceClaim> findAll();
	List<HealthInsuranceClaim> findByUserId(Long id);
	
	List<HealthInsuranceClaim> findHealthInsuranceReport( BasicReportFilterDTO basicFilter);
	
	List<HealthInsuranceClaim> findTripStatusForBilling(List<HealthInsuranceClaim> healthInsuranceClaimLst);
}
