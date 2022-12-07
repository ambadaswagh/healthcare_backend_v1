package com.healthcare.model.entity;

import java.io.Serializable;
import java.sql.Time;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "business_hours")
@EqualsAndHashCode(callSuper = true)
public @Data class ResourceBusinessHours extends Audit implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6113646196211592737L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;
	@Column(name = "resource_id")
    private long resource_id;
	@Column(name = "day_of_week")
	private int day_of_week;
	@Column(name = "open_time")
	private Time open_time;
	@Column(name = "close_time")
	private Time close_time;
	@Column(name = "time_zone")
	private String timezone;
}
