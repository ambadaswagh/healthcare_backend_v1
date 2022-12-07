package com.healthcare.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class RideMovingRequestDTO implements Serializable {
    private List<Long> rideIds = new ArrayList<Long>();
    private List<RideLineIdDTO> rideLineIds = new ArrayList<RideLineIdDTO>();

    public RideMovingRequestDTO() {}

    public RideMovingRequestDTO(List<Long> rideIds, List<RideLineIdDTO> rideLineIds) {
        this.rideIds = rideIds;
        this.rideLineIds = rideLineIds;
    }

}



