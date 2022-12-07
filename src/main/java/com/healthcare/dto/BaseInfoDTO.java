package com.healthcare.dto;

import lombok.Data;

import javax.persistence.Transient;

/**
 * Created by DuckUser on 10/3/2017.
 */
@Data
public class BaseInfoDTO {

    @Transient
    private Long companyId;

    @Transient
    private Long agencyId;

    @Transient
    private Long userId;

    @Transient
    private String companyName;

    @Transient
    private String agencyName;

    @Transient
    private String username;

    @Transient
    private String firstName;

    @Transient
    private String lastName;
}
