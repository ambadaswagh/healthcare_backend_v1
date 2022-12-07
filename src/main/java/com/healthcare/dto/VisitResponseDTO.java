package com.healthcare.dto;

import com.healthcare.model.entity.Billing;
import com.healthcare.model.entity.Visit;
import lombok.Data;

@Data
public class VisitResponseDTO extends VisitDTO {
    private String reason;
    private String medicalId;
    private String insuranceCompany;
    private String authorizationCode;
    private String checkInSignature;
    private String checkOutSignature;
    private Billing billing;

    public VisitResponseDTO() {
    }

    public VisitResponseDTO(Visit visit, String reason) {
        super(visit);
        this.reason = reason;
        this.medicalId = visit.getUser().getMedicaIdNumber();
        this.authorizationCode = visit.getUser().getAuthorizationCode();
        if (visit.getUser().getMedication() != null) {
            if (visit.getUser().getMedication().getInsurance() != null) {
                this.insuranceCompany = visit.getUser().getMedication().getInsurance().getName();
            }
        }
        if (visit.getCheckInSignature() != null) {
            this.checkInSignature = visit.getCheckInSignature().getFileUrl();

        }
        if (visit.getCheckOutSignature() != null) {
            this.checkOutSignature = visit.getCheckOutSignature().getFileUrl();
        }
    }
}
