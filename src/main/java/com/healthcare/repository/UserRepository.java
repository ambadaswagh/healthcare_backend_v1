package com.healthcare.repository;

import java.sql.Timestamp;
import java.util.List;

import com.healthcare.model.entity.RideLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.healthcare.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	User findByUsername(String username);

	@Query(value = "select u.* from user u where u.company_id =  COALESCE(?1,u.company_id) "
			+ "and u.agency_id =  COALESCE(?2,u.agency_id)", nativeQuery = true)
	List<User> findAllSeniorsForCompanyAndAgency(@Param("companyId") Long companyId, @Param("agencyId") Long agencyId);

	@Query(value = "SELECT FLOOR(RAND() * 9999) AS random_num " + " FROM user "
			+ " WHERE \"random_num\" NOT IN (SELECT pin FROM user WHERE agency_id = ?1) LIMIT 1", nativeQuery = true)
	String generatePin(long agencyId);

	@Query(value = "SELECT * " + " FROM user " + " WHERE pin = ?1 and agency_id = ?2 ", nativeQuery = true)
	User findUserByPINForAgency(String pin, Long id);
	
	@Query(value = "SELECT * " + " FROM user " + " WHERE pin = ?1", nativeQuery = true)
	User findUserByPIN(String pin);

	@Modifying
	@Query(value = "UPDATE User set note= ?2 where id= ?1")
	void updateNotes(Long id, String note);

	@Query(value = "SELECT CONCAT(pin, \"-\", first_name,\" \",last_name) AS pinstring "
			+ "FROM user where agency_id = ?1 ", nativeQuery = true)
	List<String> findAllPINForAgency(Long id);

	@Query(value = "select u.* from user u where u.agency_id =  COALESCE(?1,u.agency_id)", nativeQuery = true)
	List<User> findUserByAgency(long agencyId);

	@Query(value = "select u.* from user u  left join activity a on u.preferred_activity_id = a.id"
			+ " left join organization o on u.family_doctor_id = o.id"
			+ " left join organization e on u.expert_doctor_id = e.id" + " where " + " u.first_name like %:search%"
			+ " or u.middle_name like %:search%" + " or u.phone like %:search%" + " or u.email like %:search%"
			+ " or u.last_name like %:search%" + " or u.pin like %:search%"
			+ " or u.emergency_contact_first_name like %:search%" + " or u.emergency_contact_middle_name like %:search%"
			+ " or u.emergency_contact_last_name like %:search%" + " or u.emergency_contact_phone like %:search%"
			+ " or o.name like %:search%" + " or o.phone like %:search%" + " or e.name like %:search%"
			+ " or e.phone like %:search%" + " or a.name like %:search%", nativeQuery = true)
	List<User> searchUser(@Param("search") String search);

	@Query(value = "select u.* from user u  left join activity a on u.preferred_activity_id = a.id"
			+ " left join organization o on u.family_doctor_id = o.id"
			+ " left join organization e on u.expert_doctor_id = e.id"
			+ " where u.company_id = :companyId"
			+ " and (u.first_name like %:search%"
			+ " or u.middle_name like %:search% or u.phone like %:search% or u.email like %:search%"
			+ " or u.last_name like %:search% or u.pin like %:search%"
			+ " or u.emergency_contact_first_name like %:search% or u.emergency_contact_middle_name like %:search%"
			+ " or u.emergency_contact_last_name like %:search% or u.emergency_contact_phone like %:search%"
			+ " or o.name like %:search% or o.phone like %:search% or e.name like %:search%"
			+ " or e.phone like %:search% or a.name like %:search%)", nativeQuery = true)
	List<User> searchUserForCompany(@Param("search") String search, @Param("companyId") long companyId);

	@Query(value = "select u.* from user u  left join activity a on u.preferred_activity_id = a.id"
			+ " left join organization o on u.family_doctor_id = o.id"
			+ " left join organization e on u.expert_doctor_id = e.id"
			+ " where u.agency_id = :agencyId"
			+ " and (u.first_name like %:search%"
			+ " or u.middle_name like %:search% or u.phone like %:search% or u.email like %:search%"
			+ " or u.last_name like %:search% or u.pin like %:search%"
			+ " or u.emergency_contact_first_name like %:search% or u.emergency_contact_middle_name like %:search%"
			+ " or u.emergency_contact_last_name like %:search% or u.emergency_contact_phone like %:search%"
			+ " or o.name like %:search% or o.phone like %:search% or e.name like %:search%"
			+ " or e.phone like %:search% or a.name like %:search%)", nativeQuery = true)
	List<User> searchUserForAgency(@Param("search") String search, @Param("agencyId") long agencyId);


	@Query(value = "select u from User u " +
			" where  " +
			"  (u.agency.id = :agency) " +
			"   and (u.company.id = :company)")
	List<User> searchUserForRide(@Param("agency") Long agencyId,
								 @Param("company") Long companyId);


	@Query(value = "SELECT * " + " FROM user " + " WHERE box_number = ?1 and agency_id = ?2 ", nativeQuery = true)
	List<User> findUserByBoxNumberForAgency(String boxNumber, long agencyId);

	@Query(value = "SELECT u.* FROM user u WHERE MONTH(u.dob) = ?1 AND DAY(u.dob) = ?2", nativeQuery = true)
	List<User> getUserBornInDateAndMonth(Integer month, Integer date);


	@Query(value = "select u from User u where u.authorizationEnd >=?1  and u.authorizationEnd <=?2 and u.status= 'ACTIVE' order by u.authorizationEnd asc")
	Page<User> getAuthorizationEndFromDayToDay(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay, Pageable pageable);

	@Query(value = "select u from User u where u.assessmentDuration >=?1  and u.assessmentDuration <=?2 and u.status= 'REGISTERED' order by u.assessmentDuration asc")
	Page<User> getAssessmentEndFromDayToDay(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay, Pageable pageable);

	@Query(value = "select u from User u where u.company.id = ?3 AND u.authorizationEnd >=?1  and u.authorizationEnd <=?2 and u.status= 'ACTIVE' order by u.authorizationEnd asc")
	Page<User> getAuthorizationEndFromDayToDayByCompany(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay,
														@Param("companyId") Long companyId, Pageable pageable);

	@Query(value = "select u from User u where u.company.id = ?3 AND u.agency.id = ?4 AND u.authorizationEnd >=?1  and u.authorizationEnd <=?2 and u.status= 'ACTIVE' order by u.authorizationEnd asc")
	Page<User> getAuthorizationEndFromDayToDayByCompanyAgency(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay,
															  @Param("companyId") Long companyId, @Param("agencyId") Long agencyId, Pageable pageable);

	@Query(value = "select u.* from user u where u.authorization_end < now() and u.status= 'ACTIVE' order by authorization_end asc", nativeQuery = true)
	List<User> getAuthorizationExpired();

	@Query(value = "select c from User c where c.email = ?1")
	public List<User> findByEmail(String email);

  @Query(value = "SELECT u FROM User u where u.status = 'ACTIVE' AND DATE_FORMAT(u.dateOfBirth,'%m-%d') >= ?1 AND DATE_FORMAT(u.dateOfBirth,'%m-%d') <= ?2 order by dateOfBirth asc")
  Page<User> getAuthorizationBornFromDateToDate(String fromDate, String toDate, Pageable pageable);

  @Query(value = "SELECT u FROM User u where u.company.id = ?3 AND u.status = 'ACTIVE' AND DATE_FORMAT(u.dateOfBirth,'%m-%d') >= ?1 AND DATE_FORMAT(u.dateOfBirth,'%m-%d') <= ?2 order by dateOfBirth asc")
	Page<User> getAuthorizationBornFromDateToDateByCompany(String fromDate, String toDate, Long companyId, Pageable pageable);

  @Query(value = "SELECT u FROM User u where u.company.id = ?3 AND u.agency.id = ?4 AND u.status = 'ACTIVE' AND DATE_FORMAT(u.dateOfBirth,'%m-%d') >= ?1 AND DATE_FORMAT(u.dateOfBirth,'%m-%d') <= ?2 order by dateOfBirth asc")
	Page<User> getAuthorizationBornFromDateToDateByCompanyAgency(String fromDate, String toDate, Long companyId, Long agencyId, Pageable pageable);


	@Query(value = "select u.* from user u where u.ride_line_id =  ?1 " , nativeQuery = true)
	List<User> findAllSeniorsForRideLine(@Param("rideLineId") Long rideLineId);


