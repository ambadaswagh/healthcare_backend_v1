package com.healthcare.service;

import com.healthcare.dto.RideLineIdDTO;
import com.healthcare.dto.RideValidInvalidDTO;
import com.healthcare.dto.VisitRequestDTO;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface RideService extends IService<Ride>, IFinder<Ride> {

    Ride save(Ride ride);

    List<Ride> findAll();
    List<Ride> findAllRidesForRideLine(Long id, Long needTrip, Date date, String search);
    List<Ride> findAllRidesForRideLineByCompany(Long id, Long needTrip, Date date, String search, Long companyId);
    List<Ride> findAllRidesForRideLineByAgency(Long id, Long needTrip, Date date, String search, Long agencyId);

    void findByDateMaxTrip();

    byte[] getRideBillingReportAndDownload(Admin permissionAdmin, VisitRequestDTO dto, HttpServletRequest req);

    void distributeRideIntoRideLine(List<Long> rideIdes, List<RideLineIdDTO> rideLineIds);

    List<Ride> generateTripReport(Long needTrip, Date date, Set<Long> drivers, Set<Long> rideLineIds, Long status);

    RideValidInvalidDTO getAuthorizedRiders(Admin admin, String startDate, String endDate, List<Long> agencyIds, List<Long> seniorIds,
                                            Pageable pageable, boolean isInvalid);

    Page<Ride> getRideByCompanyAndAgency(Long agencyId, Long companyId, Pageable pageable);

    Page<Ride> rideFilter(Long agencyId, Long companyId, Long userId, Long driverId, Date fromDate, Date toDate, Pageable pageable);

    Page<Ride> findByCompany(Long companyId, Pageable pageable);
}
