package com.healthcare.service.impl;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.TrainingEmployee;
import com.healthcare.repository.TrainingEmployeeRepository;
import com.healthcare.service.TrainingEmployeeService;

import io.jsonwebtoken.lang.Collections;


@Service
@Transactional
public class TrainingEmployeeServiceImpl  extends BasicService<TrainingEmployee, TrainingEmployeeRepository>
 implements TrainingEmployeeService {
    private static final String KEY = TrainingEmployee.class.getSimpleName();

    @Autowired
    TrainingEmployeeRepository trainingEmployeeRepository;

    @Autowired
    private RedisTemplate<String, TrainingEmployee> trainingEmployeeRedisTemplate;


    @Override @Transactional
    public TrainingEmployee save(TrainingEmployee trainingEmployee) {
        trainingEmployee = trainingEmployeeRepository.save(trainingEmployee);
        trainingEmployeeRedisTemplate.opsForHash().put(KEY, trainingEmployee.getId(), trainingEmployee);
        return trainingEmployee;
    }

    @Override @Transactional
    public TrainingEmployee findById(Long id) {
        TrainingEmployee trainingEmployee = (TrainingEmployee) trainingEmployeeRedisTemplate.opsForHash().get(KEY, id);
        if (trainingEmployee == null)
            trainingEmployee = trainingEmployeeRepository.findOne(id);
        return trainingEmployee;
    }

    @Override @Transactional
    public Long deleteById(Long id) {
        trainingEmployeeRepository.delete(id);
        return trainingEmployeeRedisTemplate.opsForHash().delete(KEY, id);
    }

    public Long deleteByTrainingEmployee(TrainingEmployee trainingEmployee) {
        trainingEmployeeRepository.delete(trainingEmployee);
        return trainingEmployeeRedisTemplate.opsForHash().delete(KEY, trainingEmployee.getId());
    }

    @Override @Transactional
    public Long findByTrainingEmployee(TrainingEmployee trainingEmployee) {
    	TrainingEmployee trainingEmployee1 = (TrainingEmployee) trainingEmployeeRedisTemplate.opsForHash().get(KEY, trainingEmployee.getId());
        if (trainingEmployee1 == null)
            trainingEmployee = trainingEmployeeRepository.findOne(trainingEmployee.getId());
        return trainingEmployee.getId();
    }

    @Override @Transactional
    public List<TrainingEmployee> findAll() {
        Map<Object, Object> trainingEmployeeMap = trainingEmployeeRedisTemplate.opsForHash().entries(KEY);
        List<TrainingEmployee> trainingEmployeeList = Collections.arrayToList(trainingEmployeeMap.values().toArray());
        if (trainingEmployeeMap.isEmpty())
            trainingEmployeeList = trainingEmployeeRepository.findAll();
        return trainingEmployeeList;
    }

    @Override @Transactional
    public Long disableById(Long id) {
        //TODO
        return null;
    }
}
