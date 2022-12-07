package com.healthcare.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class LeaveStatus {

    private String type;
    private Integer total;
    
    public LeaveStatus fromDBObject(Object[] data){
    	this.type = (String) data[0];
    	this.total = ((BigDecimal) data[1]).intValue();
    	return this;
    }
}
