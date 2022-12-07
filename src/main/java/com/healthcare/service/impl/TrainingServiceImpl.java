package com.healthcare.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.Training;
import com.healthcare.model.enums.EntityStatusEnum;
import com.healthcare.repository.TrainingRepository;
import com.healthcare.service.TrainingService;

import io.jsonwebtoken.lang.Collections;

/**
 * Created by Jean Antunes on 24/05/17.
 */

@Service
@Transactional
public class TrainingServiceImpl extends BasicService<Training, TrainingRepository>
implements TrainingService {
	private static final String KEY = Training.class.getSimpleName();

	@Autowired
	TrainingRepository trainingRepository;

	@Autowired
	private RedisTemplate<String, Training> trainingRedisTemplate;

	@Override @Transactional
	public Training save(Training user) {
		if(user.getId() != null){
			user.setUpdatedAt(new Timestamp(new Date().getTime()));
	  	} else {
	  		user.setCreatedAt(new Timestamp(new Date().getTime()));
	  		user.setUpdatedAt(new Timestamp(new Date().getTime()));
	  	}
		user = trainingRepository.save(user);
		trainingRedisTemplate.opsForHash().put(KEY, user.getId(), user);
		return user;
	}

	@Override @Transactional
	public Training findById(Long id) {
		/*Training training = (Training) trainingRedisTemplate.opsForHash().get(KEY, id);
		if (training == null)
			training = trainingRepository.findOne(id);*/
		return trainingRepository.findOne(id);
	}

	@Override @Transactional
	public Long deleteById(Long id) {
		trainingRepository.delete(id);
		return trainingRedisTemplate.opsForHash().delete(KEY, id);
	}

	@SuppressWarnings("unchecked")
	@Override @Transactional
	public List<Training> findAll() {
		Map<Object, Object> trainingMap = trainingRedisTemplate.opsForHash().entries(KEY);
		List<Training> trainingList = Collections.arrayToList(trainingMap.values().toArray());
		if (trainingMap.isEmpty())
			trainingList = trainingRepository.findAll();
		return trainingList;
	}
	
	@Override @Transactional
	public List<Training> findByTraineeId(String id) {
		List<Training> trainings = findAll();
		List<Training> trainingsTrainee = new ArrayList<>();
		for (Training training : trainings) {
			if (Arrays.asList(training.getTrainee().split("[,]")).contains(id)) {
				trainingsTrainee.add(training);
			}
		}
		return trainingsTrainee;
	}

	@Override @Transactional
	public Long disableById(Long id) {
		Training training = null;
		if (trainingRedisTemplate.opsForHash().hasKey(KEY, id))
			training = (Training) trainingRedisTemplate.opsForHash().get(KEY, id);
		else
			training = trainingRepository.findById(id);
		if (training != null && training.getId() != null) {
			training.setStatus(EntityStatusEnum.DISABLE.getValue());
			trainingRepository.save(training);
			trainingRedisTemplate.opsForHash().put(KEY, training.getId(), training);
			return training.getId();
		}
		return null;
	}

	@Override
	public Page<Training> findAllByAgency(Long agencyId, Pageable pageable) {
		return trainingRepository.findAllByAgency(agencyId, pageable);
	}

	@Override
	public Page<Training> findAllByAgencies(List<Long> agencies, Pageable pageable) {
		return trainingRepository.findAllByAgencies(agencies, pageable);
	}
}
