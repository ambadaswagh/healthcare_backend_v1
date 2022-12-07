package com.healthcare.service;

import java.util.List;

import com.healthcare.model.entity.Resource;

public interface ResourceService extends IService<Resource>, IFinder<Resource> {
	List<Resource> findAll();
	
}
