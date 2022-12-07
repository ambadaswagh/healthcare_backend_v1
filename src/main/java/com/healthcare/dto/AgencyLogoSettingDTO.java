package com.healthcare.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AgencyLogoSettingDTO implements Serializable {
    private Long   agencyId;
    private Long   logoId;
    private String mainWhiteLabel;
    private String upperLeftLabel;
}
