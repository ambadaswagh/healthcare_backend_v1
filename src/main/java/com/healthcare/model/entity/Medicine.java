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

/**
 * Created by inimn on 11/9/2017.
 */
@Entity
@Data
@Table(name = "medicine")
public class Medicine implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "medicine_name")
	private String name;

	@Column(name = "medicine_use_ways")
	private String useWays;

	@Column(name = "medicine_type")
	private String type;

	@ManyToOne
	@JoinColumn(name = "medicine_prescribed_by_id")
	private Organization prescribed_by; 

}
