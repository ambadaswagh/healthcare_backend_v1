package com.healthcare.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.Company;
import com.healthcare.model.enums.EntityStatusEnum;
import com.healthcare.repository.CompanyRepository;
import com.healthcare.service.CompanyService;

import io.jsonwebtoken.lang.Collections;

@Service
@Transactional
public class CompanyServiceImpl  extends BasicService<Company, CompanyRepository>  implements CompanyService {
	private static final String KEY = Company.class.getSimpleName();

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	private RedisTemplate<String, Company> companyRedisTemplate;

	@Override @Transactional
	public Company save(Company company) {
		if(company.getId() != 0){
			company.setUpdatedAt(new Timestamp(new Date().getTime()));
    	} else {
    		company.setCreatedAt(new Timestamp(new Date().getTime()));
    		company.setUpdatedAt(new Timestamp(new Date().getTime()));
    	}
		if(company.getFederalTaxStart() != null){
			company.getFederalTaxStart().setHours(12);
			company.getFederalTaxStart().setMinutes(0);
			company.getFederalTaxStart().setSeconds(0);
		}
		if(company.getFederalTaxExpire() != null){
			company.getFederalTaxExpire().setHours(12);
			company.getFederalTaxExpire().setMinutes(0);
			company.getFederalTaxExpire().setSeconds(0);
		}
		if(company.getFederalTaxStart() 	!= null){
			company.getFederalTaxStart().setHours(12);
			company.getFederalTaxStart().setMinutes(0);
			company.getFederalTaxStart().setSeconds(0);
		}
		if(company.getFederalTaxStart() != null){
			company.getFederalTaxStart().setHours(12);
			company.getFederalTaxStart().setMinutes(0);
			company.getFederalTaxStart().setSeconds(0);
		}
		company = companyRepository.save(company);
		companyRedisTemplate.opsForHash().put(KEY, company.getId(), company);
		return company;
	}

	@Override @Transactional
	public Long deleteById(Long id) {
		companyRepository.delete(id);
		return companyRedisTemplate.opsForHash().delete(KEY, id);
	}

	@Override @Transactional
	public Company findById(Long id) {
//		if (companyRedisTemplate.opsForHash().hasKey(KEY, id))
//			return (Company) companyRedisTemplate.opsForHash().get(KEY, id);
		return companyRepository.findOne(id);
	}

	@Override @Transactional
	public List<Company> findAll() {
		Map<Object, Object> companyMap = companyRedisTemplate.opsForHash().entries(KEY);
		List<Company> companyList = Collections.arrayToList(companyMap.values().toArray());
		if (companyMap.isEmpty())
			companyList = companyRepository.findAll();
		return companyList;
	}

	@Override @Transactional
	public Long disableById(Long id) {
		Company company = null;
		if (companyRedisTemplate.opsForHash().hasKey(KEY, id))
			company = (Company) companyRedisTemplate.opsForHash().get(KEY, id);
		else
			company = companyRepository.findOne(id);
		if (company != null) {
			company.setStatus(EntityStatusEnum.DISABLE.getValue());
			companyRepository.save(company);
			companyRedisTemplate.opsForHash().put(KEY, company.getId(), company);
			return (Long)company.getId();
		}
		return null;
	}

	@Override @Transactional
	public boolean isCompanyName(String name) {
		List<Company> company = companyRepository.findCompanyName(name);
		if (company != null && company.size() > 0) {
			return true;
		}

		return false;
	}
}
