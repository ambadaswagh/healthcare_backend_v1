package com.healthcare.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.AdminAgencyCompanyOrganization;
import com.healthcare.repository.AdminAgencyCompanyOrganizationRepository;
import com.healthcare.service.AdminRelationService;

@Service
@Transactional
public class AdminRelationServiceImpl
		extends BasicService<AdminAgencyCompanyOrganization, AdminAgencyCompanyOrganizationRepository>
		implements AdminRelationService {

	@Autowired
	private AdminAgencyCompanyOrganizationRepository relationRepository;

	@Override
	public AdminAgencyCompanyOrganization save(AdminAgencyCompanyOrganization entity) {
		// TODO Auto-generated method stub
		return null;
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
	public List<Admin> getByAgencyId(Long agencyId) {
		List<Admin> admins = new ArrayList<Admin>();
		List<AdminAgencyCompanyOrganization> adminRelations = relationRepository.getAgencyAdmin(agencyId);
		if (admins != null && adminRelations.size() > 0) {
			adminRelations.forEach(relation -> admins.add(relation.getAdmin()));
		}
		return admins;
	}

}
