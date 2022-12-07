package com.healthcare.service.impl;

import com.healthcare.model.entity.VehicleType;
import com.healthcare.repository.VehicleTypeRepository;
import com.healthcare.service.VehicleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class VehicleTypeServiceImpl extends  BasicService<VehicleType, VehicleTypeRepository> implements VehicleTypeService {
    private static final String KEY = VehicleTypeServiceImpl.class.getSimpleName();

    @Autowired
    VehicleTypeRepository vehicleTypeRepository;

    @Autowired
    private RedisTemplate<String, VehicleType> redisTemplate;

    @Override
    public List<VehicleType> findAll() {
        return vehicleTypeRepository.findAll();
    }



    @Override @Transactional
    public VehicleType save(VehicleType vehicleType) {
        VehicleType savedVehicleType = vehicleTypeRepository.save(vehicleType);
        redisTemplate.opsForHash().put(KEY, savedVehicleType.getId(), savedVehicleType);

        return savedVehicleType;
    }

    @Override @Transactional
    public Long deleteById(Long id) {
        vehicleTypeRepository.delete(id);

        return redisTemplate.opsForHash().delete(KEY, id);
    }

    @Override @Transactional
    public VehicleType findById(Long id) {
        Object vehicleType = redisTemplate.opsForHash().get(KEY, id);
        if (vehicleType != null) {
            return convertToClass(vehicleType);
        }
        return vehicleTypeRepository.findOne(id);
    }

    @Override @Transactional
    public Long disableById(Long id) {
        //TODO
        return null;
    }
    
}
