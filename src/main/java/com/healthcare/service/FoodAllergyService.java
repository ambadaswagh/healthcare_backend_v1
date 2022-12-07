package com.healthcare.service;

import com.healthcare.model.entity.FoodAllergy;

import java.util.List;

public interface FoodAllergyService extends IService<FoodAllergy>, IFinder<FoodAllergy> {
    List<FoodAllergy> findAll();
    FoodAllergy findById(Long id);

    List<FoodAllergy> findByAddedByUser(Boolean addedByUser);

    List<FoodAllergy> getAlleryListForUser(Long id);
}
