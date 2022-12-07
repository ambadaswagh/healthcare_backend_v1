package com.healthcare.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.healthcare.api.auth.model.AuthRequest;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.response.Response;

public interface AdminService extends IService<Admin>, IFinder<Admin> {
	Admin getUser(String username);

	Response login(AuthRequest authenticationRequest);

	Response logout(String sessionId);

	List<Admin> findAll();

	public Page<Admin> findAll(Pageable pageable);

	public Admin addNew(Admin admin);

	public boolean isAdminExisted(String username);
	
	boolean updateTheme(String theme, long userId);

	public Page<Admin> findAllAdminByAdminIds(List<Long> adminIds, Pageable pageable);
}
