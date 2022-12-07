package com.healthcare.dto;

import com.healthcare.model.entity.HealthInsuranceClaim;
import com.healthcare.model.entity.Report;
import com.healthcare.model.entity.Visit;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hitesh on 10/3/2017.
 */
@Data
public class BillingReportResponseDTO {

    private ReportDTO report;

    private List<VisitDTO> visitList = new ArrayList<VisitDTO>();

    private List<HealthInsuranceClaim> healthInsuranceClaimList = new ArrayList <HealthInsuranceClaim>();
}
