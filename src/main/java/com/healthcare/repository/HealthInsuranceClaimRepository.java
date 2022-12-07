package com.healthcare.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthcare.model.entity.HealthInsuranceClaim;

@Repository
public interface HealthInsuranceClaimRepository extends JpaRepository<HealthInsuranceClaim, Long>, JpaSpecificationExecutor<HealthInsuranceClaim> {
	
	List<HealthInsuranceClaim> findByUserId(Long userId);
	
	@Query("select h from HealthInsuranceClaim h join h.patientDiagnosis hpd "
			+ "where h.user.id =?1 "
			+ "and ( ( hpd.patientDateOfServiceFrom >=?2 and hpd.patientDateOfServiceTo <=?3 ) "
			+ "     or hpd.patientDateOfServiceTo between ?2 and ?3 ) " +
			"   and h.user.company.id =?4 " +
			"   and h.user.agency.id = ?5 ")
	List<HealthInsuranceClaim> findHealthClaimReportForUser(Long userId, Date startDate, Date endDate,Long companyId,Long agencyId);
	
	@Query("select h from HealthInsuranceClaim h join h.patientDiagnosis hpd "
			+ "where ( ( hpd.patientDateOfServiceFrom >=?1 and hpd.patientDateOfServiceTo <=?2 ) "
			+ "or hpd.patientDateOfServiceTo between ?1 and ?2 ) " +
			"   and h.user.company.id =?3 " +
			"   and h.user.agency.id = ?4 ")
	List<HealthInsuranceClaim> findHealthClaimReport(Date startDate, Date endDate,Long companyId,Long agencyId);
	
}