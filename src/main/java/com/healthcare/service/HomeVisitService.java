package com.healthcare.service;

import java.util.List;

import com.healthcare.model.entity.HomeVisit;

/**
 * Created by Hitesh on 06/24/17.
 */
public interface HomeVisitService extends IService<HomeVisit>, IFinder<HomeVisit> {
	List<HomeVisit> findAll();
}
