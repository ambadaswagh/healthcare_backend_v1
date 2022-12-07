package com.healthcare.service.impl;

import com.healthcare.model.entity.RideLine;
import com.healthcare.model.entity.RideLine;
import com.healthcare.model.entity.User;
import com.healthcare.repository.RideLineRepository;
import com.healthcare.repository.RideLineRepository;
import com.healthcare.repository.RideRepository;
import com.healthcare.repository.UserRepository;

import com.healthcare.service.RideLineService;
import com.healthcare.service.RideLineService;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RideLineServiceImpl extends BasicService<RideLine, RideLineRepository> implements RideLineService {
	private static final String KEY = RideLine.class.getSimpleName();

	@Autowired
	RideLineRepository rideLineRepository;

	@Autowired
	RideRepository rideRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired

	private RedisTemplate<String, RideLine> rideLineRedisTemplate;

	@Override @Transactional
	public RideLine save(RideLine rideLine) {
		rideLine = rideLineRepository.save(rideLine);
		rideLineRedisTemplate.opsForHash().put(KEY, rideLine.getId(), rideLine);
		return rideLine;
	}

	@Override @Transactional
	public RideLine findById(Long id) {
		RideLine rideLine = (RideLine) rideLineRedisTemplate.opsForHash().get(KEY, id);
		if (rideLine == null)
			rideLine = rideLineRepository.findOne(id);
		return rideLine;
	}

	@Override @Transactional
	public Long deleteById(Long id) {
		rideLineRepository.delete(id);
		return rideLineRedisTemplate.opsForHash().delete(KEY, id);
	}

	@Override @Transactional
	public List<RideLine> findAll() {
		Map<Object, Object> rideLineMap = rideLineRedisTemplate.opsForHash().entries(KEY);
		List<RideLine> rideLineList = Collections.arrayToList(rideLineMap.values().toArray());
		if (rideLineMap.isEmpty())
			rideLineList = rideLineRepository.findAll();
		return rideLineList;
	}

	@Override @Transactional
	public Long disableById(Long id) {
		//TODO
		return null;
	}

	public List<RideLine> getDistRideLineForRides(Long agencyId, Long companyId) {
		List<RideLine> rideLines = rideLineRepository.getDistRideLineForRides(agencyId, companyId);
		return rideLines;
	}
	public List<RideLine> getRideLines(Long agencyId, Long companyId) {
		List<RideLine> rideLines = rideLineRepository.getRideLines(agencyId, companyId);
		for(RideLine rideLine : rideLines) {
			List<User> users = userRepository.findAllSeniorsForRideLine(rideLine.getId());
			List<String> seniors = new ArrayList<>();
			for(User user : users) {
				seniors.add(user.getFirstName());
			}
			rideLine.setSeniors(String.join(",", seniors));
			rideLine.setCount(new Long(seniors.size()));
		}
		return rideLines;
	}

	public Integer updateStatus(Long id, Long status) {
		rideLineRepository.updateStatus(id, status);
		rideRepository.updateStatus(id, status);
		return 1;
	}

	@Override
	public Page<RideLine> getRideLineByAgencyList(Long agencyId, Pageable pageable) {
		return rideLineRepository.getRideLineByAgencyList(agencyId, pageable);
	}

	@Override
	public Page<RideLine> findRideLineByAgenciesList(List<Long> agencyIds, Pageable pageable) {
		return rideLineRepository.findRideLineByAgenciesList(agencyIds, pageable);
	}
}
