package com.healthcare.service.impl;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.Role;
import com.healthcare.model.enums.EntityStatusEnum;
import com.healthcare.repository.RoleRepository;
import com.healthcare.service.RoleService;

import io.jsonwebtoken.lang.Collections;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
	private static final String KEY = Role.class.getSimpleName();

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	private RedisTemplate<String, Role> roleRedisTemplate;

	@Override @Transactional
	public Role save(Role role) {
		role = roleRepository.save(role);
		roleRedisTemplate.opsForHash().put(KEY, role.getId(), role);
		return role;
	}

	@Override @Transactional
	public Long deleteById(Long id) {
		roleRepository.delete(id);
		return roleRedisTemplate.opsForHash().delete(KEY, id);
	}

	@Override @Transactional
	public Role findById(Long id) {
		if (roleRedisTemplate.opsForHash().hasKey(KEY, id))
			return (Role) roleRedisTemplate.opsForHash().get(KEY, id);
		return roleRepository.findOne(id);
	}

	@Override @Transactional
	public Role findByLevel(long level) {
		return roleRepository.findByLevel(level);
	}

	@Override @Transactional
	public List<Role> findByStatus(long status) {
		return roleRepository.findByStatus(status);
	}

	@Override @Transactional
	public List<Role> findAll() {
		Map<Object, Object> roleMap = roleRedisTemplate.opsForHash().entries(KEY);
		List<Role> roleList = Collections.arrayToList(roleMap.values().toArray());
		if (roleMap.isEmpty())
			roleList = roleRepository.findAll();
		return roleList;
	}

	@Override @Transactional
	public Long disableById(Long id) {
		Role role = null;
		if (roleRedisTemplate.opsForHash().hasKey(KEY, id))
			role = (Role) roleRedisTemplate.opsForHash().get(KEY, id);
		else
			role = roleRepository.findOne(id);
		if (role != null && role.getId() != null) {
			role.setStatus(EntityStatusEnum.DISABLE.getValue());
			roleRepository.save(role);
			roleRedisTemplate.opsForHash().put(KEY, role.getId(), role);
			return role.getId();
		}
		return null;
	}
}
