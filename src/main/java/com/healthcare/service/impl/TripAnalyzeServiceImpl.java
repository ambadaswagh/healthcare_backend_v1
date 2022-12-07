package com.healthcare.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.healthcare.model.entity.TripAnalyze;
import com.healthcare.repository.TripAnalyzeRepository;
import com.healthcare.service.TripAnalyzeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class TripAnalyzeServiceImpl extends BasicService<TripAnalyze, TripAnalyzeRepository> implements TripAnalyzeService {

    private static final String KEY = TripAnalyze.class.getSimpleName();

    @Autowired
    TripAnalyzeRepository tripAnalyzeRepository;

    @Autowired
    private RedisTemplate<String, TripAnalyze> tripAnalyzeRedisTemplate;

    @Override @Transactional
    public TripAnalyze save(TripAnalyze ride) {
        ride = tripAnalyzeRepository.save(ride);
        tripAnalyzeRedisTemplate.opsForHash().put(KEY, ride.getId(), ride);
        return ride;
    }

    @Override @Transactional
    public TripAnalyze findById(Long id) {
        TripAnalyze tripAnalyze = (TripAnalyze) tripAnalyzeRedisTemplate.opsForHash().get(KEY, id);
        if (tripAnalyze == null)
            tripAnalyze = tripAnalyzeRepository.findOne(id);
        return tripAnalyze;
    }

    @Override @Transactional
    public Long deleteById(Long id) {
        tripAnalyzeRepository.delete(id);
        return tripAnalyzeRedisTemplate.opsForHash().delete(KEY, id);
    }

    @Override @Transactional
    public Long disableById(Long id) {
        //TODO
        return null;
    }

    @Override @Transactional
    public List<Map<String, String>> findDriverFrequency(Long userId){
        List<Object[]> data = tripAnalyzeRepository.findDriverFrequency(userId);
        if(data == null){
            return Lists.newArrayList();
        } else {
            return data.stream().map(trip -> {
                Map<String, String> tripData = Maps.newHashMap();
                tripData.put("firstName", trip[0].toString());
                tripData.put("lastName", trip[1].toString());
                tripData.put("count", trip[2].toString());
                return tripData;
            }).collect(Collectors.toList());
        }
    }
}
