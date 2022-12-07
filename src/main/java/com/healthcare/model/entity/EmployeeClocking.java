package com.healthcare.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "employee_clocking")
@EqualsAndHashCode(callSuper = true)
public @Data class EmployeeClocking extends Audit implements Serializable {

	private static final long serialVersionUID = 512962093355769597L;
	private static final DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;
	
	@Column(name = "check_in_time")
	private Date checkInTime;

	@Column(name = "check_out_time")
	private Date checkOutTime;

	@OneToOne
	@JoinColumn(name = "check_in_signature_id")
	private Document checkInSignatureId;

	@OneToOne
	@JoinColumn(name = "check_out_signature_id")
	private Document checkOutSignatureId;

	@Transient
	private Date startTime;
	@Transient
	private Date endTime;

	public EmployeeClocking fromObject(Object[] data) {
		this.id = Long.valueOf((Integer) data[0]);
		this.startTime = dtf.parseDateTime((String) data[9]).toDate();
		this.endTime = dtf.parseDateTime((String) data[10]).toDate();
		return this;
	}

}
