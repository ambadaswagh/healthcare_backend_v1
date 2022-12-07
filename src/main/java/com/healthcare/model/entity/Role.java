package com.healthcare.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "role")
@EqualsAndHashCode(callSuper = true)
public @Data class Role extends Audit implements Serializable {

	private static final long serialVersionUID = -6360665934926249915L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private long level;
	@Column(name = "level_name")
	private String levelName;
	private long status;

	@Column(name = "default_visible_list")
	private String defaultVisibleList;
	@Column(name = "default_action_list")
	private String defaultActionList;
}