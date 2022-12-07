package com.healthcare.model.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "resource")
@EqualsAndHashCode(callSuper = true)
public @Data class Resource extends Audit implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4624488646796958772L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;
	@Column(name = "category")
	private String category;
	@Column(name = "name")
	private String name;
	@Column(name = "website")
	private String website;
	@Column(name = "phone")
	private String phone;
	@Column(name = "address_one")
	private String address_one;
	@Column(name = "address_two")
	private String address_two;
	@Column(name = "city")
	private String city;
	@Column(name = "zipcode")
	private String zipcode;
	@Column(name = "state")
	private String state;
	@Column(name = "status")
	private int status;
	@Column(name = "note")
	private String note;
	@OneToOne
	@JoinColumn(name = "picture_id")
	private Document picture;
	@Transient
	private Set<ResourceBusinessHours> business_hours;
	
}
