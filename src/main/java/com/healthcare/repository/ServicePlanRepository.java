package com.healthcare.repository;

import com.healthcare.model.entity.ServicePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ServicePlanRepository extends JpaRepository<ServicePlan, Long>, JpaSpecificationExecutor<ServicePlan>,
        ServicePlanRepositoryCustom {

    @Query(value = "select s.* from serviceplan s "
            + " where ( "
            + "         ( DATE(s.plan_start) >=?1 "
            + "             and DATE(s.plan_end) <=?2"
            + "          )  "
            + "          OR  DATE(s.plan_end) between ?1 and ?2 "
            + "      ) "
            + "    and s.user_id = ?3 "
            , nativeQuery = true)
    List<ServicePlan> findAllServicePlanForUser(@Param("startDate") Date startDate,
                                    @Param("endDate") Date endDate,
                                    @Param("userId") Long userId);
    
    
    @Query(value = "select * from service_plan where user_id = ?1", nativeQuery = true)
    List<ServicePlan> findServicePlanByUserId(@Param("userId") Long userId);

}