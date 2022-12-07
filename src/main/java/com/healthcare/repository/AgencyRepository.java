package com.healthcare.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthcare.dto.AgencyStatsDTO;
import com.healthcare.dto.UserDto;
import com.healthcare.model.entity.Agency;

@Repository
public interface AgencyRepository extends JpaRepository<Agency, Long>, JpaSpecificationExecutor<Agency> {

	@Deprecated
	@Query(value = "SELECT" + " (SELECT COUNT(*) FROM user u " + " WHERE u.agency_id = ?1) AS totalRegisteredSeniors,"
			+ " (SELECT COUNT(*) FROM visit v "
			+ " WHERE  now() >= v.check_in_time AND now() <= v.check_out_time and v.agency_id=?1) AS totalCheckedInSeniors, "
			+ " (SELECT count(*) FROM visit v  INNER JOIN user u ON v.user_id = u.id "
			+ " WHERE  now() >= v.check_in_time " + " AND now() <= v.check_out_time AND v.agency_id=?1 "
			+ " AND u.status = 1) AS totalActiveSeniorsAmongCheckedIn;", nativeQuery = true)
	UserDto generateUserStats(Long agencyId);

	/**
	 * agency stats
	 * 
	 * @param agencyId
	 * @return
	 */
	@Query(value = "SELECT"
			// total active seniors
			+ " (SELECT COUNT(*) FROM user u "
			+ " WHERE u.agency_id = ?1 and u.status= 'ACTIVE') AS totalActiveSeniors,"

			// total checkedin seniors
			+ " (SELECT COUNT(*) FROM visit v "
			+ " WHERE  now() >= v.check_in_time AND now() <= v.check_out_time and v.agency_id=?1) AS totalCheckedInSeniors, "

			// seniors active and registered
			+ " (SELECT COUNT(*) FROM user u "
			+ " WHERE u.agency_id = ?1 and (u.status= 'ACTIVE' or u.status= 'REGISTERED')) AS totalActiveAndRegistered;", nativeQuery = true)
	List<Object>generateAgencyStat(Long agencyId);

	@Query(value = "select a from Agency a where a.company.id = ?1")
	public List<Agency> findByCompanyId(Long companyId);

	@Query(value = "SELECT * " + " FROM agency " + " WHERE name = ?1", nativeQuery = true)
	List<Agency> findAgencyName(String name);

	@Query(value = "select a from Agency a where a.company.id = ?1")
	public Page<Agency> findByCompanyPage(Long companyId, Pageable pageable);

	@Modifying
	@Query(value = "UPDATE Agency set mealsSelected= ?2 where id= ?1")
	void updateMealSelected(Long agencyId, String mealsSelected);

	@Modifying
	@Query(value = "UPDATE Agency set logo.id=?2, mainWhiteLabel =?3, upperLeftLabel = ?4 where id=?1")
	void updateLogoSetting(Long agencyId, Long logoId, String mainWhiteLabel, String upperLeftLabel);

}