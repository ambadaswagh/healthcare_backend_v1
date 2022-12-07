package com.healthcare.dto;

import com.healthcare.model.entity.Billing;
import com.healthcare.model.entity.Ride;
import com.healthcare.model.entity.Visit;
import lombok.Data;

@Data
public class BillingRideResponseDTO {
    private Ride ride;
    private Billing billing;
    private String reason;
    private String medicalId;
    private String insuranceCompany;
    private String authorizationCode;

    public BillingRideResponseDTO() {}

    public BillingRideResponseDTO(Ride ride, String reason) {
        this.ride = ride;
        this.reason = reason;
        this.medicalId = ride.getUser().getMedicaIdNumber();
        this.authorizationCode = ride.getUser().getAuthorizationCode();
        if (ride.getUser().getMedication() != null) {
            if (ride.getUser().getMedication().getInsurance() != null) {
                this.insuranceCompany = ride.getUser().getMedication().getInsurance().getName();
            }
        }
    }
}
