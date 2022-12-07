package com.healthcare.api;

import com.healthcare.model.entity.RideLineColor;
import com.healthcare.repository.RideLineColorRepository;
import com.healthcare.repository.RideLineRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/rideLineColor")
public class RideLineColorController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RideLineColorRepository rideLineColorRepository;

    @ApiOperation(value = "get all rideLineColor", notes = "get all rideLineColor")
    @GetMapping()
    public ResponseEntity<List<RideLineColor>> readAll() {
        return ResponseEntity.ok(rideLineColorRepository.getRideLineColor());
    }

}
