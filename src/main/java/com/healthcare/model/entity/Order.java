/**
 * 
 */
package com.healthcare.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.healthcare.util.CustomJsonDateSerializer;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Phong
 *
 */
@Entity
@Data
@Table(name = "orders")
public class Order extends Audit implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8056299874040121087L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "user_id")
	private Integer user;
	
	@ManyToOne
	@JoinColumn(name = "meal_id")
	private Meal meal;
	
	@ManyToOne
	@JoinColumn(name = "organzation_id")
	private Organization organization;
	
	@JsonSerialize(using=CustomJsonDateSerializer.class)
	@Column(name = "order_time")
	private Date orderTime;
	
	@JsonSerialize(using=CustomJsonDateSerializer.class)
	@Column(name = "delivery_time")
	private Date deliveryTime;
	
	@Column(name = "status")
	private Integer status;
}
