package com.healthcare.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "report")
public @Data class Report implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -709441430085367978L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "base_id")
	private long baseId;
	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;
	@ManyToOne
	@JoinColumn(name = "admin_id")
	private Admin admin;
	@Column(name = "report_title")
	private String reportTitle;
	@Column(name = "start_date")
	private Timestamp startDate;
	@Column(name = "end_date")
	private Timestamp endDate;
	@Column(name = "data_columns")
	private String dataColumns;
	private String format;
	@Column(name = "download_at")
	private Timestamp downloadAt = new Timestamp(new Date().getTime());
	@Column(name = "created_at")
	private Timestamp createdAt = new Timestamp(new Date().getTime());
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;

}
