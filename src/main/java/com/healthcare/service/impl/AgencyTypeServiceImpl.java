package com.healthcare.service.impl;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.AgencyType;
import com.healthcare.repository.AgencyTypeRepository;
import com.healthcare.service.AgencyTypeService;

import io.jsonwebtoken.lang.Collections;

@Service
@Transactional
public class AgencyTypeServiceImpl extends BasicService<AgencyType, AgencyTypeRepository> implements AgencyTypeService {
	private static final String KEY = AgencyType.class.getSimpleName();

	@Autowired
	AgencyTypeRepository agencyTypeRepository;

	@Autowired
	private RedisTemplate<String, AgencyType> agencyTypeRedisTemplate;

	@Override @Transactional
	public AgencyType save(AgencyType agencyType) {
		agencyType = agencyTypeRepository.save(agencyType);
		agencyTypeRedisTemplate.opsForHash().put(KEY, agencyType.getId(), agencyType);
		return agencyType;
	}

	@Override @Transactional
	public Long deleteById(Long id) {
		agencyTypeRepository.delete(id);
		return agencyTypeRedisTemplate.opsForHash().delete(KEY, id);
	}

	@Override @Transactional
	public AgencyType findById(Long id) {
		if (agencyTypeRedisTemplate.opsForHash().hasKey(KEY, id))
			return (AgencyType) agencyTypeRedisTemplate.opsForHash().get(KEY, id);
		return agencyTypeRepository.findOne(id);
	}

	@Override @Transactional
	public List<AgencyType> findAll() {
		Map<Object, Object> agencyTypeMap = agencyTypeRedisTemplate.opsForHash().entries(KEY);
		List<AgencyType> agencyTypeList = Collections.arrayToList(agencyTypeMap.values().toArray());
		if (agencyTypeMap.isEmpty())
			agencyTypeList = agencyTypeRepository.findAll();
		return agencyTypeList;
	}

	@Override @Transactional
	public Long disableById(Long id) {
		//TODO
		return null;
	}
}
