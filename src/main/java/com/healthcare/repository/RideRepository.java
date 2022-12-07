package com.healthcare.repository;

import com.healthcare.model.entity.Ride;
import com.healthcare.model.entity.RideLine;
import com.healthcare.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;

import java.util.List;
import java.util.Set;

public interface RideRepository extends JpaRepository<Ride, Long>, JpaSpecificationExecutor<Ride> {


    @Query(value = "select u.* from ride u where u.ride_line_id =  ?1 " , nativeQuery = true)
    List<Ride> findAllRidesForRideLine(@Param("rideLineId") Integer rideLineId);

    @Query(value = "select u.* from ride u " +
            " where u.ride_line_daily_id =  ?1 " +
            "   and (u.need_trip = ?2 or ?2 = 0) " +
            "   and (?3 is null or DATE(u.date) = ?3)", nativeQuery = true)
    List<Ride> findAllRidesForRideLine(@Param("rideLineId") Long rideLineId,
                                       @Param("needTrip") Long needTrip,
                                       @Param("date") Date date);

    @Query(value = "select u.* from ride u " +
            " where u.ride_line_daily_id =  ?1 " +
            "   and (u.need_trip = ?2 or ?2 = 0) " +
            "   and (?3 is null or DATE(u.date) = ?3) " +
            "   and (select company_id from user where id = u.user_id) = ?4", nativeQuery = true)
    List<Ride> findAllRidesForRideLineByCompany(@Param("rideLineId") Long rideLineId,
                                                @Param("needTrip") Long needTrip,
                                                @Param("date") Date date,
                                                @Param("companyId") Long companyId);

    @Query(value = "select u.* from ride u " +
            " where u.ride_line_daily_id =  ?1 " +
            "   and (u.need_trip = ?2 or ?2 = 0) " +
            "   and (?3 is null or DATE(u.date) = ?3) " +
            "   and (select agency_id from user where id = u.user_id) = ?4", nativeQuery = true)
    List<Ride> findAllRidesForRideLineByAgency(@Param("rideLineId") Long rideLineId,
                                               @Param("needTrip") Long needTrip,
                                               @Param("date") Date date,
                                               @Param("agencyId") Long agencyId);


    @Query(value = "select u from Ride u " +
            " where u.rideLine.id in  (:rideLineIds) " +
            "   and (u.rideLineDaily.inboundDriver.id in  (:drivers) or u.rideLineDaily.outboundDriver.id in  (:drivers))" +
            "   and (:status is null or u.status = :status) " +
            "   and (u.needTrip = :needTrip or :needTrip = 0) " +
            "   and (:date is null or u.date = :date) order by u.rideLine.id")
    List<Ride> generateTripReportForDriverRideLine(
            @Param("needTrip") Long needTrip,
            @Param("date") Date date,
            @Param("drivers") Set<Long> drivers,
            @Param("rideLineIds") Set<Long> rideLineId,
            @Param("status") Long status);

    @Query(value = "select u from Ride u " +
            " where (u.rideLineDaily.inboundDriver.id in  (:drivers) or u.rideLineDaily.outboundDriver.id in  (:drivers))" +
            "   and (:status is null or u.status = :status) " +
            "   and (u.needTrip = :needTrip or :needTrip = 0) " +
            "   and (:date is null or u.date = :date) order by u.rideLine.id")
    List<Ride> generateTripReportForDriver(
            @Param("needTrip") Long needTrip,
            @Param("date") Date date,
            @Param("drivers") Set<Long> drivers,
            @Param("status") Long status);

    @Query(value = "select u from Ride u " +
            " where u.rideLine.id in  (:rideLineIds) " +
            "   and (:status is null or u.status = :status) " +
            "   and (u.needTrip = :needTrip or :needTrip = 0) " +
            "   and (:date is null or u.date = :date) order by u.rideLine.id")
    List<Ride> generateTripReportForRideLine(
            @Param("needTrip") Long needTrip,
            @Param("date") Date date,
            @Param("rideLineIds") Set<Long> rideLineId,
            @Param("status") Long status);

    @Query(value = "select u from Ride u " +
            "   where (:status is null or u.status = :status) " +
            "   and (u.needTrip = :needTrip or :needTrip = 0) " +
            "   and (:date is null or u.date = :date) order by u.rideLine.id")
    List<Ride> generateTripReportForDate(
            @Param("needTrip") Long needTrip,
            @Param("date") Date date,
            @Param("status") Long status);

    List<Ride> findByDate(Date date);

    @Modifying
    @Query(" UPDATE Ride SET status = :status WHERE rideLineDaily.id = :rideLineDailyId ")
    void updateStatus(@Param("rideLineDailyId") Long id, @Param("status") Long status);


    @Transactional
    Long deleteByUser(User user);

    @Modifying
    @Query(" DELETE FROM Ride WHERE rideLine.id = :rideLineId ")
    void deleteRide(@Param("rideLineId") Long id);


