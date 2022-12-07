package com.healthcare.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.Data;

@Entity
@Table(name = "agency_type")/*
@SQLDelete(sql = "UPDATE agency_type SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")*/
public @Data class AgencyType implements Serializable {

	
	private static final long serialVersionUID = -5513342374278345081L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "name")
	private String name;
	@Column(name = "status")
	private int status;

	/*private Boolean deleted;*/
}
