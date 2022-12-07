package com.healthcare.service.impl;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.HomeVisit;
import com.healthcare.repository.HomeVisitRepository;
import com.healthcare.service.HomeVisitService;

import io.jsonwebtoken.lang.Collections;

@Service
@Transactional
public class HomeVisitServiceImpl extends BasicService<HomeVisit, HomeVisitRepository>
implements HomeVisitService {
	private static final String KEY = HomeVisit.class.getSimpleName();

	@Autowired
	HomeVisitRepository homeVisitRepository;

	@Autowired
	private RedisTemplate<String, HomeVisit> homeVisitRedisTemplate;

	@Override @Transactional
	public HomeVisit save(HomeVisit homeVisit) {
		homeVisit = homeVisitRepository.save(homeVisit);
		homeVisitRedisTemplate.opsForHash().put(KEY, homeVisit.getId(), homeVisit);
		return homeVisit;
	}

	@Override @Transactional
	public HomeVisit findById(Long id) {
		HomeVisit homeVisit = (HomeVisit) homeVisitRedisTemplate.opsForHash().get(KEY, id);
		if (homeVisit == null)
			homeVisit = homeVisitRepository.findOne(id);
		return homeVisit;
	}

	@Override @Transactional
	public Long deleteById(Long id) {
		homeVisitRepository.delete(id);
		return homeVisitRedisTemplate.opsForHash().delete(KEY, id);
	}

	@Override @Transactional
	public List<HomeVisit> findAll() {
		Map<Object, Object> homeVisitMap = homeVisitRedisTemplate.opsForHash().entries(KEY);
		List<HomeVisit> homeVisitList = Collections.arrayToList(homeVisitMap.values().toArray());
		if (homeVisitMap.isEmpty())
			homeVisitList = homeVisitRepository.findAll();
		return homeVisitList;
	}

	@Override @Transactional
	public Long disableById(Long id) {
		//TODO
		return null;
	}
}
