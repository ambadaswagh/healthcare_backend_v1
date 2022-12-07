package com.healthcare.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.InsuranceBrokerCompany;
import com.healthcare.model.enums.EntityStatusEnum;
import com.healthcare.repository.InsuranceBrokerCompanyRepository;
import com.healthcare.service.InsuranceBrokerCompanyService;

import io.jsonwebtoken.lang.Collections;

@Service
@Transactional
public class InsuranceBrokerCompanyServiceImpl implements InsuranceBrokerCompanyService {
	private static final String KEY = InsuranceBrokerCompany.class.getSimpleName();

	@Autowired
	private InsuranceBrokerCompanyRepository insuranceBrokerCompanyRepository;
	@Autowired
	private RedisTemplate<String, InsuranceBrokerCompanyRepository> redisTemplate;

	@Override
	public InsuranceBrokerCompany save(InsuranceBrokerCompany insuranceBrokerCompany) {
		insuranceBrokerCompany = insuranceBrokerCompanyRepository.save(insuranceBrokerCompany);
		redisTemplate.opsForHash().put(KEY, insuranceBrokerCompany.getId(), insuranceBrokerCompany);
		return insuranceBrokerCompany;
	}

	@Override
	public InsuranceBrokerCompany findById(Long id) {
		if (redisTemplate.opsForHash().hasKey(KEY, id)) {
			return (InsuranceBrokerCompany) redisTemplate.opsForHash().get(KEY, id);
		}
		return insuranceBrokerCompanyRepository.findOne(id);
	}

	@Override
	public Long deleteById(Long id) {
		insuranceBrokerCompanyRepository.delete(id);
		return redisTemplate.opsForHash().delete(KEY, id);
	}

	@Override
	public Long disableById(Long id) {
		InsuranceBrokerCompany insuranceBrokerCompany = findById(id);
		insuranceBrokerCompany.setStatus(EntityStatusEnum.DISABLE.getValue());
		save(insuranceBrokerCompany);
		return insuranceBrokerCompany.getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InsuranceBrokerCompany> findAll() {
		if (!redisTemplate.opsForHash().entries(KEY).isEmpty()) {
			return Collections.arrayToList(redisTemplate.opsForHash().entries(KEY).values().toArray());
		}
		return insuranceBrokerCompanyRepository.findAll();
	}

}
