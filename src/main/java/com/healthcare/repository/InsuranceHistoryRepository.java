package com.healthcare.repository;

import com.healthcare.model.entity.InsuranceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface InsuranceHistoryRepository extends JpaRepository<InsuranceHistory, Long>, JpaSpecificationExecutor<InsuranceHistory> {

    List<InsuranceHistory> findByUserId(Long UserId);

    @Query(
            " SELECT 1 FROM InsuranceHistory IH " +
                "   WHERE " +
                    "   IH.user.id = :userId " +
                    "   and IH.insurance.id = :insuranceId " +
                    "   and IH.authorizationStart = :authorizationStart " +
                    "   and IH.authorizationEnd = :authorizationEnd " +
                    "   and ((:authorizationCode is null and IH.authorizationCode is null) or IH.authorizationCode = :authorizationCode) " +
                    "   and ((:insuranceCode is null and IH.insuranceCode is null) or IH.insuranceCode = :insuranceCode) "
    )
    public Integer getInsuranceHistory(@Param("userId") Long userId,
                                                @Param("insuranceId") Long insuranceId,
                                                @Param("authorizationStart") Timestamp authorizationStart,
                                                @Param("authorizationEnd") Timestamp authorizationEnd,
                                                @Param("authorizationCode") String authorizationCode,
                                                @Param("insuranceCode") String insuranceCode);
}
