package com.healthcare.service.impl;

import com.healthcare.dto.*;
import com.healthcare.exception.UserException;
import com.healthcare.model.entity.*;
import com.healthcare.model.enums.FixedStatusEnum;
import com.healthcare.model.enums.PeriodTypeEnum;
import com.healthcare.model.enums.RideValidationEnum;
import com.healthcare.model.enums.UnitTypeEnum;
import com.healthcare.repository.*;
import com.healthcare.service.BillingService;
import com.healthcare.service.ReportService;
import com.healthcare.service.RideService;
import com.healthcare.specification.RideSpecification;

import io.jsonwebtoken.lang.Collections;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.healthcare.api.common.HealthcareConstants.AUTHENTICATED_ADMIN;
import static com.healthcare.api.common.HealthcareConstants.NOT_AUTHORIZED;
import static com.healthcare.api.common.HealthcareUtil.isEmpty;
import static com.healthcare.api.common.RoleUtil.isAgencyAdmin;
import static com.healthcare.api.common.RoleUtil.isCompanyAdmin;
import static com.healthcare.api.common.RoleUtil.isSuperAdmin;
import static org.springframework.data.jpa.domain.Specifications.where;

@Service
@Transactional
public class RideServiceImpl extends BasicService<Ride, RideRepository> implements RideService {

    private static final String KEY = Ride.class.getSimpleName();

    @Autowired
    RideRepository rideRepository;

    @Autowired
    TripAnalyzeRepository tripAnalyzeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    RideLineRepository rideLineRepository;

    @Autowired
    RideLineDailyRepository rideLineDailyRepository;

    @Autowired
    AdminAgencyCompanyOrganizationRepository adminAgencyCompanyOrganizationRepository;
    @Autowired
    public BillingService billingService;

    @Autowired
    private RedisTemplate<String, Ride> rideRedisTemplate;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    private ReportService reportService;

    @Override
    @Transactional
    public Ride save(Ride ride) {
        ride = rideRepository.save(ride);
        rideRedisTemplate.opsForHash().put(KEY, ride.getId(), ride);
        return ride;
    }

    @Override
    @Transactional
    public Ride findById(Long id) {
        Ride ride = (Ride) rideRedisTemplate.opsForHash().get(KEY, id);
        if (ride == null)
            ride = rideRepository.findOne(id);
        return ride;
    }

    @Override
    @Transactional
    public Long deleteById(Long id) {
        rideRepository.delete(id);
        return rideRedisTemplate.opsForHash().delete(KEY, id);
    }

    @Override
    @Transactional
    public List<Ride> findAll() {
        Map<Object, Object> rideMap = rideRedisTemplate.opsForHash().entries(KEY);
        List<Ride> rideList = Collections.arrayToList(rideMap.values().toArray());
        if (rideMap.isEmpty())
            rideList = rideRepository.findAll();
        return rideList;
    }

    @Override
    @Transactional
    public Long disableById(Long id) {
        //TODO
        return null;
    }

    public List<Ride> findAllRidesForRideLine(Long id, Long needTrip, Date date, String search) {

        // handling black list here
        RideLineDaily rideLineDaily = rideLineDailyRepository.findOne(id);
        Driver inboundDriver = rideLineDaily.getInboundDriver();
        Driver ouboundDriver = rideLineDaily.getOutboundDriver();

        return processRides(inboundDriver, ouboundDriver, rideRepository.findAllRidesForRideLine(id, needTrip, date));
    }

    public List<Ride> findAllRidesForRideLineByCompany(Long id, Long needTrip, Date date, String search, Long companyId) {

        // handling black list here
        RideLineDaily rideLineDaily = rideLineDailyRepository.findOne(id);
        Driver inboundDriver = rideLineDaily.getInboundDriver();
        Driver ouboundDriver = rideLineDaily.getOutboundDriver();

        return processRides(inboundDriver, ouboundDriver, rideRepository.findAllRidesForRideLineByCompany(id, needTrip, date, companyId));
    }

