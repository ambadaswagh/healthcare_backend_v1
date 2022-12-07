package com.healthcare.repository;

import com.healthcare.model.entity.RideLine;
import com.healthcare.model.entity.RideLineHasDriver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RideLineHasDriverRepository extends JpaRepository<RideLineHasDriver, Long>, JpaSpecificationExecutor<RideLineHasDriver> {
    @Query(
            value = " SELECT R.* FROM ride_line_has_driver R WHERE R.ride_line_id = :rideLineId AND day_of_week = :dayOfWeek", nativeQuery = true
    )
    public List<RideLineHasDriver> getRideLineHasDriverForRideLineOnDay(@Param("rideLineId") Long rideLineId,
                                       @Param("dayOfWeek") Long dayOfWeek);

}
