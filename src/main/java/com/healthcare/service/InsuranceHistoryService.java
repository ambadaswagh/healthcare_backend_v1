package com.healthcare.service;

import com.healthcare.model.entity.InsuranceHistory;

import javax.transaction.Transactional;
import java.util.List;

public interface InsuranceHistoryService extends IService<InsuranceHistory>, IFinder<InsuranceHistory> {

    InsuranceHistory save(InsuranceHistory insuranceHistory);

    List<InsuranceHistory> findAll();

    @Transactional
    List<InsuranceHistory> findByUserId(Long userId);
}
