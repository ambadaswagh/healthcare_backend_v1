package com.healthcare.repository.assessment;

import com.healthcare.model.entity.assessment.Nutrition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by inimn on 26/12/2017.
 */
public interface NutritionRepository extends JpaRepository<Nutrition, Long>, JpaSpecificationExecutor<Nutrition> {

	//@Query(value = " SELECT nutrition.* from nutrition ")
	//Nutrition findAllNutrition();

    List<Nutrition> findByUserId(Long userId);
}
