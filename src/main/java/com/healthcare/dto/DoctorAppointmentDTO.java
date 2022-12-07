package com.healthcare.dto;

import lombok.Data;

public @Data class DoctorAppointmentDTO extends PagingDTO{

    private Integer userId;
    //format YYYY-MM-DD
    private String fromDate;
    //format YYYY-MM-DD
    private String toDate;
    private String doctorTel;
    private String doctorFax;
    private String doctorName;
    private Integer status;
}
