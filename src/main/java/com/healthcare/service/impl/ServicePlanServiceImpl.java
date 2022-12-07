package com.healthcare.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.healthcare.dto.BasicReportFilterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.ServicePlan;
import com.healthcare.model.enums.DayEnum;
import com.healthcare.repository.ServicePlanRepository;
import com.healthcare.service.ServicePlanService;
import com.healthcare.util.DateUtils;
import com.healthcare.util.EnumUtils;

import io.jsonwebtoken.lang.Collections;

@Service
@Transactional
public class ServicePlanServiceImpl extends BasicService<ServicePlan, ServicePlanRepository>
implements ServicePlanService {
	

	private static final String KEY = ServicePlan.class.getSimpleName();
	private static final String DAYS_DELIMITER = ",";

	@Autowired
	ServicePlanRepository servicePlanRepository;

	@Autowired
	private RedisTemplate<String, ServicePlan> servicePlanRedisTemplate;

	@Override @Transactional
	public ServicePlan save(ServicePlan servicePlan) {
		if (servicePlan == null) {
			return null;
		}

		// validate days
		/*for (String day : servicePlan.getDays().split(DAYS_DELIMITER)) {
			if (!EnumUtils.isInEnum(day, DayEnum.class))
				return null;
		}*/

		// validate plan period
		/*if (servicePlan.getPlanStart() != null && servicePlan.getPlanEnd() != null) {
			if (servicePlan.getPlanEnd().before(servicePlan.getPlanStart()))
				return null;
		}*/

		servicePlan = servicePlanRepository.save(servicePlan);
		servicePlanRedisTemplate.opsForHash().put(KEY, servicePlan.getId(), servicePlan);

		return servicePlan;
	}

	@Override @Transactional
	public Long deleteById(Long id) {
		servicePlanRepository.delete(id);
		return servicePlanRedisTemplate.opsForHash().delete(KEY, id);
	}

	@Override @Transactional
	public ServicePlan findById(Long id) {
		if (servicePlanRedisTemplate.opsForHash().hasKey(KEY, id))
			return (ServicePlan) servicePlanRedisTemplate.opsForHash().get(KEY, id);
		return servicePlanRepository.findOne(id);
	}

	@Override @Transactional
	public List<ServicePlan> findAll() {
		Map<Object, Object> servicePlanMap = servicePlanRedisTemplate.opsForHash().entries(KEY);
		List<ServicePlan> servicePlanList = Collections.arrayToList(servicePlanMap.values().toArray());
		if (servicePlanMap.isEmpty())
			servicePlanList = servicePlanRepository.findAll();
		return servicePlanList;
	}
	
	/**
	 * generate service calendar (Home visit)
	 * @param Long serviceplanId
	 * @return List<HomeVisitDto>
	 */
	@Override @Transactional
	public List<Date> serviceCalendarGeneration(Long serviceplanId) {			
		ServicePlan servicePlan = findById(serviceplanId);
		if(servicePlan != null)
			//get all schudled dates between start and end  service plan 
			return DateUtils.getDaysBetweenDates(servicePlan.getPlanStart(),
					servicePlan.getPlanEnd(), servicePlan.getDays());

		return new ArrayList<>();
	}
	
	@Override @Transactional
	public List<String> getServiceCalendar(Long servicePlanId) {
		ServicePlan servicePlan = findById(servicePlanId);
		if (servicePlan != null)
			return servicePlanRepository.getServiceCalendar(servicePlan);
		return new ArrayList<String>();
	}


	@Override @Transactional
	public List<ServicePlan> getServicePlanByUser(BasicReportFilterDTO brf) {
        return   servicePlanRepository.findAllServicePlanForUser(brf.getStartDate(),brf.getEndDate(),brf.getUserId());
	}

	@Override @Transactional
	public Long disableById(Long id) {
		//TODO
		return null;
	}

	@Override
	public List<ServicePlan> getServicePlanByUserId(Long userId) {
		return servicePlanRepository.findServicePlanByUserId(userId);
	}
}
