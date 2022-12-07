package com.healthcare.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthcare.model.entity.Leave;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {

	@Query(value = "select l from Leave l where employee.id = ?1 ")
	List<Leave> findByEmployee(Long id);

	@Query(value = "select t.type, SUM(CEIL(TIMESTAMPDIFF(HOUR, start_date, end_date)/24)) as total from( SELECT  type, "
			+ " CASE WHEN `start` > :fromDate THEN `start` ELSE :fromDate  END as start_date, "
			+ " CASE WHEN `end` > :toDate THEN :toDate ELSE `end` END as end_date "
			+ " FROM absence WHERE employee_id = :employeeId AND status = 1) t GROUP BY t.type", nativeQuery = true)
	public List<Object[]> getStatusByEmployee(@Param("employeeId") Long employeeId, @Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate);
}