//	@Query(value = "select u.* from user u where u.ride_line_id is null" , nativeQuery = true)
//	List<User> findAllSeniorsWithoutRideLine(Long agencyId, Long companyId);

	@Query(value = "select u from User u " +
			" where  " +
			"  (u.agency.id = :agency) " +
			"   and (u.company.id = :company)" +
			"	and u.rideLine.id is null")
	List<User> findAllSeniorsWithoutRideLine(@Param("agency") Long agencyId,
								 @Param("company") Long companyId);


	@Query(value = "select U from User U where U.id =  ?1 ")
	User findUserById(@Param("id") Long id);

	@Modifying
	@Query(value = "update User set rideLine = null where rideLine.id =  ?1 ")
	void removeRideLine(Long id);


	@Modifying
	@Query(" UPDATE User SET rideLine = :rideLine WHERE id = :id ")
	int updateRideLine(@Param("id") Long userId, @Param("rideLine") RideLine rideLine);

	@Query(value = "select u from User u where u.company.id = ?1 and u.agency.id = ?2")
	Page<User> findByCompanyAndAgency(Long companyId, Long agencyId, Pageable pageable);

	@Query(value = "select u from User u where u.company.id = ?1")
	Page<User> findByCompanyPage(Long companyId, Pageable pageable);

	@Query(value = "select u from User u where u.agency.id = ?1")
	Page<User> findByAgencyPage(Long agencyId, Pageable pageable);

	@Query(value = "select u from User u where u.company.id = ?1")
	List<User> findByCompanyId(Long companyId);

	@Query(value = "select u from User u where u.vacationStart is not null AND u.vacationEnd >=?1  and u.vacationEnd <=?2 order by u.vacationEnd asc")
	Page<User> getVacationSeniorFromDayToDay(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay, Pageable pageable);

	@Query(value = "select u from User u where u.vacationStart is not null AND u.vacationEnd >=?1  and u.vacationEnd <=?2 and u.company.id = ?3 order by u.vacationEnd asc")
	Page<User> getVacationSeniorFromDayToDayByCompany(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay,
													  @Param("companyId") Long companyId, Pageable pageable);

	@Query(value = "select u from User u where " +
			"u.vacationStart is not null AND u.vacationEnd >=?1  and u.vacationEnd <=?2 and u.company.id = ?3 and u.agency.id = ?4 order by u.vacationEnd asc")
	Page<User> getVacationSeniorFromDayToDayByCompanyAgency(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay,
													  @Param("companyId") Long companyId, @Param("agencyId") Long agencyId, Pageable pageable);


	@Query(value = "select u from User u where u.firstName like %?1%")
	List<User> searchUserByFirstName(@Param("search") String search);

	@Query(value = "select u from User u where u.company.id = ?1 AND u.firstName like %?2% ")
	List<User> searchUserByFirstNameByCompany(@Param("companyId") Long companyId, @Param("search") String search);

	@Query(value = "select u from User u where u.company.id = ?1 AND u.agency.id = ?2 AND u.firstName like %?3% ")
	List<User> searchUserByFirstNameByCompanyAgency(@Param("companyId") Long companyId, @Param("agencyId") Long agencyId, @Param("search") String search);

	@Query(value = "select u from User u where u.assessmentDuration is not null AND u.assessmentStartDate < now() ")
	List<User> searchUserForAssessmentReminder();

	@Query(value="select u.* from user u inner join assessment_user au on u.id=au.user_id where au.id=:assessmentUserId", nativeQuery=true)
	User findByAssessmentUserId(@Param("assessmentUserId") Long assessmentUserId);

}
