package com.healthcare.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.Organization;
import com.healthcare.model.enums.EntityStatusEnum;
import com.healthcare.repository.OrganizationRepository;
import com.healthcare.service.OrganizationService;

@Service
@Transactional
public class OrganizationServiceImpl extends BasicService<Organization, OrganizationRepository>
		implements OrganizationService {
	private static final String KEY = Organization.class.getSimpleName();

	@Autowired
	private OrganizationRepository organizationRepository;
	
	@Autowired
	private RedisTemplate<String, Organization> organizationRedisTemplate;

	@Override @Transactional
	public Organization save(Organization organization) {
		if(organization.getId() != null){
			organization.setUpdatedAt(new Timestamp(new Date().getTime()));
	  	} else {
	  		organization.setCreatedAt(new Timestamp(new Date().getTime()));
	  		organization.setUpdatedAt(new Timestamp(new Date().getTime()));
	  	}
		if(organization.getStateTaxStart() != null){
			organization.getStateTaxStart().setHours(12);
			organization.getStateTaxStart().setMinutes(0);
			organization.getStateTaxStart().setSeconds(0);
    	}
		if(organization.getStateTaxExpire() != null){
			organization.getStateTaxExpire().setHours(12);
			organization.getStateTaxExpire().setMinutes(0);
			organization.getStateTaxExpire().setSeconds(0);
    	}
		if(organization.getFederalTaxStart() != null){
			organization.getFederalTaxStart().setHours(12);
			organization.getFederalTaxStart().setMinutes(0);
			organization.getFederalTaxStart().setSeconds(0);
    	}
		if(organization.getFederalTaxExpire() != null){
			organization.getFederalTaxExpire().setHours(12);
			organization.getFederalTaxExpire().setMinutes(0);
			organization.getFederalTaxExpire().setSeconds(0);
    	}
		organization=organizationRepository.save(organization);
		organizationRedisTemplate.opsForHash().put(KEY, organization.getId(), organization);
		return organization;
	}

	@Override @Transactional
	public Organization findById(Long id) {
		return organizationRepository.findOne(id);
	}

	@Override @Transactional
	public Long deleteById(Long id) {
		organizationRepository.delete(id);
		return organizationRedisTemplate.opsForHash().delete(KEY, id);
	}

	@Override @Transactional
	public Long disableById(Long id) {
		Organization organization=null;
		if(organizationRedisTemplate.opsForHash().hasKey(KEY, id)) {
			organization=(Organization)organizationRedisTemplate.opsForHash().get(KEY, id);
		}
		else {
			organization=organizationRepository.findOne(id);
		}
		
		if(organization!=null) {
			organization.setStatus(EntityStatusEnum.DISABLE.getValue());
			organizationRepository.save(organization);
			organizationRedisTemplate.opsForHash().put(KEY, organization.getId(), organization);
			return organization.getId();
		}
		return null;
	}

	@Override
	public List<Organization> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Page<Organization> findAll(Organization entity, Pageable pageable){
		pageable = checkPageable(pageable);
		return organizationRepository.findAllOrganizationPageable(pageable);
	}

	@Override
	public Page<Organization> findAllByAgency(Long agencyId, Pageable pageable) {
		pageable = checkPageable(pageable);
		return organizationRepository.findAllByAgency(agencyId, pageable);
	}

	@Override
	public List<Organization> findAllByAgencyList(Long agencyId) {
		return organizationRepository.findAllByAgencyList(agencyId);
	}

	@Override
	public Page<Organization> findAllByAgencies(List<Long> agencies, Pageable pageable) {
		pageable = checkPageable(pageable);
		return organizationRepository.findAllByAgencies(agencies, pageable);
	}

	public List<Organization> findAllByAgenciesList(List<Long> agencies) {
		return organizationRepository.findAllByAgenciesList(agencies);
	}

}
