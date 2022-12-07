package com.healthcare.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.dto.DoctorAppointmentDTO;
import com.healthcare.model.entity.DoctorAppointment;
import com.healthcare.repository.DoctorAppointmentRepository;
import com.healthcare.service.DoctorAppointmentService;

/**
 * Activity service
 */
@Service
@Transactional
public class DoctorAppointmentServiceImpl implements DoctorAppointmentService {

	@Autowired
	private DoctorAppointmentRepository doctorAppointmentRepository;

	@Override
	public DoctorAppointment save(DoctorAppointment entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DoctorAppointment findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long deleteById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long disableById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DoctorAppointment> search(DoctorAppointmentDTO query) {
		if(query == null){
			return new ArrayList<DoctorAppointment>();
		}
		if(query.getUserId() != null && query.getUserId().equals(0)){
			query.setUserId(-1);
		}
		if(query.getStatus() != null && query.getStatus().equals(0)){
			query.setStatus(-1);
		}
		query.setOffset(query.getPage() * query.getPerpage());
		return doctorAppointmentRepository.search(query); 
	}
	
	@Override
	public List<DoctorAppointment> getAppointmentNextDays(int numberOfNextDay){
		if(numberOfNextDay < 0) return new ArrayList<DoctorAppointment>();
		DateTime currentDate = new DateTime();
		currentDate = currentDate.plusDays(numberOfNextDay);
		DoctorAppointmentDTO search = new DoctorAppointmentDTO();
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
		search.setToDate(dtf.print(currentDate));
		return search(search);
	}

}
