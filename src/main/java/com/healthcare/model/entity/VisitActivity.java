package com.healthcare.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

/**
 * Created by pazfernando on 4/30/17.
 */
@Entity
@Table(name = "visit_has_activity", schema = "health_care_v1_dev", catalog = "")
@IdClass(VisitActivityPK.class)
public @Data class VisitActivity implements Serializable {

	private static final long serialVersionUID = -4384670103126314525L;
	@Id
    @Column(name = "visit_id")
    protected Long visitId;
	@Id
    @Column(name = "activity_id")
    protected Long activityId;	
	@ManyToOne
	@JoinColumn(name = "visit_id" , insertable = false, updatable = false)
	private Visit visit;
	@ManyToOne
	@JoinColumn(name = "activity_id" ,insertable = false, updatable = false)
	private Activity activity;
	private String tableName;
	private String seat;
	@Column(name = "start_time")
	private Timestamp startTime;
	@Column(name = "end_time")
	private Timestamp endTime;
}
