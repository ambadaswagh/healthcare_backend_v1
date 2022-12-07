package com.healthcare.service.impl;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.CareGiver;
import com.healthcare.repository.CareGiverRepository;
import com.healthcare.service.CareGiverService;

import io.jsonwebtoken.lang.Collections;

@Service
@Transactional
public class CareGiverServiceImpl extends BasicService<CareGiver, CareGiverRepository> implements CareGiverService {
	private static final String KEY = CareGiver.class.getSimpleName();

	@Autowired
	CareGiverRepository careGiverRepository;

	@Autowired
	private RedisTemplate<String, CareGiver> careGiverRedisTemplate;

	@Override @Transactional
	public CareGiver save(CareGiver careGiver) {
		careGiver = careGiverRepository.save(careGiver);
		careGiverRedisTemplate.opsForHash().put(KEY, careGiver.getId(), careGiver);
		return careGiver;
	}

	@Override @Transactional
	public CareGiver findById(Long id) {
		CareGiver careGiver = (CareGiver) careGiverRedisTemplate.opsForHash().get(KEY, id);
		if (careGiver == null)
			careGiver = careGiverRepository.findOne(id);
		return careGiver;
	}

	@Override @Transactional
	public Long deleteById(Long id) {
		careGiverRepository.delete(id);
		return careGiverRedisTemplate.opsForHash().delete(KEY, id);
	}

	@Override @Transactional
	public List<CareGiver> findAll() {
		Map<Object, Object> careGiverMap = careGiverRedisTemplate.opsForHash().entries(KEY);
		List<CareGiver> careGiverList = Collections.arrayToList(careGiverMap.values().toArray());
		if (careGiverMap.isEmpty())
			careGiverList = careGiverRepository.findAll();
		return careGiverList;
	}

	@Override @Transactional
	public Long disableById(Long id) {
		//TODO
		return null;
	}
}
