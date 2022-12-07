package com.healthcare.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AdminDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	@NotNull
	private String username;
	private String password;
	@NotNull
	private String gender;
	@NotNull
	private String email;
	@NotNull
	private String phone;
	private String ip;
	@NotNull
	private Long roleId;
	@NotNull
	private String firstName;
	private String middleName;
	@NotNull
	private String lastName;
	private String secondaryPhone;
	private Long profilePhotoId;
	private String deviceAddress;
	private String rememberToken;
	private String menulist;
}
