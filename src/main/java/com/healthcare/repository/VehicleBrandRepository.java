package com.healthcare.repository;

import com.healthcare.model.entity.VehicleBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleBrandRepository extends JpaRepository<VehicleBrand, Long>, JpaSpecificationExecutor<VehicleBrand> {
}
