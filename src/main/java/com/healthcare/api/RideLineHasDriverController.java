package com.healthcare.api;

import com.healthcare.model.entity.RideLineHasDriver;
import com.healthcare.repository.RideLineHasDriverRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/rideLineHasDriver")
public class RideLineHasDriverController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RideLineHasDriverRepository rideLineHasDriverRepository;

    @ApiOperation(value = "save rideLineHasDriver", notes = "save rideLineHasDriver")
    @ApiParam(name = "rideLineHasDriver", value = "rideLineHasDriver to save", required = true)
    @PostMapping()
    public ResponseEntity<RideLineHasDriver> create(@RequestBody RideLineHasDriver rideLineHasDriver) {
        rideLineHasDriver = rideLineHasDriverRepository.save(rideLineHasDriver);
        return new ResponseEntity<RideLineHasDriver>(rideLineHasDriver, HttpStatus.OK);
    }

    @ApiOperation(value = "get rideLineHasDriver by id", notes = "get rideLineHasDriver by id")
    @ApiImplicitParam(name = "id", value = "rideLine id", required = true, dataType = "Long" ,paramType = "path")
    @GetMapping("/{id}")
    public RideLineHasDriver read(@PathVariable Long id) {
        logger.info("id : " + id);
        return rideLineHasDriverRepository.findOne(id);
    }
}
