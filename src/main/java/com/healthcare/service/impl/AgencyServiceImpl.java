package com.healthcare.service.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.healthcare.dto.AgencyStatsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.dto.UserDto;
import com.healthcare.model.entity.Agency;
import com.healthcare.model.entity.User;
import com.healthcare.model.entity.Visit;
import com.healthcare.model.enums.*;
import com.healthcare.repository.AgencyRepository;
import com.healthcare.service.AgencyService;
import com.healthcare.service.UserService;
import com.healthcare.service.VisitService;
import com.healthcare.util.DateUtils;

import io.jsonwebtoken.lang.Collections;

@Service
@Transactional
public class AgencyServiceImpl extends BasicService<Agency, AgencyRepository> implements AgencyService {
	private static final String KEY = Agency.class.getSimpleName();

	@Autowired
	AgencyRepository agencyRepository;

	@Autowired
	UserService userService;

	@Autowired
	VisitService visitService;

	@Autowired
	private RedisTemplate<String, Agency> agencyRedisTemplate;

	@Override
	@Transactional
	public Agency save(Agency agency) {
		if(agency.getId() != null){
			agency.setUpdatedAt(new Timestamp(new Date().getTime()));
    	} else {
    		agency.setCreatedAt(new Timestamp(new Date().getTime()));
    		agency.setUpdatedAt(new Timestamp(new Date().getTime()));
    	}
		agency = agencyRepository.save(agency);
		agencyRedisTemplate.opsForHash().put(KEY, agency.getId(), agency);
		return agency;
	}

	@Override
	@Transactional
	public Long deleteById(Long id) {
		agencyRepository.delete(id);
		return agencyRedisTemplate.opsForHash().delete(KEY, id);
	}

	@Override
	@Transactional
	public Agency findById(Long id) {
		return agencyRepository.findOne(id);
	}

	@Override
	@Transactional
	public List<Agency> findAll() {
		Map<Object, Object> agencyMap = agencyRedisTemplate.opsForHash().entries(KEY);
		List<Agency> agencyList = Collections.arrayToList(agencyMap.values().toArray());
		if (agencyMap.isEmpty())
			agencyList = agencyRepository.findAll();
		return agencyList;
	}

	@Override
	@Transactional
	public List<Agency> findAllValid(long companyId) {
		List<Agency> agencyList = agencyRepository.findAll();

		return agencyList.stream().filter(agency -> agency.getCompany().getId() == companyId)
				.collect(Collectors.toList());
	}

	@Override
	public Object computeAgencyStats(Long agencyId) {
		if (agencyId == null)
			return null;
		Object object=  agencyRepository.generateAgencyStat(agencyId).get(0);
		return object;
	}

	@Deprecated
	@Override
	@Transactional
	public UserDto generateUserStats(Long agencyId) {
		UserDto userDto = new UserDto();
		long totalRegisteredSeniors = 0;
		long totalActiveSeniorsAmongCheckedIn = 0;
		long totalCheckedInSeniors = 0;
		// Redis
		List<User> users = userService.findAll();
		List<Visit> visits = visitService.findAll();

		// count all users by agencyId
		if (users != null && !users.isEmpty()) {
			totalRegisteredSeniors = users.stream().filter(user -> agencyId.equals(user.getAgency().getId())).count();
		}

		// count checkin users
		if (visits != null && !visits.isEmpty()) {
			Calendar cal = Calendar.getInstance();
			totalCheckedInSeniors = visits.stream().filter(
					visit -> agencyId.equals(visit.getAgency().getId()) && DateUtils.isAgivenDateBetweenInterval(cal,
							DateUtils.dateToCalendar(DateUtils.timestampToDate(visit.getCheckInTime())), DateUtils
									.dateToCalendar(DateUtils.timestampToDate(visit.getCheckOutTime()))))
					.count();

			totalActiveSeniorsAmongCheckedIn = visits.stream()
					.filter(visit -> agencyId.equals(visit.getAgency().getId())
							&& DateUtils.isAgivenDateBetweenInterval(cal,
									DateUtils.dateToCalendar(DateUtils.timestampToDate(visit.getCheckInTime())),
									DateUtils.dateToCalendar(DateUtils.timestampToDate(visit.getCheckOutTime())))
							&& visit.getUser().getStatus() == StatusEnum.ACTIVE)
					.count();
		}

		userDto.setTotalRegisteredSeniors(totalRegisteredSeniors);
		userDto.setTotalCheckedInSeniors(totalCheckedInSeniors);
		userDto.setTotalActiveSeniorsAmongCheckedIn(totalActiveSeniorsAmongCheckedIn);
		if (users == null || users.isEmpty())
			userDto = agencyRepository.generateUserStats(agencyId);
		return userDto;
	}

	@Override
	@Transactional
	public Long disableById(Long id) {
		Agency agency = null;
		if (agencyRedisTemplate.opsForHash().hasKey(KEY, id))
			agency = (Agency) agencyRedisTemplate.opsForHash().get(KEY, id);
		else
			agency = agencyRepository.findOne(id);
		if (agency != null) {
			agency.setStatus(EntityStatusEnum.DISABLE.getValue());
			agencyRepository.save(agency);
			agencyRedisTemplate.opsForHash().put(KEY, agency.getId(), agency);
			return agency.getId();
		}
		return null;
	}

	@Override
	public List<Agency> findByCompany(Long companyId) {
		return agencyRepository.findByCompanyId(companyId);
	}

	@Override
	public boolean isAgencyName(String name) {
		List<Agency> agencies = agencyRepository.findAgencyName(name);
		if (agencies != null && agencies.size() > 0) {
			return true;
		}

		return false;
	}

	@Override
	public Page<Agency> findByCompanyPage(Long companyId, Pageable pageable) {
		return agencyRepository.findByCompanyPage(companyId, pageable);
	}

	@Override
	public void updateMealSelected(Long agencyId, String mealSelected) {
		agencyRepository.updateMealSelected(agencyId, mealSelected);
	}

    @Override
    public Agency updateAgreementSignatureId(Long id, Long fileUploadId) {
        Agency agency  = agencyRepository.findOne(id);
        if(agency == null) return null;
        agency.setAgreementSignatureId(fileUploadId);
        return agency;
    }

    @Override
	public void updateLogoSetting(Long agencyId, Long logoId, String mainWhiteLabel, String upperLeftLabel) {
		agencyRepository.updateLogoSetting(agencyId, logoId, mainWhiteLabel, upperLeftLabel);
	}
}
