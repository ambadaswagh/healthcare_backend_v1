package com.healthcare.service;

import com.healthcare.model.entity.Agency;
import com.healthcare.model.entity.Company;
import com.healthcare.model.entity.RideLineDaily;

import java.util.Date;
import java.util.List;

public interface RideLineDailyService extends IService<RideLineDaily>, IFinder<RideLineDaily> {

    RideLineDaily save(RideLineDaily rideLineDaily);

    Integer updateStatus(Long id, Long status);

    List<RideLineDaily> findAllRidesForRideLineDaily(Date date, Long agencyId, Long companyId);

    List<RideLineDaily> getRideLineDailyByRideLine(Long rideLineId, Long bound);
}
