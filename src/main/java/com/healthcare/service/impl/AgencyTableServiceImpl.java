package com.healthcare.service.impl;

import com.healthcare.model.entity.AgencyTable;
import com.healthcare.repository.AgencyTableRepository;
import com.healthcare.service.AgencyTableService;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AgencyTableServiceImpl implements AgencyTableService {
    private static final String KEY = AgencyTableServiceImpl.class.getSimpleName();

    @Autowired
    AgencyTableRepository repository;

    @Autowired
    private RedisTemplate<String, AgencyTable> agencyTableRedisTemplate;

    @Override
    @Transactional
    public AgencyTable save(AgencyTable agencyTable) {
        agencyTable = repository.save(agencyTable);
        agencyTableRedisTemplate.opsForHash().put(KEY, agencyTable.getId(), agencyTable);
        return agencyTable;
    }

    @Override
    public AgencyTable findById(Long id) {
        if (agencyTableRedisTemplate.opsForHash().hasKey(KEY, id)) {
            return (AgencyTable) agencyTableRedisTemplate.opsForHash().get(KEY, id);
        }
        return repository.findOne(id);
    }

    @Override
    public Long deleteById(Long id) {
        repository.delete(id);
        return agencyTableRedisTemplate.opsForHash().delete(KEY, id);
    }

    @Override
    public Long disableById(Long id) {
        return null;
    }

    @Override
    @Transactional
    public List<AgencyTable> findAll() {
        Map<Object, Object> agencyTableMap = agencyTableRedisTemplate.opsForHash().entries(KEY);
        List<AgencyTable> agencyTableList = Collections.arrayToList(agencyTableMap.values().toArray());
        if (agencyTableMap.isEmpty()) {
            agencyTableList = repository.findAll();
        }
        return agencyTableList;
    }
}