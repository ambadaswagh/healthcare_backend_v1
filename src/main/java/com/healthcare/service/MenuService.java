package com.healthcare.service;

import java.util.List;

import com.healthcare.model.entity.Menu;

public interface MenuService extends IService<Menu> {
	List<Menu> findAll();
}
