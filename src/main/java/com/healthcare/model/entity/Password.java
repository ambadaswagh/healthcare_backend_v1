package com.healthcare.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "password_reset")
@EqualsAndHashCode(callSuper = true)
public @Data class Password extends Audit implements Serializable {

	private static final long serialVersionUID = 1425662189663784653L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToOne
	@JoinColumn(name = "admin_id")
	private Admin admin;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	private String token;

	private Date expired;
	private Integer status;
}
