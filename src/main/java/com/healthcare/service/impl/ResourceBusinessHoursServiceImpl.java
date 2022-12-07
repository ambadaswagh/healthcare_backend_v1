package com.healthcare.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.Resource;
import com.healthcare.model.entity.ResourceBusinessHours;
import com.healthcare.repository.ResourceBusinessHoursRepository;
import com.healthcare.repository.ResourceRepository;
import com.healthcare.service.ResourceBusinessHoursService;
import com.healthcare.service.ResourceService;

@Service
@Transactional
public class ResourceBusinessHoursServiceImpl extends BasicService<ResourceBusinessHours, ResourceBusinessHoursRepository> implements ResourceBusinessHoursService {
	private static final String KEY = ResourceBusinessHours.class.getSimpleName();
	@Autowired
    private ResourceBusinessHoursRepository resourceBHRepository;
	@Autowired
    private RedisTemplate<String, Resource> redisTemplate;
	
	@Override
	public ResourceBusinessHours save(ResourceBusinessHours entity) {
		if(entity.getId() == 0){
			entity.setCreatedAt(new Timestamp(new Date().getTime()));
			entity.setUpdatedAt(new Timestamp(new Date().getTime()));
		} else {
			entity.setUpdatedAt(new Timestamp(new Date().getTime()));
			
		}
		
		entity = resourceBHRepository.save(entity);
		
        redisTemplate.opsForHash().put(KEY, entity.getId(), entity);
        return entity;
	}

	@Override
	public ResourceBusinessHours findById(Long id) {
		if (redisTemplate.opsForHash().hasKey(KEY, id)) {
            return (ResourceBusinessHours) redisTemplate.opsForHash().get(KEY, id);
        }
        return resourceBHRepository.findOne(id);
	}

	@Override
	public Long deleteById(Long id) {
		resourceBHRepository.delete(id);
        return redisTemplate.opsForHash().delete(KEY, id);
	}

	@Override
	public Long disableById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ResourceBusinessHours> findByResourceId(Long resourceId) {
		return resourceBHRepository.findByResourceId(resourceId);
	}

	@Override
	public void deleteByResourceId(Long resourceId) {
		resourceBHRepository.deleteByResourceId(resourceId);
	}

}
