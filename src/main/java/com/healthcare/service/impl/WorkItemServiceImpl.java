package com.healthcare.service.impl;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.WorkItem;
import com.healthcare.repository.WorkItemRepository;
import com.healthcare.service.WorkItemService;

import io.jsonwebtoken.lang.Collections;


/**
 * Created by Mostafa Hamed on 30/06/17.
 * @author mhamed
 * @version 1.0
 */


@Service
@Transactional
public class WorkItemServiceImpl extends BasicService<WorkItem, WorkItemRepository>
implements WorkItemService{

	private static final String REDIS_KEY = WorkItem.class.getSimpleName();
	
	@Autowired
	WorkItemRepository workItemRepository;

	@Autowired
	RedisTemplate<String, WorkItem> workItemRedisTemplate;
	
	@Autowired
	public WorkItemServiceImpl(WorkItemRepository workItemRepository,
			RedisTemplate<String, WorkItem> workItemRedisTemplate) {
		this.workItemRepository = workItemRepository;
		this.workItemRedisTemplate = workItemRedisTemplate;
	}

	@Override @Transactional
	public WorkItem save(WorkItem workItem) {
		workItem = workItemRepository.save(workItem);
		workItemRedisTemplate.opsForHash().put(REDIS_KEY, workItem.getId(), workItem);
		return workItem;
	}

	@Override @Transactional
	public WorkItem findById(Long id) {		
		if (workItemRedisTemplate.opsForHash().hasKey(REDIS_KEY, id)){
			return (WorkItem) workItemRedisTemplate.opsForHash().get(REDIS_KEY, id);
		}
		return workItemRepository.findOne(id);
	}

	@Override @Transactional
	public Long deleteById(Long id) {
		workItemRepository.delete(id);
		return workItemRedisTemplate.opsForHash().delete(REDIS_KEY, id);
	}

	@Override @Transactional
	public List<WorkItem> findAll() {
		List<WorkItem> workItemList;
		Map<Object, Object> workItemMap = workItemRedisTemplate.opsForHash().entries(REDIS_KEY);
		if(workItemMap.isEmpty()){
			workItemList = workItemRepository.findAll();
		}else{
			workItemList = Collections.arrayToList(workItemMap.values().toArray());
		}
		return workItemList;
	}

	@Override @Transactional
	public Long disableById(Long id) {
		//TODO
		return null;
	}
}
