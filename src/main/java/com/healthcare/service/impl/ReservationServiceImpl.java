package com.healthcare.service.impl;

import com.healthcare.model.entity.Reservation;
import com.healthcare.repository.ReservationRepository;
import com.healthcare.service.ReservationService;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {
	private static final String KEY = ReservationServiceImpl.class.getSimpleName();

	@Autowired
    ReservationRepository repository;

	@Autowired
	private RedisTemplate<String, Reservation> reservationRedisTemplate;

	@Override @Transactional
	public Reservation  save(Reservation reservation) {
		reservation = repository.save(reservation);
		reservationRedisTemplate.opsForHash().put(KEY, reservation.getId(), reservation);
		return reservation;
	}

	@Override
	public Reservation findById(Long id) {
    if (reservationRedisTemplate.opsForHash().hasKey(KEY, id)) {
      return (Reservation) reservationRedisTemplate.opsForHash().get(KEY, id);
    }
    return repository.findOne(id);
	}

	@Override
	public Long deleteById(Long id) {
    repository.delete(id);
    return reservationRedisTemplate.opsForHash().delete(KEY, id);
	}

	@Override
	public Long disableById(Long id) {
		return null;
	}

	@Override
	@Transactional
	public List<Reservation> findAll() {
    Map<Object, Object> reservationMap = reservationRedisTemplate.opsForHash().entries(KEY);
    List<Reservation> standingOrderList = Collections.arrayToList(reservationMap.values().toArray());
    if (reservationMap.isEmpty()) {
      standingOrderList = repository.findAll();
    }
    return standingOrderList;
	}

	@Override
	public List<Reservation> findBySeniorId(Long seniorId) {
		return repository.getByUser(seniorId);
	}
}
