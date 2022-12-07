package com.healthcare.service;

import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.AdminAgencyCompanyOrganization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface AdminAgencyCompanyOrganizationService extends IService<AdminAgencyCompanyOrganization>, IFinder<AdminAgencyCompanyOrganization> {

	public AdminAgencyCompanyOrganization findByAdminId(Long adminId);

	public List<Long> findAdminIdByAdminCompanyId(Long companyId);

	public AdminAgencyCompanyOrganization getCompanyAndAgency(Long adminId);

	public List<AdminAgencyCompanyOrganization> findByAdminIds(Set<Long> adminIds);

	public Page<Admin> findAdminByCompanyId(Long companyId, Pageable pageable);
}
