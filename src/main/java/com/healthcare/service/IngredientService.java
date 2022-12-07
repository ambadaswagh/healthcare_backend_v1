package com.healthcare.service;

import com.healthcare.model.entity.Ingredient;

import java.util.List;

public interface IngredientService extends IService<Ingredient>, IFinder<Ingredient> {
  List<Ingredient> findAll();

  Ingredient findById(Long id);

  List<Ingredient> save(List<Ingredient> ingredients);

  Ingredient findByName(String name);
}
