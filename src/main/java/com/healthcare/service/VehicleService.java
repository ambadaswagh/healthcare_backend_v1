package com.healthcare.service;

import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VehicleService extends IService<Vehicle>, IFinder<Vehicle> {
    List<Vehicle> findAll();

    Page<Vehicle> getVehiclesByAgency(Long agencyId, Pageable pageable);

    List<Vehicle> getVehiclesByAgencyList(Long agencyId);

    Page<Vehicle> findVehicleByAgencies(List<Long> agencyIds, Pageable pageable);

    List<Vehicle> findVehicleByAgenciesList(List<Long> agencyIds);

    public Page<Vehicle> getVehicleRegistrationEndNextDays(int days, Admin permissionAdmin, Pageable pageable, Long companyId, Long agencyId);

    public Page<Vehicle> getVehicleLiabilityInsuranceEndNextDays(int days, Admin permissionAdmin, Pageable pageable, Long companyId, Long agencyId);

    public Page<Vehicle> getVehicleExtraInsuranceEndNextDays(int days, Admin permissionAdmin, Pageable pageable, Long companyId, Long agencyId);

    public Page<Vehicle> getVehicleInspectionEndNextDays(int days, Admin permissionAdmin, Pageable pageable, Long companyId, Long agencyId);
}
