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
@Table(name = "admin_post")
public @Data class AdminPost implements Serializable {

	
	private static final long serialVersionUID = 2716722834559934023L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "post_text")
	private String postText;
	@Column(name = "post_date")
	private Timestamp postDate;
	private int status;
	@ManyToOne
	@JoinColumn(name = "admin_id")
	private Admin admin;
}
