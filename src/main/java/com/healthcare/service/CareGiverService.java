package com.healthcare.service;

import java.util.List;

import com.healthcare.model.entity.CareGiver;

/**
 * Created by Hitesh on 06/24/17.
 */
public interface CareGiverService extends IService<CareGiver>, IFinder<CareGiver> {

	CareGiver save(CareGiver careGiver);

	List<CareGiver> findAll();
}
