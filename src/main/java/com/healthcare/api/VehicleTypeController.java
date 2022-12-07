package com.healthcare.api;

import com.healthcare.api.auth.AbstractBasedAPI;
import com.healthcare.model.entity.VehicleType;
import com.healthcare.service.VehicleTypeService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api/vehicleType")
public class VehicleTypeController extends AbstractBasedAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private VehicleTypeService vehicleTypeService;

    @ApiOperation(value = "get all VehicleTypes", notes = "get all VehicleTypes")
    @ApiParam(name = "id", value = "VehicleType id", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @GetMapping
    public List<VehicleType> readAll() {
        return vehicleTypeService.findAll();
    }

    @ApiOperation(value = "get vehicleType by Id", notes = "get vehicleType by Id")
    @ApiParam(name = "id", value = "VehicleType id", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/{id}")
    public VehicleType getVehicleTypeById(@PathVariable("id") Long id) {
        return vehicleTypeService.findById(id);
    }

}
