package com.healthcare.service;

import com.healthcare.model.entity.Reservation;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ReservationService extends IService<Reservation> {

	/**
	 * find all reservations
	 * @return
	 */
	List<Reservation> findAll();

	/**
	 * find reservation by senior id
	 * @param seniorId
	 * @return
	 */
	 List<Reservation> findBySeniorId(Long seniorId);
}
