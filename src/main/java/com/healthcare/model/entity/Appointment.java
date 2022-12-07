package com.healthcare.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * The persistent class for the appointment database table.
 * 
 */
@Entity
	@NamedQuery(name = "Appointment.findAll", query = "SELECT a FROM Appointment a")
@Data
public class Appointment extends Audit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id")
	@NotNull
	private User user;

	@OneToOne
	@JoinColumn(name = "admin_id")
	@NotNull
	private Admin admin;

	@Column(name = "type")
	private String type;

	@NotNull
	@Column(name = "reason")
	private String reason;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "appointment_time")
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date appointmentTime;

	@Column(name = "location")
	private String location;

	@Column(name = "status")
	private Integer status = 1;

	@Column(name = "need_notification")
	private Integer needNotification;

	@OneToOne
	@JoinColumn(name = "organization_id")
	private Organization organization;

	@Column(name = "comment")
	private String comment;

	@Column(name = "alert_type")
	private Integer alertType;

}