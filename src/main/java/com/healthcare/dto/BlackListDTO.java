package com.healthcare.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BlackListDTO implements Serializable {
    private Long   id;
    private String type;
    private String firstName;
    private String lastName;
    private String phone;
    private String note;
}
