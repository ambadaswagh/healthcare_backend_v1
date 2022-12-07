package com.healthcare.service.impl;

import com.healthcare.model.entity.AdminAgencyCompanyOrganization;
import com.healthcare.model.entity.AdminSetting;
import com.healthcare.repository.AdminAgencyCompanyOrganizationRepository;
import com.healthcare.repository.AdminSettingRepository;
import com.healthcare.service.AdminAgencyCompanyOrganizationService;
import com.healthcare.service.AdminSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class AdminSettingServiceImpl extends BasicService<AdminSetting, AdminSettingRepository> implements AdminSettingService {

	@Autowired AdminSettingRepository adminSettingRepository;

	public AdminSetting getAdminSetting(Long adminId) {
		AdminSetting adminSetting = null;
		List<AdminSetting> list = adminSettingRepository.findByAdminId(adminId);
		if(list.size() > 0)
			return list.get(0);
		else
			return null;
	}


	@Override
	public AdminSetting save(AdminSetting entity) {
		return null;
	}

	@Override
	public AdminSetting findById(Long id) {
		return null;
	}

	@Override
	public Long deleteById(Long id) {
		return null;
	}

	@Override
	public Long disableById(Long id) {
		return null;
	}
}
