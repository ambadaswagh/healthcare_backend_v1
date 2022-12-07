package com.healthcare.service;

import com.healthcare.model.entity.RideLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RideLineService extends IService<RideLine>, IFinder<RideLine> {

    RideLine save(RideLine rideLine);

    List<RideLine> findAll();

    List<RideLine> getDistRideLineForRides(Long agencyId, Long companyId);
    Integer updateStatus(Long id, Long status);

    public List<RideLine> getRideLines(Long agencyId, Long companyId);

    Page<RideLine> findRideLineByAgenciesList(List<Long> agencyIds, Pageable pageable);

    Page<RideLine> getRideLineByAgencyList(Long agencyId, Pageable pageable);

}
