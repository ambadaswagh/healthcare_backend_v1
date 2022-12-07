package com.healthcare.service;

import java.util.List;

import com.healthcare.model.entity.Training;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TrainingService extends IService<Training>, IFinder<Training> {
	Training save(Training training);

	List<Training> findAll();
	
	List<Training> findByTraineeId(String id);

	Page<Training> findAllByAgency(Long agencyId, Pageable pageable);

	Page<Training> findAllByAgencies(List<Long> agencies, Pageable pageable);
}
