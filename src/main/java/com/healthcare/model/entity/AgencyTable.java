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
@Table(name = "agency_table")
@EqualsAndHashCode(callSuper = true)
public @Data class AgencyTable extends Audit implements Serializable {

	private static final long serialVersionUID = 512962093355769597L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "agency_id")
	private Agency agency;

	@Column(name = "table_no")
	private String tableNo;

	@Column(name = "table_id")
	private Long tableId;

	@Column(name = "table_number_capacity")
	private Integer tableNumberCapacity;
}