    public List<Ride> findAllRidesForRideLineByAgency(Long id, Long needTrip, Date date, String search, Long agencyId) {

        // handling black list here
        RideLineDaily rideLineDaily = rideLineDailyRepository.findOne(id);
        Driver inboundDriver = rideLineDaily.getInboundDriver();
        Driver ouboundDriver = rideLineDaily.getOutboundDriver();

        return processRides(inboundDriver, ouboundDriver, rideRepository.findAllRidesForRideLineByAgency(id, needTrip, date, agencyId));
    }

    private List<Ride> processRides(Driver inboundDriver, Driver ouboundDriver, List<Ride> rides) {
        if (rides != null) {
            for (Ride ride : rides) {
                // get user
                User user = ride.getUser();
                if (user != null) {
                    List<BlackListDTO> blacklistDrivers = user.getBlacklistDriver();
                    if (blacklistDrivers != null && blacklistDrivers.size() > 0) {
                        for (BlackListDTO blacklistDriver : blacklistDrivers) {
                            if (inboundDriver != null) {
                                if (blacklistDriver.getId() == inboundDriver.getId()) {
                                    ride.setHasBlacklistDriver(true);
                                    ride.setBlacklistDriverNote(blacklistDriver.getNote());
                                }
                            }

                            if (ouboundDriver != null) {
                                if (blacklistDriver.getId() == ouboundDriver.getId()) {
                                    ride.setHasBlacklistDriver(true);
                                    ride.setBlacklistDriverNote(blacklistDriver.getNote());
                                }
                            }
                        }
                    }
                }
            }
        }

        return rides;
    }

    public List<Ride> generateTripReport(Long needTrip, Date date, Set<Long> drivers, Set<Long> rideLineIds, Long status) {
        List<Ride> rides = null;

        if (!CollectionUtils.isEmpty(drivers)
                && !CollectionUtils.isEmpty(rideLineIds)) {
            rides = rideRepository.generateTripReportForDriverRideLine(needTrip,
                    date,
                    drivers,
                    rideLineIds,
                    status);
        } else if (!CollectionUtils.isEmpty(drivers)
                && CollectionUtils.isEmpty(rideLineIds)) {
            rides = rideRepository.generateTripReportForDriver(needTrip,
                    date,
                    drivers,
                    status);
        } else if (!CollectionUtils.isEmpty(rideLineIds)
                && CollectionUtils.isEmpty(drivers)) {
            rides = rideRepository.generateTripReportForRideLine(needTrip,
                    date,
                    rideLineIds,
                    status);
        } else {
            rides = rideRepository.generateTripReportForDate(needTrip,
                    date,
                    status);

        }
        return rides;
    }

    @Override
    @Transactional
    public void findByDateMaxTrip() {

        // get current day
        DateTime currentDate = new DateTime();

        // get before day
        DateTime startDate = currentDate.minusDays(1);

        // find max trip id in the current  trip analyze table
        Integer currentMaxTripId = tripAnalyzeRepository.getMaxTripId();

        List<Object[]> objects = rideRepository.findByDateMaxTrip(startDate.toDate(), currentDate.toDate(), currentMaxTripId);
        // 0: user_id, 1: pickup, 2: dropoff, 3: ride_line_id,
        // 4: driver_id, 5: user_full_name,
        // 6: trip_count, 7: max_value
        if (objects != null && objects.size() > 0) {
            for (int i = 0; i < objects.size(); i++) {
                Object[] obtainedObject = objects.get(i);
                TripAnalyze tripAnalyze = new TripAnalyze();

                if (obtainedObject[4] != null) {
                    Integer dirverId = (Integer) obtainedObject[4];
                    Driver currentDriver = driverRepository.findOne(Long.valueOf(dirverId));
                    tripAnalyze.setDriver(currentDriver);
                }
                // get user
                Integer userId = (Integer) obtainedObject[0];
                User currentUser = userRepository.findUserById(Long.valueOf(userId.longValue()));
                Integer rideLineId = (Integer) obtainedObject[3];
                RideLine currentRideLine = rideLineRepository.findOne(Long.valueOf(rideLineId.longValue()));

                tripAnalyze.setUser(currentUser);
                tripAnalyze.setRideLine(currentRideLine);
                tripAnalyze.setAnalyzeDate(new Date());
                tripAnalyze.setUserFullName((String) obtainedObject[5]);
                tripAnalyze.setMaxTripId((Integer) obtainedObject[7]);
                tripAnalyze.setPickupZip((String) obtainedObject[1]);
                tripAnalyze.setDropoffZip((String) obtainedObject[2]);
                tripAnalyze.setTripCount((BigInteger) obtainedObject[6]);
                tripAnalyze.setCreatedAt(new Timestamp(currentDate.getMillis()));
                tripAnalyze.setUpdatedAt(new Timestamp(currentDate.getMillis()));

                tripAnalyzeRepository.save(tripAnalyze);
            }
        }
    }

