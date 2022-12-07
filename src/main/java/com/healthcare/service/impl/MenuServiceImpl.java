package com.healthcare.service.impl;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.Menu;
import com.healthcare.model.enums.EntityStatusEnum;
import com.healthcare.repository.MenuRepository;
import com.healthcare.service.MenuService;

import io.jsonwebtoken.lang.Collections;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {
	private static final String KEY = Menu.class.getSimpleName();

	@Autowired
	MenuRepository menuRepository;

	@Autowired
	private RedisTemplate<String, Menu> menuRedisTemplate;

	@Override @Transactional
	public Menu save(Menu menu) {
		menu = menuRepository.save(menu);
		menuRedisTemplate.opsForHash().put(KEY, menu.getId(), menu);
		return menu;
	}

	@Override @Transactional
	public Long deleteById(Long id) {
		menuRepository.delete(id);
		return menuRedisTemplate.opsForHash().delete(KEY, id);
	}

	@Override @Transactional
	public Menu findById(Long id) {
		if (menuRedisTemplate.opsForHash().hasKey(KEY, id))
			return (Menu) menuRedisTemplate.opsForHash().get(KEY, id);
		return menuRepository.findOne(id);
	}

	@Override @Transactional
	public List<Menu> findAll() {
		Map<Object, Object> menuMap = menuRedisTemplate.opsForHash().entries(KEY);
		List<Menu> menuList = Collections.arrayToList(menuMap.values().toArray());
		if (menuMap.isEmpty())
			menuList = menuRepository.findAll();
		return menuList;
	}

	@Override @Transactional
	public Long disableById(Long id) {
		Menu menu = null;
		if (menuRedisTemplate.opsForHash().hasKey(KEY, id))
			menu = (Menu) menuRedisTemplate.opsForHash().get(KEY, id);
		else
			menu = menuRepository.findOne(id);
		if (menu != null && menu.getId() != null) {
			menu.setStatus(EntityStatusEnum.DISABLE.getValue());
			menuRepository.save(menu);
			menuRedisTemplate.opsForHash().put(KEY, menu.getId(), menu);
			return menu.getId();
		}
		return null;
	}
}
