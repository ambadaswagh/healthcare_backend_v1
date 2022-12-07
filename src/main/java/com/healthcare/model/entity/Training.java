package com.healthcare.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "training")
@EqualsAndHashCode(callSuper = true)
public @Data class Training extends Audit implements Serializable {

	private static final long serialVersionUID = -797138070537430162L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String title;
	@Column(name = "start_time")
	private Timestamp startTime;
	@Column(name = "end_time")
	private Timestamp endTime;
	private String type;
	private String trainer;
	private String trainee;
	private String location;
	private String note;
	private long status;

	@ManyToOne
	@JoinColumn(name = "agency_id")
	private Agency agency;
}
