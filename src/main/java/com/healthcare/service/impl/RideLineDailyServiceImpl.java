package com.healthcare.service.impl;


import com.healthcare.model.entity.Agency;
import com.healthcare.model.entity.Company;
import com.healthcare.model.entity.RideLine;
import com.healthcare.model.entity.RideLineDaily;
import com.healthcare.repository.RideLineDailyRepository;
import com.healthcare.repository.RideLineRepository;
import com.healthcare.repository.RideRepository;
import com.healthcare.service.RideLineDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class RideLineDailyServiceImpl extends BasicService<RideLineDaily, RideLineDailyRepository> implements RideLineDailyService {

    private static final String KEY = RideLineDaily.class.getSimpleName();

    @Autowired
    private RideLineDailyRepository rideLineDailyRepository;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    RideLineRepository rideLineRepository;

    @Autowired
    private RedisTemplate<String, RideLineDaily> rideLineRedisTemplate;

    @Override @Transactional
    public RideLineDaily save(RideLineDaily rideLineDaily) {
        rideLineDaily = rideLineDailyRepository.save(rideLineDaily);
        rideLineRedisTemplate.opsForHash().put(KEY, rideLineDaily.getId(), rideLineDaily);
        return rideLineDaily;
    }

    @Override @Transactional
    public RideLineDaily findById(Long id) {
        RideLineDaily rideLineDaily = (RideLineDaily) rideLineRedisTemplate.opsForHash().get(KEY, id);
        if (rideLineDaily == null)
            rideLineDaily = rideLineDailyRepository.findOne(id);
        return rideLineDaily;
    }

    @Override @Transactional
    public Long deleteById(Long id) {
        rideLineDailyRepository.delete(id);
        return rideLineRedisTemplate.opsForHash().delete(KEY, id);
    }

    @Override @Transactional
    public Long disableById(Long id) {
        //TODO
        return null;
    }

    public Integer updateStatus(Long id, Long status) {
        rideLineDailyRepository.updateStatus(id, status);
        rideRepository.updateStatus(id, status);
        return 1;
    }

    public List<RideLineDaily> findAllRidesForRideLineDaily(Date date, Long agencyId, Long companyId) {
        return rideLineDailyRepository.findAllRidesForRideLineDaily(date, agencyId, companyId);
    }

    @Override
    public List<RideLineDaily> getRideLineDailyByRideLine(Long rideLineId , Long bound) {
      RideLine rideLine = rideLineRepository.findOne(rideLineId);
      List<RideLineDaily> rideLineDailyList = rideLineDailyRepository.findByRideLine(rideLine);
      List<RideLineDaily> rideLineDailies = new ArrayList<>();
      if(bound == 1){  // inbound
        for (RideLineDaily rideLineDaily : rideLineDailyList){
          if(rideLineDaily.getOutboundDriver() == null ){
            rideLineDailies.add(rideLineDaily);
          }
        }
      }else if(bound == 2){ // outbound
        for (RideLineDaily rideLineDaily : rideLineDailyList){
          if(rideLineDaily.getInboundDriver() == null ){
            rideLineDailies.add(rideLineDaily);
          }
        }
      }
      return rideLineDailies;
    }
}
