package com.healthcare.service;

import com.healthcare.model.entity.AdminAgencyCompanyOrganization;
import com.healthcare.model.entity.AdminSetting;

import java.util.List;

public interface AdminSettingService extends IService<AdminSetting>, IFinder<AdminSetting> {

	public AdminSetting getAdminSetting(Long adminId);
}
