package com.healthcare.api.auth.model;

import java.io.Serializable;

import com.healthcare.model.entity.Admin;

/**
 * 
 * @author orange
 *
 */
public class AuthResponse implements Serializable {

	private static final long serialVersionUID = 1250166508152483573L;

	private final String token;
	private Admin info;

	public AuthResponse(String token, Admin info) {
		this.token = token;
		this.info = info;
	}

	public String getToken() {
		return this.token;
	}

	public Admin getInfo() {
		return info;
	}

	public void setInfo(Admin info) {
		this.info = info;
	}

}
