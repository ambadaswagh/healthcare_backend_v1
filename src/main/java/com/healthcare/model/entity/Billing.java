package com.healthcare.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "billing")
@EqualsAndHashCode(callSuper = true)
public @Data class Billing extends Audit implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 3439390643416676034L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "visit_id")
	private Long visitId;
	@Column(name = "trip_id")
	private Long tripId;
	@Column(name = "type")
	private int type;
	@Column(name = "happened_date")
	private Date happenedDate;
	@Column(name = "billing_deadline")
	private Date billingDeadline;
	@Column(name = "billing_price")
	private Double billingPrice;
	@Column(name = "billing_adjustment")
	private Double billingAdjustment;
	@Column(name = "billing_final_price")
	private Double billingFinalPrice;
	@Column(name = "billing_date")
	private Date billingDate;
	@Column(name = "billed_by")
	private Long billedBy;
	@Column(name = "verified_date")
	private Date VerifiedDate;
	@Column(name = "verified_by")
	private Long verifiedBy;
	@Column(name = "status")
	private String status;
	@Column(name = "note")
	private String note;
}
