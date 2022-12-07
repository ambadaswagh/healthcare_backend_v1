package com.healthcare.repository.assessment;

import com.healthcare.model.entity.assessment.MonthlyIncome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by inimn on 26/12/2017.
 */
public interface MonthlyIncomeRepository extends JpaRepository<MonthlyIncome, Long>, JpaSpecificationExecutor<MonthlyIncome> {
    List<MonthlyIncome> findByUserId(Long userId);

}
