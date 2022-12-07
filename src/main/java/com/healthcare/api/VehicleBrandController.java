package com.healthcare.api;

import com.healthcare.api.auth.AbstractBasedAPI;
import com.healthcare.model.entity.VehicleBrand;
import com.healthcare.service.VehicleBrandService;
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
@RequestMapping(value = "/api/vehicleBrand")
public class VehicleBrandController extends AbstractBasedAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private VehicleBrandService vehicleBrandService;

    @ApiOperation(value = "get all VehicleBrands", notes = "get all VehicleBrands")
    @ApiParam(name = "id", value = "VehicleBrand id", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @GetMapping
    public List<VehicleBrand> readAll() {
        return vehicleBrandService.findAll();
    }

    @ApiOperation(value = "get vehicleBrand by Id", notes = "get vehicleBrand by Id")
    @ApiParam(name = "id", value = "VehicleBrand id", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/{id}")
    public VehicleBrand getVehicleBrandById(@PathVariable("id") Long id) {
        return vehicleBrandService.findById(id);
    }

}
