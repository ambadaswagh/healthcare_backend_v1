package com.healthcare.repository.assessment;

import com.healthcare.model.entity.assessment.Housing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by inimn on 26/12/2017.
 */
public interface HousingRepository extends JpaRepository<Housing, Long>, JpaSpecificationExecutor<Housing> {

	@Query(value = "SELECT housing.* from housing",nativeQuery = true)
	Housing findAllHousing();

	List<Housing> findByUserId(Long userId);
}
