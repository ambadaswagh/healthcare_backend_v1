package com.healthcare.repository;

import com.healthcare.model.entity.AdminAgencyCompanyOrganization;
import com.healthcare.model.entity.AdminSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminSettingRepository extends JpaRepository<AdminSetting, Long>,
		JpaSpecificationExecutor<AdminSetting> {

	@Query("select a from AdminSetting a where a.adminId = ?1")
	public List<AdminSetting> findByAdminId(Long adminId);

	@Query("select max(a.autoCheckoutTime) from AdminSetting a")
	public String findMaxAutoCheckoutTime();

	@Query(value = "select aa.agency_id, s.auto_check_out_time from admin_setting s " +
			"inner join admin_agency_company_organization aa on aa.admin_id = s.admin_id where s.auto_check_out_time is not null and aa.agency_id is not null", nativeQuery = true)
	List<Object[]> findAllSettingGroupAgency();
}