package com.healthcare.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface IFinder<T> {
	
	/**
	 * Pagination 
	 * @param pageable
	 * @return Page<T>
	 */
	Page<T> findAll(Pageable pageable);
	
	/**
	 * Pagination 
	 * @param entity
	 * @param pageable
	 * @return Page<T>
	 */
	Page<T> findAll(T entity, Pageable pageable);
	
	/**
	 * Pagination 
	 * @param entity
	 * @param pageable
	 * @return Page<T>
	 */
	Page<T> findAll(Specification<T> specification, Pageable pageable);

	/**
	 * Get elements with specifications                                                  
	 * @param specification
	 * @return List<T> 
	 */
	List<T> findAll(Specification<T> specification);
}
