package com.healthcare.repository;

import com.healthcare.model.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RideLineDailyRepository extends JpaRepository<RideLineDaily, Long>, JpaSpecificationExecutor<RideLineDaily> {
    @Query(
                " SELECT R FROM RideLineDaily R " /*+
                "   WHERE EXISTS (SELECT 'X' FROM Ride A " +
                "     WHERE A.rideLine.id = R.id ) "*/
    )
    public List<RideLine> getDistRideLineForDates();

    @Query(value = "select u from RideLineDaily u " +
            " where (:date is null or DATE(u.date) = :date) " +
            "   and (u.rideLine.agency.id = :agency) " +
            "   and (u.rideLine.company.id = :company)")
    List<RideLineDaily> findAllRidesForRideLineDaily(@Param("date") Date date,
                                                     @Param("agency") Long agencyId,
                                                     @Param("company") Long companyId);

    @Query(value = "SELECT v from RideLineDaily v where DATE(v.date) = :date AND v.rideLine.id = :rideLineId")
    List<RideLineDaily> findByDate(@Param("date") Date date, @Param("rideLineId") Long rideLineId);

    @Query(
            " SELECT R FROM RideLine R "
    )
    public List<RideLine> getRideLines();

    @Modifying
    @Query(" UPDATE RideLineDaily SET status = :status WHERE id = :rideLineDailyId ")
    void updateStatus(@Param("rideLineDailyId") Long id, @Param("status") Long status);

    @Modifying
    @Query(" UPDATE RideLineDaily SET vehicle = :vehicle WHERE id = :rideLineDailyId ")
    int updateVehicleForRideLineDaily(@Param("rideLineDailyId") Long id, @Param("vehicle") Vehicle vehicle);

    @Modifying
    @Query(" UPDATE RideLineDaily SET inboundDriver = :driver WHERE id = :rideLineDailyId ")
    int updateInboundDriverForRideLineDaily(@Param("rideLineDailyId") Long id, @Param("driver") Driver driver);

    @Modifying
    @Query(" UPDATE RideLineDaily SET outboundDriver = :driver WHERE id = :rideLineDailyId ")
    int updateOutboundDriverForRideLineDaily(@Param("rideLineDailyId") Long id, @Param("driver") Driver driver);

    List<RideLineDaily> findByRideLine(RideLine rideLineId);
}
