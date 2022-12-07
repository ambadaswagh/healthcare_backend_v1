package com.healthcare.service;

import java.util.List;

import com.healthcare.model.entity.Role;

public interface RoleService extends IService<Role> {
	Role findByLevel(long level);

	List<Role> findByStatus(long status);

	List<Role> findAll();
}
