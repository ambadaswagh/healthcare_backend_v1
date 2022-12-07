package com.healthcare.service;

import java.util.List;

import com.healthcare.model.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface OrganizationService extends IService<Organization>, IFinder<Organization> {
	List<Organization> findAll();

	Page<Organization> findAllByAgency(Long agencyId, Pageable pageable);

	List<Organization> findAllByAgencyList(Long agencyId);

	Page<Organization> findAllByAgencies(List<Long> agencies, Pageable pageable);

	List<Organization> findAllByAgenciesList(List<Long> agencies);
}
