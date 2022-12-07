package com.healthcare.dto;

import com.healthcare.model.enums.SeniorStatsEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Hitesh on 9/28/2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VisitSeniorStatsReqDTO extends BasicReportFilterDTO {
    SeniorStatsEnum seniorStatsEnum;
}
