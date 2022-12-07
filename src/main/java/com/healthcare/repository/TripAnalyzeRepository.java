package com.healthcare.repository;

import com.healthcare.model.entity.TripAnalyze;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TripAnalyzeRepository extends JpaRepository<TripAnalyze, Long>, JpaSpecificationExecutor<TripAnalyze> {

    @Query(value = "select case when max(max_trip_id) > 0 then max(max_trip_id) else 0 end as mx from trip_analyze", nativeQuery = true)
    public Integer getMaxTripId();

    @Query(value = "select tr.user_id, tr.ride_line_id, case when max(tr.trip_count) > 0 then max(tr.trip_count) else 0 end as max_trip_count " +
            "from trip_analyze tr where tr.analyze_date >= ?1 and tr.analyze_date <= ?2 group by tr.user_id, tr.ride_line_id order by max_trip_count DESC ", nativeQuery = true)
    List<Object[]> findMaxTripCount(@Param("startDate")Date startDate, @Param("endDate") Date endDate);

    @Query(value = "select d.first_name firstName, d.last_name lastName, count(t.id) count from driver d, trip_analyze t where t.driver_id = d.id " +
            "and t.user_id = ?1 GROUP BY d.first_name, d.last_name order by count desc", nativeQuery = true)
    List<Object[]> findDriverFrequency(@Param("userId") Long userId);


}

