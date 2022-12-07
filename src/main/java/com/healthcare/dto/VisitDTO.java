package com.healthcare.dto;

import com.healthcare.model.entity.*;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by Hitesh on 10/3/2017.
 */
@Data
public class VisitDTO {

    private Long id;

    private String agency;

    private Timestamp checkInTime;

    private String selectedBreakfast;
    private String selectedLunch;
    private String selectedDinner;

    private String selectedTable;

    private String selectedSeat;

    private Timestamp checkOutTime;

    private String userComments;

    private String notes;

    private String status;

    private String userName;

    private String firstName;

    private String lastName;

    private Long userId;

    private  Long mealId;
    
    private String billingCode;
    
    private Double expectedMoney;
    
    private Double actualMoney;

    public VisitDTO() {}
    public VisitDTO(Visit visit){
        this.id = visit.getId();
        this.agency = visit.getAgency().getName();
        this.checkInTime = visit.getCheckInTime();
        this.selectedBreakfast = visit.getSelectedBreakfast() == null ? null : visit.getSelectedBreakfast().getName();
        this.selectedLunch = visit.getSelectedLunch() == null ? null : visit.getSelectedLunch().getName();
        this.selectedDinner = visit.getSelectedDinner() == null ? null : visit.getSelectedDinner().getName();
        this.selectedSeat = visit.getSelectedSeat();
        this.checkOutTime = visit.getCheckOutTime();
        this.userComments = visit.getUserComments();
        this.notes = visit.getNotes();
        this.status = visit.getStatus();
        this.userName = visit.getUser().getUsername();
        this.firstName = visit.getUser().getFirstName();
        this.lastName = visit.getUser().getLastName();
        this.userId = visit.getUser().getId();
        this.mealId = visit.getSelectedLunch() == null ? null : visit.getSelectedLunch().getId();
        this.billingCode = visit.getBillingCode() == null ? null : visit.getBillingCode();
        this.expectedMoney = visit.getExpectedMoney();
        this.actualMoney = visit.getActualMoney();
    }

}
