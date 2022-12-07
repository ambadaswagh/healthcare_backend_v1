package com.healthcare.service;

import java.util.List;

import com.healthcare.model.entity.Company;

public interface CompanyService extends IService<Company>, IFinder<Company> {
	List<Company> findAll();
	boolean isCompanyName(String name);
}