    @Override
    @Transactional
    public byte[] getRideBillingReportAndDownload(Admin permissionAdmin, VisitRequestDTO dto, HttpServletRequest req) {

        RideValidInvalidDTO outputRide = getData(permissionAdmin, dto.getFromDate(), dto.getToDate(), dto.getAgencyIds(), dto.getSeniorIds());

        try {
            File file = reportService.generateRideBillingReport(outputRide, dto.getIsValidVisitor(), req);
            return IOUtils.toByteArray(new FileInputStream(file));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public RideValidInvalidDTO getAuthorizedRiders(Admin permissionAdmin, String startDate, String endDate, List<Long> agencyIds, List<Long> seniorIds,
                                                   Pageable pageable, boolean isInvalid) {
        RideValidInvalidDTO outputRide = getData(permissionAdmin, startDate, endDate, agencyIds, seniorIds);

        if (pageable != null) {
            if (isInvalid) {
                List invalidRides = outputRide.getInvalidRides();
                int start = pageable.getOffset();
                int end = (start + pageable.getPageSize()) > invalidRides.size() ? invalidRides.size() : (start + pageable.getPageSize());
                Page<BillingRideResponseDTO> pages = new PageImpl<BillingRideResponseDTO>(invalidRides.subList(start, end), pageable, invalidRides.size());
                outputRide.setInvalidRidesPage(pages);
            } else {
                List validRides = outputRide.getValidRides();
                int start = pageable.getOffset();
                int end = (start + pageable.getPageSize()) > validRides.size() ? validRides.size() : (start + pageable.getPageSize());
                Page<BillingRideResponseDTO> pages = new PageImpl<BillingRideResponseDTO>(validRides.subList(start, end), pageable, validRides.size());
                outputRide.setValidRidesPage(pages);
            }
        }

        return outputRide;
    }

    private RideValidInvalidDTO getData(Admin permissionAdmin, String startDate, String endDate, List<Long> agencyIds, List<Long> seniorIds) {
        DateTime today = new DateTime(startDate);
        DateTime endDay = new DateTime(endDate);

        List<Ride> riders = new ArrayList<Ride>();
        boolean nonEmptyAgencies = (agencyIds != null && agencyIds.size() > 0 && agencyIds.get(0) != null);
        boolean nonEmptySeniors = (seniorIds != null && !seniorIds.isEmpty());
        if (nonEmptyAgencies || nonEmptySeniors) {
            if(nonEmptyAgencies && nonEmptySeniors){
                riders = rideRepository.findAllRidersAgenciesAndSeniors(today.toDate(), endDay.toDate(), agencyIds, seniorIds);
            } else if(nonEmptyAgencies){
                riders = rideRepository.findAllRidersAgencies(today.toDate(), endDay.toDate(), agencyIds);
            } else if(nonEmptySeniors) {
                riders = rideRepository.findAllRidersBySeniors(today.toDate(), endDay.toDate(), seniorIds);
            }
        } else {
            if (isSuperAdmin(permissionAdmin)) {
                riders = rideRepository.findAllRiders(today.toDate(), endDay.toDate());
            } else if (isCompanyAdmin(permissionAdmin)) {
                AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

                if (adminAgencyCompanyOrganization != null) {

                    riders = rideRepository.findAllRidersByCompany(today.toDate(), endDay.toDate(), adminAgencyCompanyOrganization.getCompany().getId());
                }
            } else if (isAgencyAdmin(permissionAdmin)) {
                AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationRepository.findByAdminId(permissionAdmin.getId());

                if (adminAgencyCompanyOrganization != null && adminAgencyCompanyOrganization.getAgency() != null) {
                    riders = rideRepository.findAllRidersCompanyAgency(today.toDate(), endDay.toDate(), adminAgencyCompanyOrganization.getCompany().getId(), adminAgencyCompanyOrganization.getAgency().getId());
                }
            }
        }


        RideValidInvalidDTO rideValidInvalidDTO = new RideValidInvalidDTO();

        if (riders != null) {
            // convert to map
            LinkedHashMap<Long, ArrayList<Ride>> convertedMap = new LinkedHashMap<Long, ArrayList<Ride>>();

            for (Ride ride : riders) {
                Long userKey = ride.getUser().getId();
                if (convertedMap.containsKey(ride.getUser().getId())) {
                    convertedMap.get(userKey).add(ride);
                } else {
                    ArrayList<Ride> rides = new ArrayList<Ride>();
                    rides.add(ride);
                    convertedMap.put(userKey, rides);
                }
            }

            Iterator<Long> keys = convertedMap.keySet().iterator();
            while (keys.hasNext()) {
                Long key = keys.next();
                ArrayList<Ride> rides = convertedMap.get(key);

                ArrayList<Ride> validedRide = new ArrayList<Ride>();
                // get rides are within authorization start and end
                for (Ride ride : rides) {
                    if (ride.getUser().getAuthorizationStart() != null &&
                            ride.getUser().getAuthorizationEnd() != null) {
                        if ((ride.getDate().after(ride.getUser().getAuthorizationStart())
                                && ride.getDate().before(ride.getUser().getAuthorizationEnd()))
                                || DateUtils.isSameDay(ride.getDate(), ride.getUser().getAuthorizationStart())
                                || DateUtils.isSameDay(ride.getDate(), ride.getUser().getAuthorizationEnd())) {
                            validedRide.add(ride);
                        } else {
                            // INVALID: unauthorized ride
                            rideValidInvalidDTO.getInvalidRides().add(new BillingRideResponseDTO(ride, RideValidationEnum.TYPE3.getValue()));
                        }
                    }
                }
                for (int i = 0; i < validedRide.size(); i++) {
                    Ride ride = validedRide.get(i);
                    FixedStatusEnum fixedStatus = ride.getUser().getFixedStatus();
                    // for FIXED TYPE
                    if (FixedStatusEnum.FIXED.equals(fixedStatus)) {
                        String daysInWeek = ride.getUser().getDaysInWeek();
                        if (daysInWeek != null && daysInWeek.length() > 0) {
                            List<String> splittedDays = Arrays.asList(daysInWeek.split(","));

                            if (splittedDays.contains(String.valueOf(ride.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfWeek().getValue()))) {
                                // VALID: TYPE 1 valid ride
                                rideValidInvalidDTO.getValidRides().add(new BillingRideResponseDTO(ride, RideValidationEnum.TYPE1.getValue()));
                            } else {
                                // INVALID: unauthorized rides
                                rideValidInvalidDTO.getInvalidRides().add(new BillingRideResponseDTO(ride, RideValidationEnum.TYPE3.getValue()));
                            }
                        } else {
                            // INVALID: unauthorized rides
                            rideValidInvalidDTO.getInvalidRides().add(new BillingRideResponseDTO(ride, RideValidationEnum.TYPE3.getValue()));
                        }
                    }
                    // for UNFIXED TYPE
                    else if (FixedStatusEnum.UNFIXED.equals(fixedStatus)) {
                        // day
                        if (UnitTypeEnum.DAY.equals(ride.getUser().getUnitType())) {
                            if (PeriodTypeEnum.ENTIRE_AUTH.equals(ride.getUser().getPeriodType())) {
                                // calculate days from
                                long diff = ride.getDate().getTime() - ride.getUser().getAuthorizationStart().getTime();
                                long diffDays = diff / (24 * 60 * 60 * 1000);
                                if (ride.getUser().getMaximumUnits() == 0 || diffDays < ride.getUser().getMaximumUnits()) {
                                    // TYPE 1: VALID RIDE
                                    rideValidInvalidDTO.getValidRides().add(new BillingRideResponseDTO(ride, RideValidationEnum.TYPE1.getValue()));
                                } else {
                                    // TYPE 2: Invalid visits - Exceed the maximum units
                                    rideValidInvalidDTO.getInvalidRides().add(new BillingRideResponseDTO(ride, RideValidationEnum.TYPE2.getValue()));
                                }
                            }
                        }
                    }
                    // not defined
                    else {
                        rideValidInvalidDTO.getInvalidRides().add(new BillingRideResponseDTO(ride, RideValidationEnum.TYPE4.getValue()));
                    }
                }
            }
        }

        for (BillingRideResponseDTO valid_ride : rideValidInvalidDTO.getValidRides()) {
            Billing billing_info = billingService.findByTripId(valid_ride.getRide().getId());
            if (billing_info != null) {
                valid_ride.setBilling(billing_info);
            } else {
                billing_info = new Billing();
                billing_info.setStatus("");
                valid_ride.setBilling(billing_info);
            }

        }
        return rideValidInvalidDTO;
    }

    @Override
    @Transactional
    public void distributeRideIntoRideLine(List<Long> rideIdes, List<RideLineIdDTO> rideLineIds) {

        // find trip line history based on trip analyze, range: past one week
        DateTime currentTime = new DateTime();
        DateTime pastOneWeek = currentTime.minusDays(7);

        // checking past one week
        List<Object[]> tripHistories = tripAnalyzeRepository.findMaxTripCount(pastOneWeek.toDate(), currentTime.toDate());

        boolean isFounded = false;
        // here: O: user_id, 1: ride_line_id, 2: max_trip_count
        if (tripHistories != null && tripHistories.size() > 0) {
            if (rideLineIds != null && rideLineIds.size() > 0) {
                for (int i = 0; i < rideLineIds.size(); i++) {
                    RideLineIdDTO rideLineIdDTO = rideLineIds.get(i);
                    Long foundRideLineId = rideLineIdDTO.getRideLineId();
                    for (int historyIndex = 0; historyIndex < tripHistories.size(); historyIndex++) {
                        Object[] tripHistory = tripHistories.get(historyIndex);

                        BigInteger maxTripCount = (BigInteger) tripHistory[2];
                        Integer rideLineId = (Integer) tripHistory[1];

                        if (maxTripCount.compareTo(BigInteger.ZERO) > 0) {
                            // founded
                            if (foundRideLineId.compareTo(Long.valueOf(rideLineId.longValue())) == 0) {
                                // get ride
                                if (rideIdes != null && rideIdes.size() > 0) {
                                    for (Long rideId : rideIdes) {
                                        Ride ride = rideRepository.findOne(rideId);
                                        // update ride line id and ride lide daily
                                        RideLine rideLine = rideLineRepository.findOne(foundRideLineId);
                                        RideLineDaily rideLineDaily = rideLineDailyRepository.findOne(rideLineIdDTO.getRideLineDailyId());
                                        ride.setRideLine(rideLine);
                                        ride.setRideLineDaily(rideLineDaily);
                                        // update ride line with new ride line and ride line daily
                                        rideRepository.save(ride);
                                    }
                                }
                                isFounded = true;
                                break;
                            }
                        }
                    }

                    if (isFounded) {
                        break;
                    }
                }
            }

        }

        // checking past one month
        if (!isFounded) {
            DateTime pastOneMonth = currentTime.minusDays(30);
            tripHistories = tripAnalyzeRepository.findMaxTripCount(pastOneMonth.toDate(), currentTime.toDate());
            if (tripHistories != null && tripHistories.size() > 0) {
                if (rideLineIds != null && rideLineIds.size() > 0) {
                    for (int i = 0; i < rideLineIds.size(); i++) {
                        RideLineIdDTO rideLineIdDTO = rideLineIds.get(i);
                        Long foundRideLineId = rideLineIdDTO.getRideLineId();
                        for (int historyIndex = 0; historyIndex < tripHistories.size(); historyIndex++) {
                            Object[] tripHistory = tripHistories.get(historyIndex);

                            BigInteger maxTripCount = (BigInteger) tripHistory[2];
                            Integer rideLineId = (Integer) tripHistory[1];

                            if (maxTripCount.compareTo(BigInteger.ZERO) > 0) {
                                // founded
                                if (foundRideLineId.compareTo(Long.valueOf(rideLineId.longValue())) == 0) {
                                    // get ride
                                    if (rideIdes != null && rideIdes.size() > 0) {
                                        for (Long rideId : rideIdes) {
                                            Ride ride = rideRepository.findOne(rideId);
                                            // update ride line id and ride lide daily
                                            ride.setRideLine(rideLineRepository.findOne(foundRideLineId));
                                            ride.setRideLineDaily(rideLineDailyRepository.findOne(rideLineIdDTO.getRideLineDailyId()));
                                            // update ride line with new ride line and ride line daily
                                            rideRepository.save(ride);
                                        }
                                    }
                                    isFounded = true;
                                    break;
                                }
                            }
                        }

                        if (isFounded) {
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public Page<Ride> getRideByCompanyAndAgency(Long agencyId, Long companyId, Pageable pageable) {
        List<RideLine> rideLineList = rideLineRepository.getDistRideLineForRides(agencyId, companyId);
        return (rideRepository.findByRideLineIn(rideLineList, pageable));
    }

    @Override
    public Page<Ride> rideFilter(Long agencyId, Long companyId, Long userId, Long driverId, Date fromDate, Date toDate, Pageable pageable) {
    	fromDate.setHours(0);
    	fromDate.setMinutes(0);
    	fromDate.setSeconds(0);
    	toDate.setHours(23);
    	toDate.setMinutes(59);
    	toDate.setSeconds(59);
        List<Long> rideLineList = new ArrayList<>();
        if (driverId == null) {
            rideLineList = rideLineRepository.getIdsDistRideLineForRides(agencyId, companyId);
        } else {
            rideLineList = rideLineRepository.getIdsRideLineByCompanyAndAgencyAndDriver(agencyId, companyId, driverId);
        }
        if (rideLineList.isEmpty()) {
            return rideRepository.findByRideLineIn(new ArrayList<>(), pageable);
        }
        if (userId == null) {
            return rideRepository.findAllRideLinesUserIdDates(rideLineList, fromDate, toDate, pageable);
        } else {
            return rideRepository.findAllRideLinesUserIdDates(rideLineList, userId, fromDate, toDate, pageable);
        }
    }

    @Override
    public Page<Ride> findByCompany(Long companyId, Pageable pageable) {
        List<RideLine> rideLines = rideLineRepository.findByCompany(companyRepository.findOne(companyId));
        Page<Ride> rideList = rideRepository.findByRideLineIn(rideLines, pageable);
        return rideList;
    }

}
