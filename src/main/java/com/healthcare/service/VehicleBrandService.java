package com.healthcare.service;

import com.healthcare.model.entity.VehicleBrand;

import java.util.List;

public interface VehicleBrandService extends IService<VehicleBrand>, IFinder<VehicleBrand> {
    List<VehicleBrand> findAll();
}
