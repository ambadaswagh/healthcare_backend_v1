package com.healthcare.repository;

import com.healthcare.model.entity.RideLineColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RideLineColorRepository extends JpaRepository<RideLineColor, Long>, JpaSpecificationExecutor<RideLineColor> {

    @Query(
            " SELECT RC FROM  RideLineColor  RC " +
            "   WHERE RC.name NOT IN (SELECT DISTINCT rideLineColor FROM RideLine" +
            "       WHERE rideLineColor is not null)"
    )
    public List<RideLineColor> getRideLineColor();

    RideLineColor findByName(String name);
}
