package com.healthcare.service;

import com.healthcare.dto.UserDto;
import com.healthcare.model.entity.Agency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AgencyService extends IService<Agency>, IFinder<Agency> {
	List<Agency> findAll();
	UserDto generateUserStats(Long agencyId);
	List<Agency> findAllValid(long companyId);
	Object computeAgencyStats(Long agencyId);
	List<Agency> findByCompany(Long companyId);
	boolean isAgencyName(String name);
	Page<Agency> findByCompanyPage(Long companyId, Pageable pageable);
	void updateMealSelected(Long agencyId, String mealSelected);
	Agency updateAgreementSignatureId(Long id, Long fileUploadId);
	void updateLogoSetting(Long agencyId, Long logoId, String mainWhiteLabel, String upperLeftLabel);
}
