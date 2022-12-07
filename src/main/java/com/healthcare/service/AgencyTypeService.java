package com.healthcare.service;

import java.util.List;

import com.healthcare.model.entity.AgencyType;

public interface AgencyTypeService extends IService<AgencyType>, IFinder<AgencyType> {
	List<AgencyType> findAll();
}
