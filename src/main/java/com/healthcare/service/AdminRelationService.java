package com.healthcare.service;

import java.util.List;

import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.AdminAgencyCompanyOrganization;

public interface AdminRelationService
		extends IService<AdminAgencyCompanyOrganization>, IFinder<AdminAgencyCompanyOrganization> {
	public List<Admin> getByAgencyId(Long agencyId);

}
