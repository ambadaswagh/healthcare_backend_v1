package com.healthcare.service.impl;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.AdminPost;
import com.healthcare.repository.AdminPostRepository;
import com.healthcare.service.AdminPostService;

import io.jsonwebtoken.lang.Collections;

@Service
@Transactional
public class AdminPostServiceImpl extends BasicService<AdminPost, AdminPostRepository> implements AdminPostService {
	private static final String KEY = AdminPost.class.getSimpleName();

	@Autowired
	AdminPostRepository adminPostRepository;

	@Autowired
	private RedisTemplate<String, AdminPost> adminPostRedisTemplate;

	@Override @Transactional
	public AdminPost save(AdminPost adminPost) {
		adminPost = adminPostRepository.save(adminPost);
		adminPostRedisTemplate.opsForHash().put(KEY, adminPost.getId(), adminPost);
		return adminPost;
	}

	@Override @Transactional
	public Long deleteById(Long id) {
		adminPostRepository.delete(id);
		return adminPostRedisTemplate.opsForHash().delete(KEY, id);
	}

	@Override @Transactional
	public AdminPost findById(Long id) {
		if (adminPostRedisTemplate.opsForHash().hasKey(KEY, id)) {
			return (AdminPost) adminPostRedisTemplate.opsForHash().get(KEY, id);
		}
		return adminPostRepository.findOne(id);
	}

	@Override @Transactional
	public List<AdminPost> findAll() {
		Map<Object, Object> adminPostMap = adminPostRedisTemplate.opsForHash().entries(KEY);
		List<AdminPost> adminPostList = Collections.arrayToList(adminPostMap.values().toArray());
		if (adminPostMap.isEmpty())
			adminPostList = adminPostRepository.findAll();
		return adminPostList;
	}

	@Override @Transactional
	public Long disableById(Long id) {
		//TODO
		return null;
	}
}
