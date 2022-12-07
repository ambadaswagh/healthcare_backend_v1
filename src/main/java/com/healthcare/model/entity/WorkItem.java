package com.healthcare.model.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;


/**
 * Created by Mostafa Hamed on 30/06/17.
 * @author mhamed
 * @version 1.0
 */


@Entity
@Table(name = "work_item", schema = "health_care_v1_dev", catalog = "")
public @Data class WorkItem implements Serializable{
	

	private static final long serialVersionUID = 683646276941914143L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long Id;
	@Column(name="item_name")
	private String itemName;
	@Column(name="item_note")
	private String itemNote;
	
}
