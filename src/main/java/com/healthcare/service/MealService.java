package com.healthcare.service;

import java.util.List;

import com.healthcare.api.model.MealMobileDTO;
import com.healthcare.model.entity.Ingredient;
import com.healthcare.model.entity.Meal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MealService extends IService<Meal>, IFinder<Meal> {

	List<Meal> findAll();
	List<Ingredient> saveAllIngredients(Long d, List<Ingredient> ingredients);
	List<Ingredient> findAllIngredients(Long id);

	Page<Meal> findAllByAgency(Long agencyId, Pageable pageable);

	MealMobileDTO findAllByAgency(Long agencyId, Long userId);

	Page<Meal> findAllByAgencies(List<Long> agencies, Pageable pageable);

    Meal getMeal(Long id);

	List<Meal> bulkUpdate(List<Meal> meals);
}
