package com.healthcare.dto;

import lombok.Data;

import java.util.Date;

/**
 * Created by Hitesh on 9/28/2017.
 */
@Data
public class TimeFilterDTO {
    Date startDate;
    Date endDate;
}
