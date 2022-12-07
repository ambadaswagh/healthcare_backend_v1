package com.healthcare.repository;

import com.healthcare.model.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {

    @Query(value = "select v from Vehicle v where v.agency.id = ?1")
    Page<Vehicle> getVehiclesByAgency(Long agencyId, Pageable pageable);

    @Query(value = "select v from Vehicle v where v.agency.id = ?1")
    List<Vehicle> getVehiclesByAgencyList(Long agencyId);

    @Query(value = "select v from Vehicle v where v.agency.id in ?1")
    Page<Vehicle> findVehicleByAgencies(List<Long> agencyIds, Pageable pageable);

    @Query(value = "select v from Vehicle v where v.agency.id in ?1")
    List<Vehicle> findVehicleByAgenciesList(List<Long> agencyIds);

    @Query(value = "select v from Vehicle v where v.vehicleRegistrationExpire >=?1  and v.vehicleRegistrationExpire <=?2 and v.status= 1 order by v.vehicleRegistrationExpire asc")
    Page<Vehicle> getVehicleRegistrationEndFromDayToDay(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay, Pageable pageable);

    @Query(value = "select v from Vehicle v where v.agency.id = ?3 AND v.vehicleRegistrationExpire >=?1  and v.vehicleRegistrationExpire <=?2 and v.status= 1 order by v.vehicleRegistrationExpire asc")
    Page<Vehicle> getVehicleRegistrationEndFromDayToDayByAgency(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay,
                                                             @Param("agencyId") Long agencyId, Pageable pageable);

    @Query(value = "select v from Vehicle v where v.liabilityInsuranceExpire >=?1  and v.liabilityInsuranceExpire <=?2 and v.status= 1 order by v.liabilityInsuranceExpire asc")
    Page<Vehicle> getVehicleLiabilityInsuranceEndFromDayToDay(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay, Pageable pageable);

    @Query(value = "select v from Vehicle v where v.agency.id = ?3 AND v.liabilityInsuranceExpire >=?1  and v.liabilityInsuranceExpire <=?2 and v.status= 1 order by v.liabilityInsuranceExpire asc")
    Page<Vehicle> getVehicleLiabilityInsuranceEndFromDayToDayByAgency(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay,
                                                              @Param("agencyId") Long agencyId, Pageable pageable);

    @Query(value = "select v from Vehicle v where v.extraInsuranceExpire >=?1  and v.extraInsuranceExpire <=?2 and v.status= 1 order by v.extraInsuranceExpire asc")
    Page<Vehicle> getVehicleExtraInsuranceEndFromDayToDay(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay, Pageable pageable);

    @Query(value = "select v from Vehicle v where v.agency.id = ?3 AND v.extraInsuranceExpire >=?1  and v.extraInsuranceExpire <=?2 and v.status= 1 order by v.extraInsuranceExpire asc")
    Page<Vehicle> getVehicleExtraInsuranceEndFromDayToDayByAgency(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay,
                                                                      @Param("agencyId") Long agencyId, Pageable pageable);

    @Query(value = "select v from Vehicle v where v.vehicleInspectionExpire >=?1  and v.vehicleInspectionExpire <=?2 and v.status= 1 order by v.vehicleInspectionExpire asc")
    Page<Vehicle> getVehicleInspectionEndFromDayToDay(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay, Pageable pageable);

    @Query(value = "select v from Vehicle v where v.agency.id = ?3 AND v.vehicleInspectionExpire >=?1  and v.vehicleInspectionExpire <=?2 and v.status= 1 order by v.vehicleInspectionExpire asc")
    Page<Vehicle> getVehicleInspectionEndFromDayToDayByAgency(@Param("fromDay") Timestamp fromDay, @Param("toDay") Timestamp toDay,
                                                                  @Param("agencyId") Long agencyId, Pageable pageable);

}
