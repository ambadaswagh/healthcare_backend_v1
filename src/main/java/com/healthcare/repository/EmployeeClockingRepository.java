package com.healthcare.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthcare.model.entity.EmployeeClocking;

@Repository
public interface EmployeeClockingRepository
		extends JpaRepository<EmployeeClocking, Long>, JpaSpecificationExecutor<EmployeeClocking> {
	@Query(value = "select * from employee_clocking e where e.check_in_time >= ?1 and e.check_out_time <= ?2 and e.employee_id = ?3 ", nativeQuery = true)
	List<EmployeeClocking> generateReport(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("employeeId") long employeeId);

	@Query(value = "select * from employee_clocking e where e.employee_id = ?1 order by id desc limit 1", nativeQuery = true)
	EmployeeClocking getLastClockingByEmployee(@Param("employeeId") long employeeId);

	@Query(value = "select c.*, case when c.check_in_time >= :fromDate then" 
			+ " c.check_in_time else DATE_FORMAT(:fromDate,'%Y-%m-%d %H:%i:%s') end as startTime," 
			+ " case when c.check_out_time >= :toDate then DATE_FORMAT(:toDate,'%Y-%m-%d %H:%i:%s') else"
			+ " c.check_out_time end as endTime"
			+ " from employee_clocking c where c.employee_id = :employeeId and check_in_time <= :toDate", nativeQuery = true)
	public List<Object[]> getWorkingByEmployee(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("employeeId") Long employeeId);

	@Query(value = "select c.* from employee_clocking c where c.check_out_time is not null AND c.check_in_time >=?1 AND c.check_in_time <=?2", nativeQuery = true)
	List<EmployeeClocking> findAllByStartDateEndDate(@Param("fromDate") Date fromDate, @Param("endDate") Date endDate);

	@Query(value = "select c.* from employee_clocking c inner join employee e ON c.employee_id = e.id where c.check_out_time is not null AND c.check_in_time >=?1 AND c.check_in_time <=?2 AND e.company_id = ?3", nativeQuery = true)
	List<EmployeeClocking> findAllByStartDateEndDateByCompany(
			@Param("fromDate") Date fromDate, @Param("endDate") Date endDate, @Param("companyId") Long companyId);

	@Query(value = "select c.* from employee_clocking c inner join employee e ON c.employee_id = e.id where c.check_out_time is not null AND c.check_in_time >=?1 AND c.check_in_time <=?2 " +
			"AND e.company_id = ?3 AND e.agency_id = ?4", nativeQuery = true)
	List<EmployeeClocking> findAllByStartDateEndDateByCompanyAgency(
			@Param("fromDate") Date fromDate, @Param("endDate") Date endDate,
			@Param("companyId") Long companyId, @Param("agencyId") Long agencyId);


	@Query(value = "SELECT * FROM employee_clocking c INNER JOIN (select id, case when (MONTH(e.accrual_period_start) > MONTH(now()) OR (MONTH(e.accrual_period_start) = MONTH(now()) AND DAY(e.accrual_period_start) > DAY(now()))) " +
			"THEN STR_TO_DATE(CONCAT(YEAR(now()) - 1,'-',MONTH(e.accrual_period_start) ,'-', DAY(e.accrual_period_start)), '%Y-%m-%d') " +
			"else STR_TO_DATE(CONCAT(YEAR(now()) ,'-',MONTH(e.accrual_period_start) ,'-', DAY(e.accrual_period_start)), '%Y-%m-%d') end as startDate " +
			"from employee e where e.accrual_period_start is not null) t ON t.id = c.employee_id WHERE c.check_out_time is not null AND c.check_in_time >= t.startDate AND c.check_in_time <= now()"
			, nativeQuery = true)
	List<EmployeeClocking> findAllByAccrualTime();

	@Query(value = "SELECT ec from EmployeeClocking ec where ec.employee.id = ?1")
	Page<EmployeeClocking> findByEmployeeId(Long employeeId, Pageable pageable);

	@Query(value = "SELECT ec from EmployeeClocking ec where ec.employee.id IN ?1")
	Page<EmployeeClocking> findByeEmployeeIds(List<Long> employeeIds, Pageable pageable);
	
	@Query(value = "SELECT ec from EmployeeClocking ec where DATE(ec.checkInTime) >=:startDate AND DATE(ec.checkInTime) <=:endDate ORDER BY ec.checkInTime ASC")
	Page<EmployeeClocking> findAllByStartDateEndDate(@Param("startDate") Date fromDate, @Param("endDate") Date endDate, Pageable pageable);

	@Query(value = "SELECT ec from EmployeeClocking ec where DATE(ec.checkInTime) >=:startDate AND DATE(ec.checkInTime) <=:endDate AND ec.employee.id = :employeeId ORDER BY ec.checkInTime ASC")
	Page<EmployeeClocking> findAllByStartDateEndDateEmployeeId(@Param("startDate") Date fromDate, @Param("endDate") Date endDate, @Param("employeeId") Long employeeId, Pageable pageable);


}