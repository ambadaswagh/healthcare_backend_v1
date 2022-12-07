package com.healthcare.repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthcare.model.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

	Employee findById(long l);

	@Query(value = " SELECT emp from Employee emp left join emp.agency agnc left join agnc.company cmp where cmp.id = :companyId and (:agencyId is null or agnc.id = :agencyId) ")
	List<Employee> findByCompany(@Param("companyId") Long companyId, @Param("agencyId") Long agencyId);

	@Query(value = " select t1.worked as weekly_hours_worked, e.* from employee e inner join ("
			+ " select employee_id, SUM(CEIL(TIMESTAMPDIFF(MINUTE, t.start_date, t.end_date)/60)) as worked from("
			+ " select *, case when check_in_time > :startDate then check_in_time else :startDate end as start_date,"
			+ " case when check_out_time > :endDate then :endDate else check_out_time end as end_date"
			+ " from employee_clocking c where check_out_time is not null" + " ) t group by employee_id) t1 on e.id = t1.employee_id"
			+ " where worked > 0 && worked >= e.weekly_working_time_limitation - :hoursDifference", nativeQuery = true)
	List<Employee> findEmployeesCrossingWorkLimit(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("hoursDifference") int hoursDifference);

	@Modifying
	@Query(value = " UPDATE Employee e SET e.weeklyHoursWorked = :weeklyHoursWorked ")
	Integer resetEmployeesWeeklyHours(@Param("weeklyHoursWorked") String weeklyHoursWorked);
	
	@Query(value = " SELECT emp from Employee emp where emp.pin = ?1 ")
	Employee findByPin(@Param("pin") String pin);

	@Query(value = "SELECT e.* FROM employee e " +
			"where e.status = 1 " +
			"AND e.date_of_birth + INTERVAL EXTRACT(YEAR FROM NOW()) - EXTRACT(YEAR FROM e.date_of_birth) YEAR " +
			" BETWEEN CURRENT_DATE() AND CURRENT_DATE() + INTERVAL :days DAY " +
			"order by e.date_of_birth desc", nativeQuery = true)
	List<Employee> getAuthorizationEmployeeBornFromDateToDate(@Param("days") Integer days);

	@Query(value = "SELECT e.* FROM employee e " +
			"where e.status = 1 AND e.company_id = :companyId " +
			"AND e.date_of_birth + INTERVAL EXTRACT(YEAR FROM NOW()) - EXTRACT(YEAR FROM e.date_of_birth) YEAR " +
			" BETWEEN CURRENT_DATE() AND CURRENT_DATE() + INTERVAL :days DAY " +
			"order by e.date_of_birth desc", nativeQuery = true)
	List<Employee> getAuthorizationEmployeeBornFromDateToDateByCompany(@Param("days") Integer days, @Param("companyId") Long companyId);

	@Query(value = "SELECT e.* FROM employee e " +
			"where e.status = 1 AND e.company_id = :companyId AND e.agency_id = :agencyId " +
			"AND e.date_of_birth + INTERVAL EXTRACT(YEAR FROM NOW()) - EXTRACT(YEAR FROM e.date_of_birth) YEAR " +
			" BETWEEN CURRENT_DATE() AND CURRENT_DATE() + INTERVAL :days DAY " +
			"order by e.date_of_birth desc", nativeQuery = true)
	List<Employee> getAuthorizationEmployeeBornFromDateToDateByCompanyAgency(@Param("days") Integer days, @Param("companyId") Long companyId, @Param("agencyId") Long agencyId);

	@Query(value = "select emp from Employee emp where emp.company.id = ?1")
	Page<Employee> findByCompanyId(@Param("companyId") Long companyId, Pageable pageable);

	@Query(value = "SELECT emp from Employee emp where emp.company.id = ?1 AND emp.agency.id = ?2")
	Page<Employee> findByCompanyIdANDAgencyId(@Param("companyId") Long companyId, @Param("agencyId") Long agencyId, Pageable pageable);

	@Query(value = "SELECT emp from Employee emp where emp.company.id = ?1 AND emp.agency.id = ?2")
	List<Employee> findByCpmAndAgencyList(@Param("companyId") Long companyId, @Param("agencyId") Long agencyId);

	@Query(value = "select e from Employee e where e.vacationStart is not null AND e.vacationEnd >=?1  and e.vacationEnd <=?2 order by e.vacationEnd asc")
	Page<Employee> getVacationEmployeeFromDayToDay(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay, Pageable pageable);

	@Query(value = "select e from Employee e where e.vacationStart is not null AND e.vacationEnd >=?1  and e.vacationEnd <=?2 and e.company.id = ?3 order by e.vacationEnd asc")
	Page<Employee> getVacationEmployeeFromDayToDayByCompany(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay,
													  @Param("companyId") Long companyId, Pageable pageable);

	@Query(value = "select e from Employee e where " +
			"e.vacationStart is not null AND e.vacationEnd >=?1  and e.vacationEnd <=?2 and e.company.id = ?3 and e.agency.id = ?4 order by e.vacationEnd asc")
	Page<Employee> getVacationEmployeeFromDayToDayByCompanyAgency(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay,
															@Param("companyId") Long companyId, @Param("agencyId") Long agencyId, Pageable pageable);

	@Query(value = "select emp from Employee emp where emp.company.id = ?1")
	List<Employee> findByCompanyId(@Param("companyId") Long companyId);
}