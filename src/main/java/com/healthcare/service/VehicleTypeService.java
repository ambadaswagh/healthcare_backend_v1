package com.healthcare.service;

import com.healthcare.model.entity.VehicleType;

import java.util.List;

public interface VehicleTypeService extends IService<VehicleType>, IFinder<VehicleType> {
    List<VehicleType> findAll();
}
