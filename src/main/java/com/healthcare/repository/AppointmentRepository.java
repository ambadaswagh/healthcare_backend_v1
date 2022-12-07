package com.healthcare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthcare.dto.AppointmentSearchDTO;
import com.healthcare.model.entity.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {
	@Query("select a from Appointment a where (a.comment like %:#{#dto.search}% or a.reason like %:#{#dto.search}%) "
			+ " AND (:#{#dto.fromDate} is null OR a.appointmentTime >= :#{#dto.fromDate}) "
			+ " AND (:#{#dto.toDate} is null OR a.appointmentTime <= :#{#dto.toDate}) " )
	Page<Appointment> search(@Param("dto") AppointmentSearchDTO dto, Pageable pageable);

	@Query("select a from Appointment a where "
			+ " a.user in (select u from User u where u.company.id = :#{#companyId}) "
			+ " AND (a.comment like %:#{#dto.search}% or a.reason like %:#{#dto.search}%) "
			+ " AND (:#{#dto.fromDate} is null OR a.appointmentTime >= :#{#dto.fromDate}) "
			+ " AND (:#{#dto.toDate} is null OR a.appointmentTime <= :#{#dto.toDate}) " )
	Page<Appointment> searchByCompany(@Param("companyId") Long companyId, @Param("dto") AppointmentSearchDTO dto, Pageable pageable);

	@Query("select a from Appointment a where "
			+ " a.user in (select u from User u where u.agency.id = :#{#agencyId}) "
			+ " AND (a.comment like %:#{#dto.search}% or a.reason like %:#{#dto.search}%) "
			+ " AND (:#{#dto.fromDate} is null OR a.appointmentTime >= :#{#dto.fromDate}) "
			+ " AND (:#{#dto.toDate} is null OR a.appointmentTime <= :#{#dto.toDate}) " )
	Page<Appointment> searchByAgency(@Param("agencyId") Long agencyId, @Param("dto") AppointmentSearchDTO dto, Pageable pageable);
}