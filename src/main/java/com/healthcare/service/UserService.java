package com.healthcare.service;

import com.healthcare.api.auth.model.AuthRequest;
import com.healthcare.dto.SeniorMapDTO;
import com.healthcare.dto.UserSearchDTO;
import com.healthcare.model.entity.AdminAgencyCompanyOrganization;
import com.healthcare.model.entity.FoodAllergy;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.User;
import com.healthcare.model.entity.assessment.HealthStatus;
import com.healthcare.model.entity.assessment.Housing;
import com.healthcare.model.entity.assessment.Nutrition;
import com.healthcare.model.response.Response;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface UserService extends IService<User>, IFinder<User> {
	User getUser(String username);

	List<User> findByAgencyId(Long id);

	List<User> findAllSeniorsForRideLine(Long id);

	void update_note(Long id, String note);
	public List<User> findAllSeniorsWithoutRideLine(Long agencyId, Long companyId);


	List<User> findByCompanyId(Long id);

	Response login(AuthRequest authenticationRequest);

	Response logout(String sessionId);

	List<User> findAll();

	String generatePin(Long agencyId);

	boolean isPinUsed(String pin, Long id, Long userId);

	List<String> findAllPIN(Long id);

	User getUserByPIN(String pin, Long agencyId);

	List<User> getUsersByAgency(Long agencyId);

	List<User> searchUser(String searchString);

    List<User> searchUserForAdmin(String searchString, AdminAgencyCompanyOrganization adminAgencyCompanyOrganization);

	List<User> searchUserForRide(Long agencyId, Long companyId);

	User getUserByBoxNumber(String boxNumber, Long agencyId);

	boolean isBoxNumberUsed(String pin, Long id, Long userId);

	List<User> searchUser(UserSearchDTO userSearchDTO);

	public List<User> getUserHaveBirthday(DateTime today);

	public List<User> getUserHaveBirthday(String date);

	public Page<User> getUserAuthorizationEndNextDays(int days, Admin permissionAdmin, Pageable pageable);

	public Page<User> getAssessmentReminder(int days, Admin permissionAdmin, Pageable pageable);

	public List<User> getByEmail(String email);

	public Page<User> getAuthorizationBornFromDateToDate(int days, Admin permissionAdmin, Pageable pageable);

	boolean validPin(String pin, Long agencyId);

    @Transactional
    List<SeniorMapDTO> getSeniorMapLocations(Date date);

	@Transactional
	List<SeniorMapDTO> getSeniorMapLocations(Date date, Long companyId);

	@Transactional
	List<SeniorMapDTO> getSeniorMapLocations(Date date, Long companyId, Long agencyId);

	Housing save(Housing housing);

	HealthStatus save(HealthStatus healthStatus);

	Nutrition save(Nutrition nutrition);

	HealthStatus findAllHealthStatus();

	Housing findAllHousing();

	public Page<User> getUserAssessmentEndNextDays(int days, Pageable pageable);

	/*
	Nutrition findAllNutrition();

	*/

  List<FoodAllergy> findAllAllergies(Long id);
  List<FoodAllergy> saveAllAllergies(Long id, List<FoodAllergy> allergies);

	Page<User> findByCompanyAndAgency(Long companyId, Long agencyId, Pageable pageable);

	Page<User> findByCompanyPage(Long companyId, Pageable pageable);
	Page<User> findByAgencyPage(Long agencyId, Pageable pageable);

	public Page<User> getVacationSeniorReminder(int days, Admin permissionAdmin, Pageable pageable);

	List<User> searchUserByFirstName(String search);

	List<User> searchUserByFirstNameByCompany(Long companyId, String search);

	List<User> searchUserByFirstNameByCompanyAgency(Long companyId, Long agencyId, String search);

}
