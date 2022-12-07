package com.healthcare.dto;

import lombok.Data;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class VisitRequestDTO {
    private String  fromDate;
    private String toDate;
    private String statFilter;
    private boolean invalid;
    private List<Long> agencyIds;
    private List<Long> seniorIds;
    private Integer isValidVisitor;

    private static DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");

    public VisitRequestDTO fromMap(MultiValueMap<String, String> attributes) {
        List<String> fromDate = attributes.get("fromDate");
        List<String> toDate = attributes.get("toDate");
        List<String> statFilter = attributes.get("statFilter");
        List<String> agencyIds  = attributes.get("agencyIds");
        List<String> seniorIds  = attributes.get("seniorIds");

        this.fromDate = fromDate != null ? fromDate.get(0) : null;
        this.toDate = toDate != null ? toDate.get(0): null;
        this.statFilter = statFilter != null ? statFilter.get(0): null;

        if (agencyIds != null && agencyIds.size() > 0) {
            for (String str : agencyIds) {
                if (str != null && !str.isEmpty() && str.matches("[0-9]*")) {
                    if (this.agencyIds == null) {
                        this.agencyIds = new ArrayList<Long>();
                    }
                    this.agencyIds.add(Long.valueOf(str));
                }
            }
        }
        if (seniorIds != null && seniorIds.size() > 0) {
            for (String str : seniorIds) {
                if (str != null && !str.isEmpty() && str.matches("[0-9]*")) {
                    if (this.seniorIds == null) {
                        this.seniorIds = new ArrayList<Long>();
                    }
                    this.seniorIds.add(Long.valueOf(str));
                }
            }
        }
        return this;
    }
}
