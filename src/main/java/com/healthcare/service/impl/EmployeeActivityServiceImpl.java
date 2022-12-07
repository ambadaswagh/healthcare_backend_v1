package com.healthcare.service.impl;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.EmployeeActivity;
import com.healthcare.repository.EmployeeActivityRepository;
import com.healthcare.service.EmployeeActivityService;

import io.jsonwebtoken.lang.Collections;

@Service
@Transactional
public class EmployeeActivityServiceImpl extends BasicService<EmployeeActivity, EmployeeActivityRepository>
		implements EmployeeActivityService {
	private static final String KEY = EmployeeActivity.class.getSimpleName();

	@Autowired
	EmployeeActivityRepository employeeActivityRepository;

	@Autowired
	private RedisTemplate<String, EmployeeActivity> employeeActivityRedisTemplate;

	@Override
	@Transactional
	public EmployeeActivity save(EmployeeActivity employeeActivity) {
		employeeActivity = employeeActivityRepository.save(employeeActivity);
		employeeActivityRedisTemplate.opsForHash().put(KEY, employeeActivity.getId(), employeeActivity);
		return employeeActivity;
	}

	@Override
	@Transactional
	public EmployeeActivity findById(Long id) {
		return employeeActivityRepository.findOne(id);
	}

	@Override
	@Transactional
	public Long deleteById(Long id) {
		employeeActivityRepository.delete(id);
		return employeeActivityRedisTemplate.opsForHash().delete(KEY, id);
	}

	@Override
	@Transactional
	public Long deleteByEmployeeActivity(EmployeeActivity employeeActivity) {
		employeeActivityRepository.delete(employeeActivity);
		return employeeActivityRedisTemplate.opsForHash().delete(KEY, employeeActivity.getId());
	}

	@Override
	@Transactional
	public Long findByEmployeeActivity(EmployeeActivity employeeActivity) {
		employeeActivityRedisTemplate.opsForHash().get(KEY, employeeActivity);
		if (employeeActivity == null)
			employeeActivity = employeeActivityRepository.findOne(employeeActivity.getId());
		return employeeActivity.getId();
	}

	@Override
	@Transactional
	public List<EmployeeActivity> findAll() {
		Map<Object, Object> employeeActivityMap = employeeActivityRedisTemplate.opsForHash().entries(KEY);
		List<EmployeeActivity> employeeActivityList = Collections.arrayToList(employeeActivityMap.values().toArray());
		if (employeeActivityMap.isEmpty())
			employeeActivityList = employeeActivityRepository.findAll();
		return employeeActivityList;
	}

	@Override
	@Transactional
	public Long disableById(Long id) {
		// TODO
		return null;
	}
}
