package com.healthcare.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.Resource;
import com.healthcare.model.entity.ResourceBusinessHours;
import com.healthcare.repository.ResourceRepository;
import com.healthcare.service.ResourceBusinessHoursService;
import com.healthcare.service.ResourceService;

import io.jsonwebtoken.lang.Collections;

@Service
@Transactional
public class ResourceServiceImpl extends BasicService<Resource, ResourceRepository> implements ResourceService {
	private static final String KEY = Resource.class.getSimpleName();
	@Autowired
    private ResourceRepository resourceRepository;
	@Autowired
    private RedisTemplate<String, Resource> redisTemplate;
	@Autowired
	private ResourceBusinessHoursService resource_service;
	@Override
	public Resource save(Resource entity) {
		Set<ResourceBusinessHours> set_rbh = new HashSet<>(entity.getBusiness_hours());
		try {
			if(entity.getId() != 0){
				resource_service.deleteByResourceId(entity.getId());
				entity.setUpdatedAt(new Timestamp(new Date().getTime()));
			} else {
				entity.setCreatedAt(new Timestamp(new Date().getTime()));
				entity.setUpdatedAt(new Timestamp(new Date().getTime()));
			}
			entity = resourceRepository.save(entity);
			for(ResourceBusinessHours rbh : set_rbh){
				rbh.setResource_id(entity.getId());
				rbh = resource_service.save(rbh);
			}
			redisTemplate.opsForHash().put(KEY, entity.getId(), entity);
			return entity;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Resource findById(Long id) {
		Resource res = resourceRepository.findOne(id);
		res.setBusiness_hours(new HashSet<>(resource_service.findByResourceId(res.getId())));
		return res;
	}

	@Override
	public Long deleteById(Long id) {
		resourceRepository.delete(id);
        return redisTemplate.opsForHash().delete(KEY, id);
	}

	@Override
	public Long disableById(Long id) {
        return null;
	}

	@Override
	public List<Resource> findAll() {
		List<Resource> roleList =  resourceRepository.findAll();
		for(Resource resource : roleList){
			resource.setBusiness_hours(new HashSet<>(resource_service.findByResourceId(resource.getId())));
		}
		return roleList;
	}

}
