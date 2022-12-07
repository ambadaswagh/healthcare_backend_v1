package com.healthcare.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RideLineIdDTO implements Serializable {

    private Long rideLineId;
    private Long rideLineDailyId;

    public RideLineIdDTO() {}

    public RideLineIdDTO(Long rideLineId, Long rideLineDailyId) {
        this.rideLineDailyId = rideLineDailyId;
        this.rideLineId = rideLineId;
    }
}
