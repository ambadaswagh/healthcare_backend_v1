package com.healthcare.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.VisitActivity;
import com.healthcare.model.entity.VisitActivityPK;
import com.healthcare.repository.VisitActivityRepository;
import com.healthcare.service.VisitActivityService;

import io.jsonwebtoken.lang.Collections;

@Service
@Transactional
public class VisitActivityServiceImpl implements VisitActivityService {
	private static final String KEY = VisitActivity.class.getSimpleName();

	@Autowired
	VisitActivityRepository visitActivityRepository;

	@Autowired
	private RedisTemplate<String, VisitActivity> visitActivityRedisTemplate;

	@Override @Transactional
	public VisitActivity save(VisitActivity visitActivity) {
		visitActivity = visitActivityRepository.save(visitActivity);
		visitActivityRedisTemplate.opsForHash().put(KEY, getPk(visitActivity), visitActivity);
		return visitActivity;
	}

	@Override @Transactional
	public VisitActivity findById(VisitActivityPK pk) {
		VisitActivity visitActivity = (VisitActivity) visitActivityRedisTemplate.opsForHash().get(KEY, pk);
		if (visitActivity == null || visitActivity.getActivity() == null || visitActivity.getVisit() == null) {
			visitActivity = visitActivityRepository.findOne(pk);
			visitActivityRedisTemplate.opsForHash().put(KEY, getPk(visitActivity), visitActivity);
		}

		return visitActivity;
	}

	@Override @Transactional
	public Long deleteById(VisitActivityPK pk) {
		visitActivityRepository.delete(pk);
		return visitActivityRedisTemplate.opsForHash().delete(KEY, pk);
	}

	@Override @Transactional
	public List<VisitActivity> findVisitActivityByActivityId(Long id) {
		List<VisitActivity> visitActivityReturnList = getList(id, true);

		if (visitActivityReturnList.isEmpty() || checkVisitOrActivityIsNull(visitActivityReturnList)) {
			visitActivityReturnList = visitActivityRepository.findVisitActivityByActivityId(id);
			putResultInRedis(visitActivityReturnList);
		}

		return visitActivityReturnList;
	}

	private void putResultInRedis(List<VisitActivity> visitActivityReturnList) {
		for (VisitActivity visitActivity : visitActivityReturnList) {
			visitActivityRedisTemplate.opsForHash().put(KEY, getPk(visitActivity), visitActivity);
		}
	}

	private boolean checkVisitOrActivityIsNull(List<VisitActivity> visitActivityReturnList) {
		for (VisitActivity visitActivity : visitActivityReturnList) {
			if (visitActivity.getActivity() == null || visitActivity.getVisit() == null) {
				return true;
			}
		}
		return false;
	}

	@Override @Transactional
	public List<VisitActivity> findVisitActivityByVisitId(Long id) {
		List<VisitActivity> visitActivityReturnList = getList(id, false);

		if (visitActivityReturnList.isEmpty() || checkVisitOrActivityIsNull(visitActivityReturnList)) {
			visitActivityReturnList = visitActivityRepository.findVisitActivityByVisitId(id);
			putResultInRedis(visitActivityReturnList);
		}

		return visitActivityReturnList;
	}

	private List<VisitActivity> getList(Long id, boolean isActivityId) {
		List<VisitActivity> visitActivityReturnList = new ArrayList<VisitActivity>();
		Map<Object, Object> visitActivityMap = visitActivityRedisTemplate.opsForHash().entries(KEY);
		List<VisitActivityPK> visitActivityPkList = Collections.arrayToList(visitActivityMap.keySet().toArray());

		if (!visitActivityMap.isEmpty()) {
			for (VisitActivityPK pk : visitActivityPkList) {
				if (matchFound(id, isActivityId, pk)) {
					visitActivityReturnList.add((VisitActivity) visitActivityRedisTemplate.opsForHash().get(KEY, pk));
				}
			}
		}
		return visitActivityReturnList;
	}

	private boolean matchFound(Long id, boolean isActivityId, VisitActivityPK pk) {
		if (isActivityId && pk.getActivityId().intValue() == id.intValue()) {
			return true;
		} else if (!isActivityId && pk.getVisitId().intValue() == id.intValue()) {
			return true;
		}
		return false;
	}

	@Override @Transactional
	public VisitActivity findById(Long id) {
		return null;
	}

	@Override @Transactional
	public Long deleteById(Long id) {
		return null;
	}

	private VisitActivityPK getPk(VisitActivity visitActivity) {
		return new VisitActivityPK(visitActivity.getVisitId(), visitActivity.getActivityId());
	}

	@Override @Transactional
	public Long disableById(Long id) {
		//TODO
		return null;
	}
	
	@Override
	public Page<VisitActivity> findVisitActivityByVisitId(Long visitId, Pageable pageable) {
		return visitActivityRepository.findVisitActivityByVisitId(visitId, pageable); 
	}

	@Override
	public Page<VisitActivity> findVisitActivityByActivityId(Long activityId, Pageable pageable) {
		return visitActivityRepository.findVisitActivityByActivityId(activityId, pageable); }

}
