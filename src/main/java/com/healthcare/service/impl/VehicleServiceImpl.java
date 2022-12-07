package com.healthcare.service.impl;

import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.AdminAgencyCompanyOrganization;
import com.healthcare.model.entity.Driver;
import com.healthcare.model.entity.Vehicle;
import com.healthcare.repository.AdminAgencyCompanyOrganizationRepository;
import com.healthcare.repository.VehicleRepository;
import com.healthcare.service.VehicleService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.healthcare.api.common.RoleUtil.isAgencyAdmin;
import static com.healthcare.api.common.RoleUtil.isCompanyAdmin;
import static com.healthcare.api.common.RoleUtil.isSuperAdmin;

@Service
@Transactional
public class VehicleServiceImpl extends  BasicService<Vehicle, VehicleRepository> implements VehicleService {
    private static final String KEY = Vehicle.class.getSimpleName();

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    private RedisTemplate<String, Vehicle> redisTemplate;

    @Override
    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    @Autowired
    private AdminAgencyCompanyOrganizationRepository adminAgencyCompanyOrganizationRepository;



    @Override @Transactional
    public Vehicle save(Vehicle vehicle) {
    	if(vehicle.getId() != null){
    		vehicle.setUpdatedAt(new Timestamp(new Date().getTime()));
	  	} else {
	  		vehicle.setCreatedAt(new Timestamp(new Date().getTime()));
	  		vehicle.setUpdatedAt(new Timestamp(new Date().getTime()));
	  	}
		if(vehicle.getVehicleInspectionStart() != null){
			vehicle.getVehicleInspectionStart().setHours(12);
			vehicle.getVehicleInspectionStart().setMinutes(0);
			vehicle.getVehicleInspectionStart().setSeconds(0);
    	}
		if(vehicle.getVehicleInspectionExpire() != null){
			vehicle.getVehicleInspectionExpire().setHours(12);
			vehicle.getVehicleInspectionExpire().setMinutes(0);
			vehicle.getVehicleInspectionExpire().setSeconds(0);
    	}
		if(vehicle.getVehicleRegistrationStart() != null){
			vehicle.getVehicleRegistrationStart().setHours(12);
			vehicle.getVehicleRegistrationStart().setMinutes(0);
			vehicle.getVehicleRegistrationStart().setSeconds(0);
    	}
		if(vehicle.getVehicleRegistrationExpire() != null){
			vehicle.getVehicleRegistrationExpire().setHours(12);
			vehicle.getVehicleRegistrationExpire().setMinutes(0);
			vehicle.getVehicleRegistrationExpire().setSeconds(0);
    	}
		if(vehicle.getVehicleTlcFhvLicenseStart() != null){
			vehicle.getVehicleTlcFhvLicenseStart().setHours(12);
			vehicle.getVehicleTlcFhvLicenseStart().setMinutes(0);
			vehicle.getVehicleTlcFhvLicenseStart().setSeconds(0);
    	}
		if(vehicle.getVehicleTlcFhvLicenseExpire() != null){
			vehicle.getVehicleTlcFhvLicenseExpire().setHours(12);
			vehicle.getVehicleTlcFhvLicenseExpire().setMinutes(0);
			vehicle.getVehicleTlcFhvLicenseExpire().setSeconds(0);
    	}
		if(vehicle.getLiabilityInsuranceStart() != null){
			vehicle.getLiabilityInsuranceStart().setHours(12);
			vehicle.getLiabilityInsuranceStart().setMinutes(0);
			vehicle.getLiabilityInsuranceStart().setSeconds(0);
    	}
		if(vehicle.getLiabilityInsuranceExpire() != null){
			vehicle.getLiabilityInsuranceExpire().setHours(12);
			vehicle.getLiabilityInsuranceExpire().setMinutes(0);
			vehicle.getLiabilityInsuranceExpire().setSeconds(0);
    	}
		if(vehicle.getExtraInsuranceStart() != null){
			vehicle.getExtraInsuranceStart().setHours(12);
			vehicle.getExtraInsuranceStart().setMinutes(0);
			vehicle.getExtraInsuranceStart().setSeconds(0);
    	}
		if(vehicle.getExtraInsuranceExpire() != null){
			vehicle.getExtraInsuranceExpire().setHours(12);
			vehicle.getExtraInsuranceExpire().setMinutes(0);
			vehicle.getExtraInsuranceExpire().setSeconds(0);
    	}
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        redisTemplate.opsForHash().put(KEY, savedVehicle.getId(), savedVehicle);

        return savedVehicle;
    }

    @Override @Transactional
    public Long deleteById(Long id) {
        vehicleRepository.delete(id);

        return redisTemplate.opsForHash().delete(KEY, id);
    }

    @Override @Transactional
    public Vehicle findById(Long id) {
        Object vehicle = redisTemplate.opsForHash().get(KEY, id);
        if (vehicle != null) {
            return convertToClass(vehicle);
        }
        return vehicleRepository.findOne(id);
    }

    @Override @Transactional
    public Long disableById(Long id) {
        //TODO
        return null;
    }

    @Override
    public Page<Vehicle> getVehiclesByAgency(Long agencyId, Pageable pageable) {
        return vehicleRepository.getVehiclesByAgency(agencyId, pageable);
    }

    @Override
    public List<Vehicle> getVehiclesByAgencyList(Long agencyId) {
        return vehicleRepository.getVehiclesByAgencyList(agencyId);
    }

