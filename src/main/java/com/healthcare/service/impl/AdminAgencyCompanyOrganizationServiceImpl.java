package com.healthcare.service.impl;

import javax.transaction.Transactional;

import com.healthcare.model.entity.Admin;
import com.healthcare.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.AdminAgencyCompanyOrganization;
import com.healthcare.repository.AdminAgencyCompanyOrganizationRepository;
import com.healthcare.service.AdminAgencyCompanyOrganizationService;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AdminAgencyCompanyOrganizationServiceImpl extends BasicService<AdminAgencyCompanyOrganization, AdminAgencyCompanyOrganizationRepository> implements AdminAgencyCompanyOrganizationService {
	private static final String KEY = AdminAgencyCompanyOrganization.class.getSimpleName();

	private AdminAgencyCompanyOrganizationRepository adminAgencyCompanyOrganizationRepository;
	private RedisTemplate<String, AdminAgencyCompanyOrganization> redisTemplate;

	@Autowired
	private AdminRepository adminRepository;
	

	@Autowired
	public AdminAgencyCompanyOrganizationServiceImpl(
			AdminAgencyCompanyOrganizationRepository AdminAgencyCompanyOrganizationRepository, 
			RedisTemplate<String, AdminAgencyCompanyOrganization> redisTemplate) {
		this.adminAgencyCompanyOrganizationRepository = AdminAgencyCompanyOrganizationRepository;
		this.redisTemplate = redisTemplate;
	}


	
	@Override
	public AdminAgencyCompanyOrganization save(AdminAgencyCompanyOrganization entity) {
		AdminAgencyCompanyOrganization savedObject = adminAgencyCompanyOrganizationRepository.save(entity);
		redisTemplate.opsForHash().put(KEY, savedObject.getAdmin().getId(), savedObject);
		return savedObject;
	}

	@Override
	public AdminAgencyCompanyOrganization findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long deleteById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long disableById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public AdminAgencyCompanyOrganization findByAdminId(Long adminId) {
		/*Object adminAgencyCompanyOrganization = redisTemplate.opsForHash().get(KEY, adminId);
		if (adminAgencyCompanyOrganization != null) {
			return (AdminAgencyCompanyOrganization) adminAgencyCompanyOrganization;
		}*/

		return adminAgencyCompanyOrganizationRepository.findByAdminId(adminId);
	}

	@Override
	public List<AdminAgencyCompanyOrganization> findByAdminIds(Set<Long> adminIds) {
		return adminAgencyCompanyOrganizationRepository.findByAdminIds(adminIds);
	}

	@Override
	public List<Long> findAdminIdByAdminCompanyId(Long companyId) {
		return adminAgencyCompanyOrganizationRepository.findAdminIdByAdminCompanyId(companyId);
	}

	@Override
	public AdminAgencyCompanyOrganization getCompanyAndAgency(Long adminId) {
		List<AdminAgencyCompanyOrganization> adminAgencyCompanyOrganizations = adminAgencyCompanyOrganizationRepository.findByAdmin(adminId);
		if(adminAgencyCompanyOrganizations.size() > 0)
			return adminAgencyCompanyOrganizations.get(0);
		else
			return null;
	}

	@Override
	public Page<Admin> findAdminByCompanyId(Long companyId, Pageable pageable) {
		Page<Admin> adminList = adminRepository.findAdminByCompanyId(companyId, pageable);
		return adminList;
	}

}
