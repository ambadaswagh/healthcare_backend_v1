package com.healthcare.api.model;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * @author Moustafa Hamed
 * Used for express checkin and express checkout
 *
 */
@Data
public class ExpressVisitRequestDTO implements Serializable{
	
	private static final long serialVersionUID = 8109925376598549926L;

	private Integer id;
	private Integer agencyId;
	private Integer userId;
	private String signature;
	private String pin;

}
