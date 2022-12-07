package com.healthcare.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthcare.model.entity.AdminAgencyCompanyOrganization;

@Repository
public interface AdminAgencyCompanyOrganizationRepository extends JpaRepository<AdminAgencyCompanyOrganization, Long>,
		JpaSpecificationExecutor<AdminAgencyCompanyOrganization> {

	@Query("select a from AdminAgencyCompanyOrganization a where a.agency.id = ?1 ")
	public List<AdminAgencyCompanyOrganization> getAgencyAdmin(Long agencyId);
	
	@Query("select a from AdminAgencyCompanyOrganization a where a.admin.id = ?1")
	public AdminAgencyCompanyOrganization findByAdminId(Long adminId);

	@Query("select a.admin.id from AdminAgencyCompanyOrganization a where a.company.id = ?1 and a.agency is not null")
	public List<Long> findAdminIdByAdminCompanyId(Long companyId);

	@Query("select a from AdminAgencyCompanyOrganization a where a.admin.id = ?1")
	public List<AdminAgencyCompanyOrganization> findByAdmin(Long adminId);

	@Query("select a from AdminAgencyCompanyOrganization a where a.admin.id in ?1")
    public List<AdminAgencyCompanyOrganization> findByAdminIds(Set<Long> adminIds);
}