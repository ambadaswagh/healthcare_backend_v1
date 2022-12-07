package com.healthcare.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "agency")
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class Agency extends Audit implements Serializable {

	private static final long serialVersionUID = -6572833124019691517L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "license_no")
	private String licenseNo;
	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;
	@Column(name = "name")
	private String name;
	@Column(name = "tracking_mode")
	private int trackingMode;
	@Column(name = "contact_person")
	private String contactPerson;
	@Column(name = "email")
	private String email;
	@Column(name = "address_one")
	private String addressOne;
	@Column(name = "address_two")
	private String addressTwo;
	@Column(name = "city")
	private String city;
	@Column(name = "state")
	private String state;
	@Column(name = "zipcode")
	private String zipcode;
	@Column(name = "timezone")
	private String timezone;
	@Column(name = "phone")
	private String phone;
	@Column(name = "holiday")
	private String holiday;
	@Column(name = "fax")
	private String fax;
	@Column(name = "status")
	private int status;
//	@Column(name = "table_number_capacity")
//	private Integer tableNumberCapacity;
	@ManyToOne
	@JoinColumn(name = "agency_type_id")
	private AgencyType agencyType;
	
	@Column(name = "capacity_num")
	private Integer capacityNum;

	@Column(name = "minimum_wage")
	private Double minimumWage;

	@Column(name = "cctv_url")
	private String cctvUrl;

	@Column(name = "required_hours")
	private Long requiredHours;

	@Column(name = "agreement_signature_id")
    private Long agreementSignatureId;

	@Column(name = "meals_selected")
	private String mealsSelected;

	@Column(name = "main_white_label")
	private String mainWhiteLabel;

	@Column(name = "upper_left_label")
	private String upperLeftLabel;

	@OneToOne
	@JoinColumn(name = "logo_id")
	private Document logo;
}