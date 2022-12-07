package com.healthcare.repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthcare.model.entity.Driver;
import com.healthcare.model.entity.Employee;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long>, JpaSpecificationExecutor<Driver> {

	Driver findById(long l);

	/*@Query(value = " SELECT dri from Driver dri left join emp.agency agnc left join agnc.company cmp where cmp.id = :companyId and (:agencyId is null or agnc.id = :agencyId) ")
	List<Employee> findByCompany(@Param("companyId") Long companyId, @Param("agencyId") Long agencyId);

	@Query(value = " select t1.worked as weekly_hours_worked, e.* from employee e inner join ("
			+ " select employee_id, SUM(CEIL(TIMESTAMPDIFF(MINUTE, t.start_date, t.end_date)/60)) as worked from("
			+ " select *, case when check_in_time > :startDate then check_in_time else :startDate end as start_date,"
			+ " case when check_out_time is null then NOW() when check_out_time > :endDate then :endDate else check_out_time end as end_date"
			+ " from employee_clocking c" + " ) t group by employee_id) t1 on e.id = t1.employee_id"
			+ " where worked > 0 && worked >= e.weekly_working_time_limitation - :hoursDifference", nativeQuery = true)
	List<Employee> findEmployeesCrossingWorkLimit(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("hoursDifference") int hoursDifference);
	
	@Query(value = " SELECT emp from Employee emp where emp.pin = ?1 ")
	Employee findByPin(@Param("pin") String pin);*/

	@Query(value = "select dri from Driver dri where dri.company.id = ?1")
	Page<Driver> findByCompanyId(Long companyId, Pageable pageable);

	@Query(value = "select dri from Driver dri where dri.company.id = ?1 and dri.agency.id = ?2")
	Page<Driver> findByCompanyIdAndAgency(Long companyId, Long agencyId, Pageable pageable);

	@Query(value = "select dri from Driver dri where dri.company.id = ?1")
	List<Driver> findByCompanyId(Long companyId);

	@Query(value = "select dri from Driver dri where dri.company.id = ?1 and dri.agency.id = ?2")
	List<Driver> findByCompanyIdAndAgency(Long companyId, Long agencyId);

	@Query(value = "select dri from Driver dri where dri.firstName like %?1%")
	List<Driver> searchByFirstName(@Param("search") String search);

	@Query(value = "select dri from Driver dri where dri.company.id = ?1 AND dri.firstName like %?2% ")
	List<Driver> searchByFirstNameByCompany(@Param("companyId") Long companyId, @Param("search") String search);

	@Query(value = "select dri from Driver dri where dri.company.id = ?1 AND dri.agency.id = ?2 AND dri.firstName like %?3% ")
	List<Driver> searchByFirstNameByCompanyAgency(@Param("companyId") Long companyId, @Param("agencyId") Long agencyId, @Param("search") String search);

	@Query(value = "select dri from Driver dri where dri.driverLicenseExpire >=?1  and dri.driverLicenseExpire <=?2 and dri.status= 1 order by dri.driverLicenseExpire asc")
	Page<Driver> getDriverLicenseEndFromDayToDay(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay, Pageable pageable);

	@Query(value = "select dri from Driver dri where dri.company.id = ?3 AND dri.driverLicenseExpire >=?1  and dri.driverLicenseExpire <=?2 and dri.status= 1 order by dri.driverLicenseExpire asc")
	Page<Driver> getDriverLicenseEndFromDayToDayByCompany(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay,
														  @Param("companyId") Long companyId, Pageable pageable);

	@Query(value = "select dri from Driver dri where dri.company.id = ?3 AND dri.agency.id = ?4 and dri.driverLicenseExpire >=?1  and dri.driverLicenseExpire <=?2 and dri.status= 1 order by dri.driverLicenseExpire asc")
	Page<Driver> getDriverLicenseEndFromDayToDayByCompanyAgency(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay,
															  @Param("companyId") Long companyId, @Param("agencyId") Long agencyId, Pageable pageable);

	@Query(value = "select dri from Driver dri where dri.driverTlcFhvLicenseExpire >=?1  and dri.driverTlcFhvLicenseExpire <=?2 and dri.status= 1 order by dri.driverTlcFhvLicenseExpire asc")
	Page<Driver> getDriverTLCFhvLicenseEndFromDayToDay(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay, Pageable pageable);

	@Query(value = "select dri from Driver dri where dri.company.id = ?3 AND dri.driverTlcFhvLicenseExpire >=?1  and dri.driverTlcFhvLicenseExpire <=?2 and dri.status= 1 order by dri.driverTlcFhvLicenseExpire asc")
	Page<Driver> getDriverTLCFhvLicenseEndFromDayToDayByCompany(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay,
														  @Param("companyId") Long companyId, Pageable pageable);

	@Query(value = "select dri from Driver dri where dri.company.id = ?3 AND dri.agency.id = ?4 and dri.driverTlcFhvLicenseExpire >=?1  and dri.driverTlcFhvLicenseExpire <=?2 and dri.status= 1 order by dri.driverTlcFhvLicenseExpire asc")
	Page<Driver> getDriverTLCFhvLicenseEndFromDayToDayByCompanyAgency(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay,
																@Param("companyId") Long companyId, @Param("agencyId") Long agencyId, Pageable pageable);

	@Query(value = "select dri from Driver dri where dri.backgroundCheckExpire >=?1  and dri.backgroundCheckExpire <=?2 and dri.status= 1 order by dri.backgroundCheckExpire asc")
	Page<Driver> getDriverBackgroundCheckEndFromDayToDay(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay, Pageable pageable);

	@Query(value = "select dri from Driver dri where dri.company.id = ?3 AND dri.backgroundCheckExpire >=?1  and dri.backgroundCheckExpire <=?2 and dri.status= 1 order by dri.backgroundCheckExpire asc")
	Page<Driver> getDriverBackgroundCheckEndFromDayToDayByCompany(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay,
																@Param("companyId") Long companyId, Pageable pageable);

	@Query(value = "select dri from Driver dri where dri.company.id = ?3 AND dri.agency.id = ?4 and dri.backgroundCheckExpire >=?1  and dri.backgroundCheckExpire <=?2 and dri.status= 1 order by dri.backgroundCheckExpire asc")
	Page<Driver> getDriverBackgroundCheckEndFromDayToDayByCompanyAgency(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay,
																	  @Param("companyId") Long companyId, @Param("agencyId") Long agencyId, Pageable pageable);

	@Query(value = "select dri from Driver dri where dri.drivingRecordExpire >=?1  and dri.drivingRecordExpire <=?2 and dri.status= 1 order by dri.drivingRecordExpire asc")
	Page<Driver> getDriverDrivingRecordEndFromDayToDay(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay, Pageable pageable);

	@Query(value = "select dri from Driver dri where dri.company.id = ?3 AND dri.drivingRecordExpire >=?1  and dri.drivingRecordExpire <=?2 and dri.status= 1 order by dri.drivingRecordExpire asc")
	Page<Driver> getDriverDrivingRecordEndFromDayToDayByCompany(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay,
																  @Param("companyId") Long companyId, Pageable pageable);

	@Query(value = "select dri from Driver dri where dri.company.id = ?3 AND dri.agency.id = ?4 and dri.drivingRecordExpire >=?1  and dri.drivingRecordExpire <=?2 and dri.status= 1 order by dri.drivingRecordExpire asc")
	Page<Driver> getDriverDrivingRecordEndFromDayToDayByCompanyAgency(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay,
																		@Param("companyId") Long companyId, @Param("agencyId") Long agencyId, Pageable pageable);

	@Query(value = "select dri from Driver dri where dri.drugScreenExpire >=?1  and dri.drugScreenExpire <=?2 and dri.status= 1 order by dri.drugScreenExpire asc")
	Page<Driver> getDriverDrugScreenEndFromDayToDay(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay, Pageable pageable);

	@Query(value = "select dri from Driver dri where dri.company.id = ?3 AND dri.drugScreenExpire >=?1  and dri.drugScreenExpire <=?2 and dri.status= 1 order by dri.drugScreenExpire asc")
	Page<Driver> getDriverDrugScreenEndFromDayToDayByCompany(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay,
																@Param("companyId") Long companyId, Pageable pageable);

	@Query(value = "select dri from Driver dri where dri.company.id = ?3 AND dri.agency.id = ?4 and dri.drugScreenExpire >=?1  and dri.drugScreenExpire <=?2 and dri.status= 1 order by dri.drugScreenExpire asc")
	Page<Driver> getDriverDrugScreenEndFromDayToDayByCompanyAgency(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay,
																	  @Param("companyId") Long companyId, @Param("agencyId") Long agencyId, Pageable pageable);
}