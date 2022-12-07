package com.healthcare.service.impl;

import com.healthcare.model.entity.VehicleBrand;
import com.healthcare.repository.VehicleBrandRepository;
import com.healthcare.service.VehicleBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class VehicleBrandServiceImpl extends  BasicService<VehicleBrand, VehicleBrandRepository> implements VehicleBrandService {
    private static final String KEY = VehicleBrandServiceImpl.class.getSimpleName();

    @Autowired
    VehicleBrandRepository vehicleBrandRepository;

    @Autowired
    private RedisTemplate<String, VehicleBrand> redisTemplate;

    @Override
    public List<VehicleBrand> findAll() {
        return vehicleBrandRepository.findAll();
    }



    @Override @Transactional
    public VehicleBrand save(VehicleBrand vehicleBrand) {
        if (vehicleBrand.getId() == null) {
            vehicleBrand.setCreatedAt(new Timestamp(new Date().getTime()));
        } else {
            vehicleBrand.setUpdatedAt(new Timestamp(new Date().getTime()));
        }
        VehicleBrand savedVehicleBrand = vehicleBrandRepository.save(vehicleBrand);
        redisTemplate.opsForHash().put(KEY, savedVehicleBrand.getId(), savedVehicleBrand);

        return savedVehicleBrand;
    }

    @Override @Transactional
    public Long deleteById(Long id) {
        vehicleBrandRepository.delete(id);

        return redisTemplate.opsForHash().delete(KEY, id);
    }

    @Override @Transactional
    public VehicleBrand findById(Long id) {
        Object vehicleBrand = redisTemplate.opsForHash().get(KEY, id);
        if (vehicleBrand != null) {
            return convertToClass(vehicleBrand);
        }
        return vehicleBrandRepository.findOne(id);
    }

    @Override @Transactional
    public Long disableById(Long id) {
        //TODO
        return null;
    }
    
}
