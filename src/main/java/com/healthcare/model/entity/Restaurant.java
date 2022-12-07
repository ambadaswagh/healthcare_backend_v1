package com.healthcare.model.entity;

import java.io.*;
import java.sql.*;

import javax.persistence.*;

import lombok.*;

/*
 * Github #140
 */

@Entity
@Table(name = "restaurant")
@EqualsAndHashCode(callSuper = true)
public @Data class Restaurant extends Audit implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "restaurant_id")
	private long id;
	
	@Column(name = "restaurant_name")
	private String name;
	
	@Column(name = "restaurant_tel")
	private String tel;
	
	@Column(name = "restaurant_location")
	private String location;
	
	@Column(name = "restaurant_contact_person")
	private String contactPerson;
	
	@Column(name = "restaurant_operation_start")
	private Date start;
	
	@Column(name = "restaurant_operation_end")
	private Date end;
	
	@Column(name = "restaurant_status")
	private int status;

}
