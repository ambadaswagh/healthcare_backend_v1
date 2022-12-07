package com.healthcare.dto;

import lombok.Data;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;
import java.util.List;

@Data
public class ReminderDTO implements Serializable {
    private Integer page;
    private Integer size;
    private Long companyId;
    private Long agencyId;


    public ReminderDTO fromMap(MultiValueMap<String, String> attributes) {
        List<String> pages = attributes.get("page");
        List<String> sizes = attributes.get("size");
        List<String> companyIds = attributes.get("companyId");
        List<String> agencyIds  = attributes.get("agencyId");

        this.page = pages != null ? Integer.valueOf(pages.get(0)) : null;
        this.size = sizes != null ? Integer.valueOf(sizes.get(0)) : null;
        this.companyId = companyIds != null ? Long.valueOf(companyIds.get(0)) : null;
        this.agencyId = agencyIds != null ? Long.valueOf(agencyIds.get(0)) : null;

        return this;
    }
}
