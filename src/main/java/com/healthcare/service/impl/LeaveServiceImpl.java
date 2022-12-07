package com.healthcare.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.api.common.HealthcareUtil;
import com.healthcare.dto.LeaveStatus;
import com.healthcare.model.entity.Leave;
import com.healthcare.repository.LeaveRepository;
import com.healthcare.service.LeaveService;
import com.healthcare.util.DateUtils;

import io.jsonwebtoken.lang.Collections;

@Service
@Transactional
public class LeaveServiceImpl implements LeaveService {
	private static final String KEY = LeaveServiceImpl.class.getSimpleName();

	@Autowired
	LeaveRepository repository;

	@Autowired
	private RedisTemplate<String, Leave> employeeLeaveRedisTemplate;

	@Override
	@Transactional
	public Leave save(Leave employeeLeave) {
		Leave dbLeave = repository.save(employeeLeave);
		employeeLeaveRedisTemplate.opsForHash().put(KEY, dbLeave.getId(), dbLeave);
		return dbLeave;
	}

	@Override
	@Transactional
	public Long deleteById(Long id) {
		repository.delete(id);
		return employeeLeaveRedisTemplate.opsForHash().delete(KEY, id);
	}

	@Override
	public Long disableById(Long id) {
		return null;
	}

	@Override
	@Transactional
	public Leave findById(Long id) {
		if (employeeLeaveRedisTemplate.opsForHash().hasKey(KEY, id))
			return (Leave) employeeLeaveRedisTemplate.opsForHash().get(KEY, id);
		return repository.findOne(id);
	}

	@Override
	@Transactional
	public List<Leave> findAll() {
		Map<Object, Object> employeeLeaveMap = employeeLeaveRedisTemplate.opsForHash().entries(KEY);
		List<Leave> employeeLeaveList = Collections.arrayToList(employeeLeaveMap.values().toArray());
		if (employeeLeaveMap.isEmpty())
			employeeLeaveList = repository.findAll();
		return employeeLeaveList;
	}

	public List<Leave> findByEmployeeId(Long id) {
		return repository.findByEmployee(id);
	}

	public List<LeaveStatus> getStatusByEmployee(Long employeeId) {
		List<Object[]> data = repository.getStatusByEmployee(employeeId,
				DateUtils.getFirstDayOfYear(HealthcareUtil.getCurrentYear()),
				DateUtils.getLastDayOfYear(HealthcareUtil.getCurrentYear()));
		List<LeaveStatus> status = new ArrayList<LeaveStatus>();
		data.forEach(object -> {
			status.add(new LeaveStatus().fromDBObject(object));
		});
		return status;
	}

}
