package com.healthcare.service;

import java.util.List;

import com.healthcare.model.entity.InsuranceBrokerCompany;

public interface InsuranceBrokerCompanyService extends IService<InsuranceBrokerCompany> {

	List<InsuranceBrokerCompany> findAll();

}
