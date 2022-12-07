package com.healthcare.service;

import java.util.List;

import com.healthcare.dto.BasicReportFilterDTO;
import com.healthcare.dto.VisitDTO;
import com.healthcare.model.entity.Review;

public interface ReviewService extends IService<Review>, IFinder<Review> {
	List<Review> findAll();
	List<Review> findByUser(Long userId);
}
