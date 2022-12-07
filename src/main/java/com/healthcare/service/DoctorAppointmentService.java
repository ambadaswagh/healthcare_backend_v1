package com.healthcare.service;

import java.util.List;

import com.healthcare.dto.DoctorAppointmentDTO;
import com.healthcare.model.entity.DoctorAppointment;

public interface DoctorAppointmentService extends IService<DoctorAppointment> {
	public List<DoctorAppointment> search(DoctorAppointmentDTO query);
	public List<DoctorAppointment> getAppointmentNextDays(int numberOfNextDay);
}
