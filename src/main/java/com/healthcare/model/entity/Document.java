package com.healthcare.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "file_upload")
@EqualsAndHashCode(callSuper = true)
public @Data class Document extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7834424843432334738L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String entity;
	
	@Column(name = "entity_id")
	private Long entityId;
	
	@Column(name = "file_class")
	private String fileClass;
	
	@Column(name = "file_name")
	private String fileName;
	
	@Column(name = "file_type")
	private String fileType;
	
	@Column(name = "file_size")
	private Long fileSize;
	
	@Column(name = "file_path")
	private String filePath;
	
	@Column(name = "file_url")
	private String fileUrl;
	
	private String status;

}

