package com.healthcare.repository.assessment;

import com.healthcare.model.entity.assessment.BenefitEntitlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by inimn on 26/12/2017.
 */
public interface BenefitEntitlementRepository extends JpaRepository<BenefitEntitlement, Long>, JpaSpecificationExecutor<BenefitEntitlement> {
    List<BenefitEntitlement> findByUserId(Long userId);
}
