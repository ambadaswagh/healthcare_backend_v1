package com.healthcare.repository;

import com.healthcare.dto.DoctorAppointmentDTO;
import com.healthcare.model.entity.CareGiver;
import com.healthcare.model.entity.Company;
import com.healthcare.model.entity.DoctorAppointment;
import com.healthcare.model.entity.RideLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Modifying;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideLineRepository extends JpaRepository<RideLine, Long>, JpaSpecificationExecutor<RideLine> {
    @Query("SELECT R FROM RideLine R WHERE R.agency.id = :agencyId AND R.company.id = :companyId")
    public List<RideLine> getDistRideLineForRides(@Param("agencyId") Long agencyId,
                                                  @Param("companyId") Long companyId);
    
    @Query("SELECT R.id FROM RideLine R WHERE R.agency.id = :agencyId AND R.company.id = :companyId")
    public List<Long> getIdsDistRideLineForRides(@Param("agencyId") Long agencyId,
                                                  @Param("companyId") Long companyId);

    @Query(
            " SELECT R FROM RideLine R " +
                    "   WHERE R.agency.id = :agencyId " +
                    "         AND R.company.id = :companyId "
    )
    public List<RideLine> getRideLines(@Param("agencyId") Long agencyId,
                                       @Param("companyId") Long companyId);

    @Modifying
    @Query(" UPDATE RideLine SET status = :status WHERE id = :rideLineId ")
    void updateStatus(@Param("rideLineId") Long id, @Param("status") Long status);


    List<RideLine> findByCompany(Company company);

    @Query(value = "select v from RideLine v where v.agency.id in ?1")
    Page<RideLine> findRideLineByAgenciesList(List<Long> agencyIds, Pageable pageable);

    @Query(value = "select v from RideLine v where v.agency.id = ?1")
    Page<RideLine> getRideLineByAgencyList(Long agencyId, Pageable pageable);

    @Query(
      " SELECT R FROM RideLine R " +
        "   WHERE R.agency.id = :agencyId " +
        "         AND R.company.id = :companyId AND R.driver.id = :driverId")
    List<RideLine> getRideLineByCompanyAndAgencyAndDriver(@Param("agencyId") Long agencyId,
                                                          @Param("companyId") Long companyId, @Param("driverId") Long driverId);
    
    @Query(" SELECT R.id FROM RideLine R " +
    	        "   WHERE R.agency.id = :agencyId " +
    	        "         AND R.company.id = :companyId AND R.driver.id = :driverId")
    List<Long> getIdsRideLineByCompanyAndAgencyAndDriver(@Param("agencyId") Long agencyId,
    	                                                          @Param("companyId") Long companyId, @Param("driverId") Long driverId);
}
