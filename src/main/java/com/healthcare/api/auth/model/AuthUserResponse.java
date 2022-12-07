package com.healthcare.api.auth.model;

import java.io.Serializable;

import com.healthcare.model.entity.User;

/**
 * 
 * @author orange
 *
 */
public class AuthUserResponse implements Serializable {

	private static final long serialVersionUID = 1250166508152483573L;

	private final String token;
	private User info;

	public AuthUserResponse(String token, User info) {
		this.token = token;
		this.info = info;
	}

	public String getToken() {
		return this.token;
	}

	public User getInfo() {
		return info;
	}

	public void setInfo(User info) {
		this.info = info;
	}

}