    @Query(value = "select v.* from ride v inner join user u on v.user_id = u.id " +
            "where DATE(v.date) >=:startDate AND DATE(v.date) <=:endDate ORDER BY v.date ASC ", nativeQuery = true)
    List<Ride> findAllRiders(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    @Query(value = "select v.* from ride v inner join user u on v.user_id = u.id " +
            "where u.company_id =:companyId AND DATE(v.date) >=:startDate AND DATE(v.date) <=:endDate ORDER BY v.date ASC ", nativeQuery = true)
    List<Ride> findAllRidersByCompany(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate, @Param("companyId") Long companyId);

    @Query(value = "select v.* from ride v inner join user u on v.user_id = u.id " +
            "where u.company_id =:companyId AND u.agency_id =:agencyId AND DATE(v.date) >=:startDate AND DATE(v.date) <=:endDate ORDER BY v.date ASC ", nativeQuery = true)
    List<Ride> findAllRidersCompanyAgency(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate, @Param("companyId") Long companyId, @Param("agencyId") Long agencyId);

    @Query(value = "select v.* from ride v where u.agency_id IN :agencyIds AND DATE(v.date) >=:startDate AND DATE(v.date) <=:endDate " +
                    "ORDER BY v.date ASC ", nativeQuery = true)
    List<Ride> findAllRidersAgencies(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate, @Param("agencyIds") List<Long> agencyIds);

    @Query(value = "select v.* from ride v where v.user_id IN :userIds AND DATE(v.date) >=:startDate AND DATE(v.date) <=:endDate " +
                    "ORDER BY v.date ASC ", nativeQuery = true)
    List<Ride> findAllRidersBySeniors(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("userIds") List<Long> userIds);

    @Query(value = "select v.* from ride v where v.user_id IN :userIds AND DATE(v.date) >=:startDate AND DATE(v.date) <=:endDate " +
            " AND v.agency_id in :agencyIds ORDER BY v.date ASC ", nativeQuery = true)
    List<Ride> findAllRidersAgenciesAndSeniors(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
                                               @Param("agencyIds") List<Long> agencyIds, @Param("userIds") List<Long> userIds);

    @Query(value = "select r from Ride r where DATE(r.date) = DATE(:date) AND r.rideLine = :rideLine AND r.needTrip = :needTrip AND r.user.id = :userId")
    List<Ride> findRideIfExists(
            @Param("date") Date date,
            @Param("rideLine") RideLine rideLine,
            @Param("needTrip") Long needTrip,
            @Param("userId") Long userId);

   Long countByDateAndRideLineAndNeedTripAndUserAndIdNotIn(Date date, RideLine rideLine,Long needTrip,User user,Long id);

    @Query(value = "select r.user_id, r.pickup, r.dropoff, r.ride_line_id, u.driver_id, u.user_full_name, count(r.ride_line_id) as trip_count, (select max(id) from ride) as max_id from ride  r " +
            "inner join (select u.id, rl.driver_id, concat(u.first_name, ' ', u.last_name) as user_full_name from user u inner join ride_line rl on u.ride_line_id = rl.id) u on r.user_id = u.id  " +
            "where r.date >= ?1 and r.date <= ?2 and r.id > ?3 group by r.user_id, r.dropoff, r.pickup, r.ride_line_id;", nativeQuery = true)
    List<Object[]> findByDateMaxTrip(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("maxTripId") Integer maxtripId);

    List<Ride> findByRideLineIn(List<RideLine> rideLineList);

    Page<Ride>  findByRideLineIn(List<RideLine> rideLineList, Pageable pageable);

    List<Ride> findByDateGreaterThanEqualAndDateLessThanEqualAndRideLineIn(Date from, Date to , List<RideLine> rideLine);

    List<Ride> findByRideLineDailyId(Long rideLineDailyId);
    
    @Query(value = "SELECT * FROM ride r where r.ride_line_id in (?1) and r.date >= ?2 and r.date <= ?3 ORDER BY ?#{#pageable}",
    		countQuery = "SELECT count(*) FROM ride r where r.ride_line_id in (?1) and r.date >= ?2 and r.date <= ?3 ORDER BY ?#{#pageable}", nativeQuery = true)
    Page<Ride> findAllRideLinesUserIdDates(List<Long> ridelineIds,Date from_date,Date to_date, Pageable pageable);
    @Query(value = "SELECT * FROM ride r where r.ride_line_id in (?1) and r.user_id=?2 and r.date >= ?3 and r.date <= ?4 ORDER BY ?#{#pageable}",
    		countQuery = "SELECT count(*) FROM ride r where r.ride_line_id in (?1) and r.user_id=?2 and r.date >= ?3 and r.date <= ?4 ORDER BY ?#{#pageable}", nativeQuery = true)
    Page<Ride> findAllRideLinesUserIdDates(List<Long> ridelineIds, Long userId,Date from_date,Date to_date, Pageable pageable);
}
