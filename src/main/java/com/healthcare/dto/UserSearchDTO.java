package com.healthcare.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.healthcare.model.entity.*;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by DuckUser on 10/15/2017.
 */
@Data
public class UserSearchDTO {

    private String username;
    private String firstName;
    private String lastName;
    private String socialSecurityNumber;
    private String email;
    private String phone;
    private String emergencyContactPhone;
    private String medicareNumber;
    private String familyDoctor;
    private String familyDoctorTel;
    private String pin;
}
