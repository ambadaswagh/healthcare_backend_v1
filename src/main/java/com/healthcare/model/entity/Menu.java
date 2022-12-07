package com.healthcare.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

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
@Table(name = "menu")
public @Data class Menu implements Serializable {

	
	private static final long serialVersionUID = -3163487536542705169L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String url;
	@Column(name = "angular_url")
	private String angularUrl;
	private String page;
	@Column(name = "class")
	private String clazz;
	@Column(name = "img_url")
	private String imgUrl;
	@Column(name = "created_at")
	private Timestamp createdAt;
	@Column(name = "display_order")
	private Integer displayOrder;
	private Integer status;
	
}
