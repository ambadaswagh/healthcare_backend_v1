package com.healthcare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthcare.model.entity.Organization;

import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long>, JpaSpecificationExecutor<Organization> {
	@Query("select a from Organization a")
	Page<Organization> findAllOrganizationPageable(Pageable pageable);

	@Query("select a from Organization a where a.agency.id = ?1")
	Page<Organization> findAllByAgency(Long agencyId, Pageable pageable);

	@Query("select a from Organization a where a.agency.id = ?1")
	List<Organization> findAllByAgencyList(Long agencyId);

	@Query("select a from Organization a where a.agency.id in ?1")
	Page<Organization> findAllByAgencies(List<Long> agencies, Pageable pageable);

	@Query("select a from Organization a where a.agency.id in ?1")
	List<Organization> findAllByAgenciesList(List<Long> agencies);
}