package com.healthcare.service.impl;

import static org.springframework.util.Assert.notNull;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.transaction.Transactional;

import com.healthcare.dto.BasicReportFilterDTO;
import com.healthcare.model.enums.EntityStatusEnum;
import com.healthcare.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.Activity;
import com.healthcare.repository.ActivityRepository;
import com.healthcare.service.ActivityService;

import io.jsonwebtoken.lang.Collections;

/**
 * Activity service
 */
@Service
@Transactional
public class ActivityServiceImpl  extends BasicService<Activity, ActivityRepository> implements ActivityService {

	private static final String REDIS_KEY = Activity.class.getSimpleName();

	private ActivityRepository activityRepository;
	private RedisTemplate<String, Activity> redisTemplate;

	@Autowired
	private VisitRepository visitRepository;

	@Autowired
	public ActivityServiceImpl(ActivityRepository activityRepository, RedisTemplate<String, Activity> redisTemplate) {
		this.activityRepository = activityRepository;
		this.redisTemplate = redisTemplate;
	}

	@Nonnull
	@Override @Transactional
	public Activity save(@Nonnull Activity activity) {
		notNull(activity, "Activity must not be null");
		if(activity.getId() != null){
			activity.setUpdatedAt(new Timestamp(new Date().getTime()));
    	} else {
    		activity.setCreatedAt(new Timestamp(new Date().getTime()));
    		activity.setUpdatedAt(new Timestamp(new Date().getTime()));
    	}
		Activity savedActivity = activityRepository.save(activity);
		redisTemplate.opsForHash().put(REDIS_KEY, savedActivity.getId(), savedActivity);

		return savedActivity;
	}

	@Nullable
	@Override @Transactional
	public Activity findById(@Nonnull Long id) {
		notNull(id, "Activity Id must not be null");

		Object activity = redisTemplate.opsForHash().get(REDIS_KEY, id);
		if (activity != null) {
			return ((Activity) activity);
		}

		return activityRepository.findOne(id);
	}

	@Override @Transactional
	public Long deleteById(@Nonnull Long id) {
		notNull(id, "Activity Id must not be null");

		activityRepository.delete(id);
		return redisTemplate.opsForHash().delete(REDIS_KEY, id);
	}

	@Override @Transactional
	public Long disableById(@Nonnull Long id) {
		Activity activity = null;
		if (redisTemplate.opsForHash().hasKey(REDIS_KEY, id))
			activity = (Activity) redisTemplate.opsForHash().get(REDIS_KEY, id);
		else
			activity = activityRepository.findOne(id);
		if (activity != null && activity.getId() != null) {
			activity.setStatus(EntityStatusEnum.DISABLE.getValue());
			activityRepository.save(activity);
			redisTemplate.opsForHash().put(REDIS_KEY, activity.getId(), activity);
			return activity.getId();
		}
		return null;
	}

	@Override @Transactional
	public List<Activity> findAll() {
		Map<Object, Object> activityMap = redisTemplate.opsForHash().entries(REDIS_KEY);
		List<Activity> activityList = Collections.arrayToList(activityMap.values().toArray());
		if (activityMap.isEmpty())
			activityList = activityRepository.findAll();
		return activityList;
	}

	@Override
	@Transactional
	public List<Activity> getActivityByUser(BasicReportFilterDTO brf) {
		return   activityRepository.findAllActivityForUser(brf.getStartDate(),brf.getEndDate(),brf.getUserId());
	}

	@Override
	@Transactional
	public Page<Activity> findAllByAgency(Long agencyId, Pageable pageable) {
		return activityRepository.findAllByAgency(agencyId, pageable);
	}

	@Override
	@Transactional
	public List<Activity> findAllByAgencyList(Long agencyId) {
		return activityRepository.findAllByAgencyList(agencyId);
	}

	@Override
	@Transactional
	public Page<Activity> findAllByAgencies(List<Long> agencies, Pageable pageable) {
		return activityRepository.findAllByAgencies(agencies, pageable);
	}

	@Override
	@Transactional
	public List<Activity> findAllByAgenciesList(List<Long> agencies) {
		return activityRepository.findAllByAgenciesList(agencies);
	}
}
