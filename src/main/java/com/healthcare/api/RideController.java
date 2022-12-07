package com.healthcare.api;


import com.healthcare.api.common.HealthcareUtil;
import com.healthcare.dto.RideMovingRequestDTO;
import com.healthcare.dto.RideValidInvalidDTO;
import com.healthcare.dto.VisitRequestDTO;
import com.healthcare.exception.UserException;
import com.healthcare.model.entity.*;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.repository.RideLineDailyRepository;
import com.healthcare.repository.RideLineRepository;
import com.healthcare.repository.RideRepository;
import com.healthcare.service.AdminAgencyCompanyOrganizationService;
import com.healthcare.service.RideService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.Set;

import static com.healthcare.api.common.HealthcareConstants.AUTHENTICATED_ADMIN;
import static com.healthcare.api.common.HealthcareConstants.NOT_AUTHORIZED;
import static com.healthcare.api.common.HealthcareUtil.isEmpty;
import static com.healthcare.api.common.RoleUtil.isAgencyAdmin;
import static com.healthcare.api.common.RoleUtil.isCompanyAdmin;
import static com.healthcare.api.common.RoleUtil.isSuperAdmin;

@RestController
@RequestMapping(value = "/api/ride")
public class RideController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RideService rideService;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private RideLineDailyRepository rideLineDailyRepository;

    @Autowired
    private RideLineRepository rideLineRepository;

    @Autowired
    AdminAgencyCompanyOrganizationService adminAgencyCompanyOrganizationService;

    @ApiOperation(value = "save ride", notes = "save ride")
    @ApiParam(name = "ride", value = "ride to save", required = true)
    @PostMapping()
    public ResponseEntity<Ride> create(@RequestBody Ride ride) {
        ride.setRideLineDaily(rideLineDailyRepository.findOne(ride.getRideLineDaily().getId()));
        ride.setRideLine(ride.getRideLineDaily().getRideLine());
        //ride.setDate(ride.getRideLineDaily().getDate());
        ride.setStatus(new Long(1));
        ride.setRideColor(ride.getRideLine().getRideLineColor());

        if(ride.getId() == null){
        if((rideRepository.findRideIfExists(ride.getDate(), ride.getRideLine(), ride.getNeedTrip(), ride.getUser().getId()).size() )> 0) {
            throw new UserException("Ride already Exists");
        }}
        if(ride.getId() != null){
          if(rideRepository.countByDateAndRideLineAndNeedTripAndUserAndIdNotIn(ride.getDate(), ride.getRideLine(), ride.getNeedTrip(), ride.getUser(),ride.getId()) > 0){
            throw new UserException("Ride already Exists");
          }
        }

        ride = rideService.save(ride);
        return new ResponseEntity<Ride>(ride, HttpStatus.OK);
    }

    @ApiOperation(value = "get ride by id", notes = "get ride by id")
    @ApiImplicitParam(name = "id", value = "ride id", required = true, dataType = "Long" ,paramType = "path")
    @GetMapping("/{id}")
    public Ride read(@PathVariable Long id) {
        logger.info("id : " + id);
        return rideService.findById(id);
    }

    @ApiOperation(value = "get all riedLine", notes = "get all riedLine")
    @GetMapping()
    public ResponseEntity<Page<Ride>> readAll(@RequestParam MultiValueMap<String, String> attributes) {
        MultiValueMapConverter<Ride> converter = new MultiValueMapConverter<>(attributes, Ride.class);
        return ResponseEntity.ok(rideService.findAll(converter.getData(), converter.getPageable()));
    }

    @GetMapping("/findAllRidesForRideLine/{id}")
    public List<Ride> findAllRidesForRideLine(HttpServletRequest req,
                                              @PathVariable Long id,
                                              @RequestParam(value = "needTrip", required = false) Long needTrip,
                                              @RequestParam(value = "search", required = false) String search,
                                              @RequestParam(value = "date", required = false) String dateInString){

        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);

        if(isEmpty(admin)){
            throw new UserException(NOT_AUTHORIZED);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        if(!StringUtils.isEmpty(dateInString)) {
            try {
                date = formatter.parse(dateInString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if(isSuperAdmin(admin)){
            return rideService.findAllRidesForRideLine(id, needTrip, date, search);
        } else if (isCompanyAdmin(admin)) {
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

            if (adminAgencyCompanyOrganization != null) {
                return rideService.findAllRidesForRideLineByCompany(id, needTrip, date, search, adminAgencyCompanyOrganization.getCompany().getId());
            }

        } else if (isAgencyAdmin(admin)) {
            AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.findByAdminId(admin.getId());

            if (adminAgencyCompanyOrganization != null) {
                return rideService.findAllRidesForRideLineByAgency(id, needTrip, date, search, adminAgencyCompanyOrganization.getAgency().getId());
            }
        }

        return new ArrayList<Ride>();
    }

    @GetMapping("/generateTripReport")
    public List<Ride> generateTripReport(@RequestParam(value = "needTrip", required = false) Long needTrip,
                                         @RequestParam(value = "date", required = false) String dateInString,
                                         @RequestParam(value = "driveridlist", required = false) Set<Long> drivers,
                                         @RequestParam(value = "ridelineidlist", required = false) Set<Long> rideLineIds,
                                         @RequestParam(value = "status", required = false) Long status){
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        if(!StringUtils.isEmpty(dateInString)) {
            try {
                date = formatter.parse(dateInString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return rideService.generateTripReport(needTrip, date, drivers, rideLineIds, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> delete(@PathVariable("id") Long id) {
        logger.info("id : " + id);
        rideService.deleteById(id);
        return ResponseEntity.ok(1);
    }

    @ApiOperation(value = "Ride moving decision", notes = "Ride moving decision")
    @PostMapping(value = "/distribution")
    public ResponseEntity findAllByTimeRange(@RequestBody RideMovingRequestDTO dto) {

        rideService.distributeRideIntoRideLine(dto.getRideIds(), dto.getRideLineIds());
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @ApiOperation(value = "get all trips by range time", notes = "get all trips by range time")
    @ApiImplicitParam(name = "startDate", value = "start date and format", required = true, dataType = "Long", paramType = "path")
    @RequestMapping(value = "/range", method = RequestMethod.POST)
    public RideValidInvalidDTO findAllByTimeRange(HttpServletRequest req,
                                                  @RequestBody VisitRequestDTO dto,
                                                  @RequestParam MultiValueMap<String, String> attributes) {

        MultiValueMapConverter<Ride> converter = new MultiValueMapConverter<>(attributes, Ride.class);

        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
        if (HealthcareUtil.isEmpty(admin)){
            throw new UserException(NOT_AUTHORIZED);
        }

//        VisitRequestDTO visitRequestDTO = new VisitRequestDTO().fromMap(attributes);
        RideValidInvalidDTO rideValidInvalidDTO = rideService.getAuthorizedRiders(admin, dto.getFromDate(), dto.getToDate(), dto.getAgencyIds(), dto.getSeniorIds(),
                                                                                        converter.getPageable(), dto.isInvalid());
        return rideValidInvalidDTO;
    }

    @ApiOperation(value = "get all trips by range time", notes = "get all trips by range time")
    @ApiImplicitParam(name = "startDate", value = "start date and format", required = true, dataType = "Long", paramType = "path")
    @GetMapping(value = "/billing-report-download")
    public ResponseEntity<byte[]> downloadRideReport(HttpServletRequest req,
                                                  VisitRequestDTO visitRequestDTO) {

        Admin admin = (Admin) req.getAttribute(AUTHENTICATED_ADMIN);
        if (HealthcareUtil.isEmpty(admin)){
            throw new UserException(NOT_AUTHORIZED);
        }

        if (visitRequestDTO != null) {
            if (visitRequestDTO.getIsValidVisitor() == null) {
                visitRequestDTO.setIsValidVisitor(0);
            }
        byte[] data = rideService.getRideBillingReportAndDownload(admin, visitRequestDTO, req);
            return ResponseEntity.ok().contentLength(data.length)
                    .contentType(MediaType
                            .parseMediaType("application/pdf"))
                    .body(data);
        }
        return null;
    }


    @ApiOperation(value = "get rides by company and angency", notes = "get rides by company and angency")
    @GetMapping(value = "/getRideByCompanyAndAgency")
    public ResponseEntity<Page<Ride>> getRideByCompanyAndAgency(@RequestParam(value = "agencyId") Long agencyId,
                                                         @RequestParam(value = "companyId") Long companyId, @RequestParam MultiValueMap<String, String> attributes) {

      if(agencyId == null || companyId == null){
        throw  new UserException("agency and company can't empty");
      }
      MultiValueMapConverter<Ride> converter = new MultiValueMapConverter<>(attributes, Ride.class);
      return new ResponseEntity<>(rideService.getRideByCompanyAndAgency(agencyId,companyId,converter.getPageable()), HttpStatus.OK);
    }

    @ApiOperation(value = "get rides by filter", notes = "get rides by filter")
    @GetMapping(value = "/rideFilter")
    public ResponseEntity<Page<Ride>> rideFilter(@RequestParam(value = "agencyId") Long agencyId,
                                                 @RequestParam(value = "companyId") Long companyId,
                                                 @RequestParam(value = "userId", required = false) Long userId,
                                                 @RequestParam(value = "driverId", required = false) Long driverId,
                                                 @RequestParam(value = "fromDate", required = false) String fromDateInString,
                                                 @RequestParam(value = "toDate", required = false) String toDateInString , @RequestParam MultiValueMap<String, String> attributes){

      MultiValueMapConverter<Ride> converter = new MultiValueMapConverter<>(attributes, Ride.class);
      Date fromDate = null;
      Date toDate = null;
      if(fromDateInString != null && toDateInString != null){
        try {
          SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
          fromDate=formatter.parse(fromDateInString);
          toDate=formatter.parse(toDateInString);
        } catch (ParseException e) {
          throw new UserException("Invalid date format");
        }
      } else {
    	  fromDate = new Date();
    	  toDate = new Date();
      }
      if(agencyId == null || companyId == null){
        throw  new UserException("agency and company can't empty");
      }
      return new ResponseEntity<>(rideService.rideFilter(agencyId, companyId, userId, driverId, fromDate, toDate, converter.getPageable()), HttpStatus.OK);
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<Page<Ride>> getByComapny(@PathVariable("id") Long id, @RequestParam MultiValueMap<String, String> attributes) {
      logger.info("id : " + id);
      MultiValueMapConverter<Ride> converter = new MultiValueMapConverter<>(attributes, Ride.class);
      if(id == null){
        throw new UserException("Company can't be empty");
      }
      return new ResponseEntity<>(rideService.findByCompany(id , converter.getPageable() ),HttpStatus.OK);
    }
}
