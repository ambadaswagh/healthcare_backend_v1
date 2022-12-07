package com.healthcare.service.impl;

import com.healthcare.model.entity.InsuranceHistory;
import com.healthcare.model.entity.User;
import com.healthcare.repository.InsuranceHistoryRepository;
import com.healthcare.service.InsuranceHistoryService;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class InsuranceHistoryServiceImpl extends BasicService<InsuranceHistory, InsuranceHistoryRepository> implements InsuranceHistoryService {

    private static final String KEY = InsuranceHistory.class.getSimpleName();

    @Autowired
    private InsuranceHistoryRepository insuranceHistoryRepository;

    @Autowired
    private RedisTemplate<String, InsuranceHistory> insuranceHistoryRedisTemplate;

    @Override @Transactional
    public InsuranceHistory save(InsuranceHistory insuranceHistory) {
        insuranceHistory = insuranceHistoryRepository.save(insuranceHistory);
        insuranceHistoryRedisTemplate.opsForHash().put(KEY, insuranceHistory.getId(), insuranceHistory);
        return insuranceHistory;
    }

    @Override @Transactional
    public InsuranceHistory findById(Long id) {
        InsuranceHistory insuranceHistory = (InsuranceHistory) insuranceHistoryRedisTemplate.opsForHash().get(KEY, id);
        if (insuranceHistory == null)
            insuranceHistory = insuranceHistoryRepository.findOne(id);
        return insuranceHistory;
    }

    @Override @Transactional
    public Long deleteById(Long id) {
        insuranceHistoryRepository.delete(id);
        return insuranceHistoryRedisTemplate.opsForHash().delete(KEY, id);
    }

    @Override @Transactional
    public List<InsuranceHistory> findAll() {
        Map<Object, Object> rideMap = insuranceHistoryRedisTemplate.opsForHash().entries(KEY);
        List<InsuranceHistory> insuranceHistoryList = Collections.arrayToList(rideMap.values().toArray());
        if (rideMap.isEmpty())
            insuranceHistoryList = insuranceHistoryRepository.findAll();
        return insuranceHistoryList;
    }

    @Override
    @Transactional
    public List<InsuranceHistory> findByUserId(Long userId) {
            List<InsuranceHistory> insuranceHistories = insuranceHistoryRepository.findByUserId(userId);
            return insuranceHistories;
    }

    @Override @Transactional
    public Long disableById(Long id) {
        //TODO
        return null;
    }
}
