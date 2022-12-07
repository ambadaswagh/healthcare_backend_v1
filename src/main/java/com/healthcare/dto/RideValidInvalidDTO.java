package com.healthcare.dto;

import com.healthcare.model.entity.Ride;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Data
public class RideValidInvalidDTO {
    private List<BillingRideResponseDTO> validRides = new ArrayList<BillingRideResponseDTO>();
    private List<BillingRideResponseDTO> invalidRides = new ArrayList<BillingRideResponseDTO>();

    private Page<BillingRideResponseDTO> validRidesPage = null;
    private Page<BillingRideResponseDTO> invalidRidesPage = null;

    public RideValidInvalidDTO(){}

}
