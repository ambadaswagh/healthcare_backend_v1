package com.healthcare.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.healthcare.dto.AppointmentSearchDTO;
import com.healthcare.model.entity.Appointment;

/**
 * AppointmentService methods
 */
public interface AppointmentService extends IService<Appointment>, IFinder<Appointment> {
	public Page<Appointment> search(AppointmentSearchDTO dto, Pageable pageable);
	public Page<Appointment> searchByCompany(Long companyId, AppointmentSearchDTO dto, Pageable pageable);
	public Page<Appointment> searchByAgency(Long agencyId, AppointmentSearchDTO dto, Pageable pageable);

	void doSendingReminder();
}
