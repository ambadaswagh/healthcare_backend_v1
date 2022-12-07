package com.healthcare.dto;

import lombok.Data;

/**
 * Created by ABE on 10/27/2017.
 */
@Data
public class AgencyStatsDTO {

    private long totalActiveSeniors;
    private long totalCheckedInSeniors;
    private long totalActiveAndRegistered;
}