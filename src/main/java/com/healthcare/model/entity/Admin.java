package com.healthcare.model.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.healthcare.conf.StringListConverter;
import com.healthcare.model.enums.EntityStatusEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "admin")
@EqualsAndHashCode(callSuper = true)
public @Data class Admin extends Audit implements Serializable {

	private static final long serialVersionUID = 1425662189663784653L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	private String username;
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	@NotNull
	private String gender;
	@NotNull
	private String email;
	@NotNull
	private String phone;
	private String ip;
	@NotNull
	private long status = EntityStatusEnum.ENABLE.getValue();
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;
	
	@NotNull
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "middle_name")
	private String middleName;
	@NotNull
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "secondary_phone")
	private String secondaryPhone;

	@OneToOne
	@JoinColumn(name = "profile_photo_id")
	private Document profilePhoto;

	@Column(name = "device_address")
	private String deviceAddress;
	@Column(name = "remember_token")
	private String rememberToken;

	@Column(name = "menu_list")
	@Convert(converter = StringListConverter.class)
	private List<String> menulist;

	@Column(name = "action_list")
	@Convert(converter = StringListConverter.class)
	private List<String> actionlist;

	@Transient
	Long companyId;

	@Transient
	Company company;

	@Transient
	Long agencyId;

	@Transient
	Agency agency;

	@Transient
	AdminSetting adminSetting;

	public String getPassword() {
		return password;
	}

	public Long getId() {
		return id;
	}
}
