package com.healthcare.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthcare.model.entity.Activity;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long>, JpaSpecificationExecutor<Activity> {


    @Query(value = " select a.* from visit v  "
                    + " inner join visit_has_activity vha on v.id = vha.visit_id "
                    + " inner join activity a  on vha.activity_id = a.id "
                    + " where ( ( DATE(v.check_in_time) >=?1 and DATE(v.check_out_time) <=?2 )  "
                    + "        OR  DATE(v.check_out_time) between ?1 and ?2 ) "
                    + " 	  and v.user_id = ?3 "
            , nativeQuery = true)
    List<Activity> findAllActivityForUser(@Param("startDate") Date startDate,
                                                @Param("endDate") Date endDate,
                                                @Param("userId") Long userId);

    @Query(value = "select tr from Activity tr where tr.agency.id = ?1")
    Page<Activity> findAllByAgency(Long agencyId, Pageable pageable);

    @Query(value = "select tr from Activity tr where tr.agency.id = ?1")
    List<Activity> findAllByAgencyList(Long agencyId);

    @Query(value = "select tr from Activity tr where tr.agency.id in ?1")
    Page<Activity> findAllByAgencies(List<Long> agencies, Pageable pageable);

    @Query(value = "select tr from Activity tr where tr.agency.id in ?1")
    List<Activity> findAllByAgenciesList(List<Long> agencies);
}