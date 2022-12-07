package com.healthcare.dto;

import com.healthcare.model.entity.Visit;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hitesh on 9/28/2017.
 */
@Data
public class VisitSeniorStatsResDTO {

    // Register [status = 0] and active [status = 1]
    long allSeniors;

    // Only active [status = 1]
    long activeSeniors;

    // Only left [status =2]
    long leftSeniors;

    // Register seniors
    long notStartedSeniors;

    //Retentions rate = All Active Seniors/All Seniors
    double retentionRate;
    long completedCheckInOut ;
    long onlyCheckIn ;

    List<VisitDTO> visits = new ArrayList<VisitDTO>();
}

