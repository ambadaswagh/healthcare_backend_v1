package com.healthcare.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MealOrderDTO implements Serializable {

    private Long companyId;
    private String pin;
    private Long selectedBreakfastId;
    private Long selectedDinnerId;
    private Long selectedLunchId;
}
