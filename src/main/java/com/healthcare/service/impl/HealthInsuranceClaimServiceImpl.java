package com.healthcare.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.api.common.HealthcareUtil;
import com.healthcare.dto.BasicReportFilterDTO;
import com.healthcare.model.entity.HealthInsuranceClaim;
import com.healthcare.model.enums.TripBillingStatusEnum;
import com.healthcare.repository.HealthInsuranceClaimRepository;
import com.healthcare.service.HealthInsuranceClaimService;

import io.jsonwebtoken.lang.Collections;

@Service
@Transactional
public class HealthInsuranceClaimServiceImpl  extends BasicService<HealthInsuranceClaim, HealthInsuranceClaimRepository>
implements HealthInsuranceClaimService {
	private static final String KEY = HealthInsuranceClaim.class.getSimpleName();

	@Autowired
	HealthInsuranceClaimRepository healthInsuranceClaimRepository;

	@Autowired
	private RedisTemplate<String, HealthInsuranceClaim> healthInsuranceClaimRedisTemplate;

	@Override @Transactional
	public HealthInsuranceClaim save(HealthInsuranceClaim healthInsuranceClaim) {
		healthInsuranceClaim = healthInsuranceClaimRepository.save(healthInsuranceClaim);
		healthInsuranceClaimRedisTemplate.opsForHash().put(KEY, healthInsuranceClaim.getId(), healthInsuranceClaim);
		return healthInsuranceClaim;
	}

	@Override @Transactional
	public HealthInsuranceClaim findById(Long id) {
		HealthInsuranceClaim healthInsuranceClaim = null;
		Object obj = healthInsuranceClaimRedisTemplate.opsForHash().get(KEY, id);
		if (obj != null && obj instanceof HealthInsuranceClaim) {
			healthInsuranceClaim = (HealthInsuranceClaim) healthInsuranceClaimRedisTemplate.opsForHash().get(KEY, id);
		}
		if (healthInsuranceClaim == null)
			healthInsuranceClaim = healthInsuranceClaimRepository.findOne(id);
		return healthInsuranceClaim;
	}

	@Override @Transactional
	public Long deleteById(Long id) {
		healthInsuranceClaimRepository.delete(id);
		return healthInsuranceClaimRedisTemplate.opsForHash().delete(KEY, id);
	}

	@Override @Transactional
	public List<HealthInsuranceClaim> findAll() {
		Map<Object, Object> healthInsuranceClaimMap = healthInsuranceClaimRedisTemplate.opsForHash().entries(KEY);
		List<HealthInsuranceClaim> healthInsuranceClaimList = Collections.arrayToList(healthInsuranceClaimMap.values().toArray());
		if (healthInsuranceClaimMap.isEmpty())
			healthInsuranceClaimList = healthInsuranceClaimRepository.findAll();
		return healthInsuranceClaimList;
	}
	
	@Override @Transactional
	public List<HealthInsuranceClaim> findByUserId(Long userId) {
		List<HealthInsuranceClaim> returnHealthInsuranceClaimList = null;
		Map<Object, Object> healthInsuranceClaimMap = healthInsuranceClaimRedisTemplate.opsForHash().entries(KEY);
		List<HealthInsuranceClaim> healthInsuranceClaimList = Collections.arrayToList(healthInsuranceClaimMap.values().toArray());
		if (healthInsuranceClaimMap.isEmpty()){
			returnHealthInsuranceClaimList = new ArrayList<HealthInsuranceClaim>();
			for(HealthInsuranceClaim healthInsuranceClaim : healthInsuranceClaimList){
				if(healthInsuranceClaim.getUser()!=null && userId == healthInsuranceClaim.getUser().getId()){
					returnHealthInsuranceClaimList.add(healthInsuranceClaim);
				}
			}
		}else{
			returnHealthInsuranceClaimList = healthInsuranceClaimRepository.findByUserId(userId);
		}
		
		return returnHealthInsuranceClaimList;
	}

    @Override @Transactional
	public Long disableById(Long id) {
        //TODO
        return null;
    }

	public List<HealthInsuranceClaim> findHealthInsuranceReport( BasicReportFilterDTO basicFilter) {
		HealthcareUtil.SET_DATE_RANGE_IF_NOT_PROVIDED(basicFilter);

		if(HealthcareUtil.isNull(basicFilter.getUserId())){
			return healthInsuranceClaimRepository.findHealthClaimReport(basicFilter.getStartDate(), basicFilter.getEndDate(),
					basicFilter.getCompanyId(),basicFilter.getAgencyId());
		}else{
			return healthInsuranceClaimRepository.findHealthClaimReportForUser(basicFilter.getUserId(),basicFilter.getStartDate(),
					basicFilter.getEndDate(),basicFilter.getCompanyId(),basicFilter.getAgencyId());
		}
	}

    @Override
    public List<HealthInsuranceClaim> findTripStatusForBilling(List<HealthInsuranceClaim> healthInsuranceClaimLst) {
        healthInsuranceClaimLst.forEach(healthInsuranceClaim -> {
            healthInsuranceClaim.setTripStatus(TripBillingStatusEnum.VALID.toString());
        });
        return null;
    }
}
