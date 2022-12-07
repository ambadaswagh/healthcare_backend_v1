package com.healthcare.api;


import com.healthcare.exception.UserException;
import com.healthcare.model.entity.Ride;
import com.healthcare.model.entity.RideLine;
import com.healthcare.model.entity.RideLineDaily;
import com.healthcare.model.entity.RideLineHasDriver;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.repository.*;
import com.healthcare.service.RideLineDailyService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/rideLineDaily")
public class RideLineDailyController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RideLineDailyService rideLineDailyService;

    @Autowired
    private RideLineHasDriverRepository rideLineHasDriverRepository;

    @Autowired
    private RideLineDailyRepository rideLineDailyRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private AgencyRepository agencyRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private RideLineRepository rideLineRepository;
    @ApiOperation(value = "save rideLineDaily", notes = "save rideLineDaily")
    @ApiParam(name = "rideLine", value = "rideLine to save", required = true)
    @PostMapping()
    public ResponseEntity<RideLineDaily> create(@RequestBody RideLineDaily rideLineDaily) {
        RideLine rideLine = rideLineRepository.findOne(rideLineDaily.getRideLine().getId());
        rideLineDaily.setName(rideLine.getName());
        rideLineDaily.setBorough(rideLine.getBorough());
        rideLineDaily.setNote(rideLine.getNote());
        rideLineDaily.setStatus(new Long(1));
        rideLineDaily.setDate(DateUtils.truncate(rideLineDaily.getDate(), Calendar.DAY_OF_MONTH));
        rideLineDaily.setRideLineDailyColor(rideLine.getRideLineColor());
        rideLineDaily.setVehicle(rideLine.getVehicle());

        int day = rideLineDaily.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfWeek().getValue();
        List<RideLineHasDriver> rideLineHasDrivers = rideLineHasDriverRepository.getRideLineHasDriverForRideLineOnDay(rideLineDaily.getRideLine().getId(), new Long(day));
        if(rideLineHasDrivers.size() > 0) {
            for(RideLineHasDriver rideLineHasDriver : rideLineHasDrivers) {
                if(rideLineHasDriver.getNeedTrip().intValue() == 1) {
                    rideLineDaily.setInboundDriver(rideLineHasDriver.getDriver());
                } else if(rideLineHasDriver.getNeedTrip().intValue() == 2) {
                    rideLineDaily.setOutboundDriver(rideLineHasDriver.getDriver());
                }
            }
        }


        rideLineDaily = rideLineDailyService.save(rideLineDaily);
        return new ResponseEntity<RideLineDaily>(rideLineDaily, HttpStatus.OK);
    }

    @ApiOperation(value = "get rideLineDaily by id", notes = "get rideLineDaily by id")
    @ApiImplicitParam(name = "id", value = "rideLineDaily id", required = true, dataType = "Long" ,paramType = "path")
    @GetMapping("/{id}")
    public RideLineDaily read(@PathVariable Long id) {
        logger.info("id : " + id);
        return rideLineDailyService.findById(id);
    }

    @ApiOperation(value = "get all rideLineDaily", notes = "get all rideLineDaily")
    @GetMapping("/getAllRideLineDaily")
    public ResponseEntity<Page<RideLineDaily>> readAll(@RequestParam MultiValueMap<String, String> attributes) {
        MultiValueMapConverter<RideLineDaily> converter = new MultiValueMapConverter<>(attributes, RideLineDaily.class);
        return ResponseEntity.ok(rideLineDailyService.findAll(converter.getData(), converter.getPageable()));
    }

    @PostMapping("/updateStatus/{id}")
    public ResponseEntity<Integer> updateStatus(@RequestParam(value = "status") Long status, @PathVariable Long id) {
        return ResponseEntity.ok(rideLineDailyService.updateStatus(id, status));
    }

    @GetMapping("/findAllRideDailies")
    public List<RideLineDaily> findAllRideDailies(@RequestParam(value = "date", required = false) String dateInString,
                                                  @RequestParam(value = "agencyId") Long agencyId,
                                                  @RequestParam(value = "companyId") Long companyId){
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        if(!StringUtils.isEmpty(dateInString)) {
            try {
                date = formatter.parse(dateInString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return rideLineDailyService.findAllRidesForRideLineDaily(date, agencyId, companyId);
    }

    @Transactional
    @PostMapping("/updateVehicleForRideLineDaily/{id}")
    public ResponseEntity<Integer> updateVehicleForRideLineDaily(@RequestParam(value = "vehicleId") Long vehicleId, @PathVariable Long id) {
        return ResponseEntity.ok(rideLineDailyRepository.updateVehicleForRideLineDaily(id, vehicleRepository.findOne(vehicleId)));
    }

    @Transactional
    @PostMapping("/updateDriverForRideLineDaily/{id}")
    public ResponseEntity<Integer> updateDriverForRideLineDaily(@RequestParam(value = "driverId") Long driverId,
                                                                @RequestParam(value = "tripType") Long tripType,
                                                                @PathVariable Long id) {
        if(tripType.intValue() == 1) {
            rideLineDailyRepository.updateInboundDriverForRideLineDaily(id, driverRepository.findOne(driverId));
        } else if(tripType.intValue() == 2) {
            rideLineDailyRepository.updateOutboundDriverForRideLineDaily(id, driverRepository.findOne(driverId));
        }
        return ResponseEntity.ok(1);
    }


    @Transactional
    @GetMapping("/getRideLineDailyByRideLine/{id}")
    public ResponseEntity<List<RideLineDaily>> getRideLineDailyByRideLineAndBound(@RequestParam(value = "bound") Long bound, @PathVariable Long id) {
      if(id == null || bound == null){
        throw  new UserException("rideLine and bound can't empty");
      }
      return new ResponseEntity<>(rideLineDailyService.getRideLineDailyByRideLine(id, bound), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete rideLineDaily", notes = "Delete rideLineDaily")
    @ApiImplicitParam(name = "id", value = "rideLineDaily Id", required = true, dataType = "Long" ,paramType = "path")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long rideLineDailyId, HttpServletResponse response) {
        if (rideLineDailyId == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }

        List<Ride> rideList = rideRepository.findByRideLineDailyId(rideLineDailyId);
        if(rideList.size() > 0) {
            for (Ride rides : rideList) {
                rides.setRideLineDaily(null);
                rides.setRideLine(null);
                rideRepository.save(rides);
            }
        }
        rideLineDailyService.deleteById(rideLineDailyId);
    }


}
