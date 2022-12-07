package com.healthcare.repository;

import com.healthcare.model.entity.Employee;
import com.healthcare.model.entity.FoodAllergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface FoodAllergyRepository extends JpaRepository<FoodAllergy, Long>, JpaSpecificationExecutor<FoodAllergy> {

  FoodAllergy findByName(String name);

  List<FoodAllergy> findByAddedByUser(Boolean addedByUser);
}