    @Override
    public Page<Vehicle> findVehicleByAgencies(List<Long> agencyIds, Pageable pageable) {
        return vehicleRepository.findVehicleByAgencies(agencyIds, pageable);
    }

    @Override
    public List<Vehicle> findVehicleByAgenciesList(List<Long> agencyIds) {
        return vehicleRepository.findVehicleByAgenciesList(agencyIds);
    }

    @Override
    public Page<Vehicle> getVehicleRegistrationEndNextDays(int days, Admin permissionAdmin, Pageable pageable, Long companyId, Long agencyId) {
        if (days < 0)
            return new PageImpl<Vehicle>(new ArrayList<>());
        DateTime currentDate = new DateTime();
        DateTime today = currentDate.minusDays(1).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
        DateTime endDay = currentDate.plusDays(days).plusDays(days).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);

        Page<Vehicle> returnedPages = null;

        if (companyId != null && agencyId != null) {
            returnedPages = vehicleRepository.getVehicleRegistrationEndFromDayToDayByAgency(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
                    agencyId, pageable);
        } else {
            if(isSuperAdmin(permissionAdmin)){
                returnedPages = vehicleRepository.getVehicleRegistrationEndFromDayToDay(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()), pageable);
            }else if (isCompanyAdmin(permissionAdmin) || isAgencyAdmin(permissionAdmin)){
                AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

                if (adminAgencyCompanyOrganization != null) {
                    returnedPages = vehicleRepository.getVehicleRegistrationEndFromDayToDayByAgency(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
                            adminAgencyCompanyOrganization.getAgency().getId(), pageable);
                }
            }
        }


        return returnedPages;
    }

    @Override
    public Page<Vehicle> getVehicleLiabilityInsuranceEndNextDays(int days, Admin permissionAdmin, Pageable pageable, Long companyId, Long agencyId) {
        if (days < 0)
            return new PageImpl<Vehicle>(new ArrayList<>());
        DateTime currentDate = new DateTime();
        DateTime today = currentDate.minusDays(1).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
        DateTime endDay = currentDate.plusDays(days).plusDays(days).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);

        Page<Vehicle> returnedPages = null;

        if (companyId != null && agencyId != null) {
            returnedPages = vehicleRepository.getVehicleLiabilityInsuranceEndFromDayToDayByAgency(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
                    agencyId, pageable);
        } else {
            if(isSuperAdmin(permissionAdmin)){
                returnedPages = vehicleRepository.getVehicleLiabilityInsuranceEndFromDayToDay(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()), pageable);
            }else if (isCompanyAdmin(permissionAdmin) || isAgencyAdmin(permissionAdmin)){
                AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

                if (adminAgencyCompanyOrganization != null) {
                    returnedPages = vehicleRepository.getVehicleLiabilityInsuranceEndFromDayToDayByAgency(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
                            adminAgencyCompanyOrganization.getAgency().getId(), pageable);
                }
            }
        }

        return returnedPages;
    }

    @Override
    public Page<Vehicle> getVehicleExtraInsuranceEndNextDays(int days, Admin permissionAdmin, Pageable pageable, Long companyId, Long agencyId) {
        if (days < 0)
            return new PageImpl<Vehicle>(new ArrayList<>());
        DateTime currentDate = new DateTime();
        DateTime today = currentDate.minusDays(1).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
        DateTime endDay = currentDate.plusDays(days).plusDays(days).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);

        Page<Vehicle> returnedPages = null;

        if (companyId != null && agencyId != null) {
            returnedPages = vehicleRepository.getVehicleExtraInsuranceEndFromDayToDayByAgency(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
                    agencyId, pageable);
        } else {
            if(isSuperAdmin(permissionAdmin)){
                returnedPages = vehicleRepository.getVehicleExtraInsuranceEndFromDayToDay(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()), pageable);
            }else if (isCompanyAdmin(permissionAdmin) || isAgencyAdmin(permissionAdmin)){
                AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

                if (adminAgencyCompanyOrganization != null) {
                    returnedPages = vehicleRepository.getVehicleExtraInsuranceEndFromDayToDayByAgency(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
                            adminAgencyCompanyOrganization.getAgency().getId(), pageable);
                }
            }
        }


        return returnedPages;
    }

    @Override
    public Page<Vehicle> getVehicleInspectionEndNextDays(int days, Admin permissionAdmin, Pageable pageable, Long companyId, Long agencyId) {
        if (days < 0)
            return new PageImpl<Vehicle>(new ArrayList<>());
        DateTime currentDate = new DateTime();
        DateTime today = currentDate.minusDays(1).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
        DateTime endDay = currentDate.plusDays(days).plusDays(days).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
        Page<Vehicle> returnedPages = null;

        if (companyId != null && agencyId != null) {
            returnedPages = vehicleRepository.getVehicleInspectionEndFromDayToDayByAgency(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
                    agencyId, pageable);
        } else {
            if(isSuperAdmin(permissionAdmin)){
                returnedPages = vehicleRepository.getVehicleInspectionEndFromDayToDay(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()), pageable);
            }else if (isCompanyAdmin(permissionAdmin) || isAgencyAdmin(permissionAdmin)){
                AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

                if (adminAgencyCompanyOrganization != null) {
                    returnedPages = vehicleRepository.getVehicleInspectionEndFromDayToDayByAgency(new Timestamp(today.getMillis()), new Timestamp(endDay.getMillis()),
                            adminAgencyCompanyOrganization.getAgency().getId(), pageable);
                }
            }
        }


        return returnedPages;
    }

}
