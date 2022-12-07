package com.healthcare.dto;

import com.healthcare.model.entity.HealthInsuranceClaim;
import com.healthcare.model.entity.Review;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hitesh on 10/3/2017.
 */
@Data
public class AssessmentReportResponseDTO {

    private List<ActivityDTO> activityDTOS = new ArrayList<ActivityDTO>();
    private List<ServicePlanDTO> servicePlanDTOS = new ArrayList<ServicePlanDTO>();
    private List<Review> reviews = new ArrayList <Review>();
}
