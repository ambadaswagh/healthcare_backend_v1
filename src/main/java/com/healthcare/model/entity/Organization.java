package com.healthcare.model.entity;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.healthcare.conf.StringListConverter;
import lombok.Data;


/**
 * The persistent class for the organization database table.
 * 
 */
@Entity
@Table(name="organization")
@NamedQuery(name="Organization.findAll", query="SELECT o FROM Organization o")
@Data
public class Organization implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name="address_one")
	private String addressOne;

	@Column(name="address_two")
	private String addressTwo;

	@Column(name="city")
	private String city;

	@Column(name="code")
	private String code;

	@Column(name="created_at")
	private Timestamp createdAt;

	@Column(name="days_work")
	private String daysWork;

	@Column(name="email")
	private String email;

	@Column(name="fax")
	private String fax;

	@Column(name="federal_tax")
	private String federalTax;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="federal_tax_expire")
	private Date federalTaxExpire;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="federal_tax_start")
	private Date federalTaxStart;

	@Column(name="federal_tax_status")
	private Integer federalTaxStatus;

	@Column(name="license_no")
	private String licenseNo;

	@Column(name="main_contact")
	private String mainContact;

	@Column(name="name")
	private String name;

	@Column(name="phone")
	private String phone;

	@Column(name="state")
	private String state;

	@Column(name="state_tax")
	private String stateTax;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="state_tax_expire")
	private Date stateTaxExpire;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="state_tax_start")
	private Date stateTaxStart;

	@Column(name="state_tax_status")
	private Integer stateTaxStatus;

	@Column(name="status")
	private Integer status;

	@Column(name="type")
	private String type;

	@Column(name="updated_at")
	private Timestamp updatedAt;

	@Column(name="web_url")
	private String webUrl;

	@Column(name="worktime_end")
	private Time worktimeEnd;

	@Column(name="worktime_start")
	private Time worktimeStart;

	@Column(name="zipcode")
	private String zipcode;

	@ManyToOne
	@JoinColumn(name = "agency_id")
	private Agency agency;

	@Column(name = "checkbox_list")
	@Convert(converter = StringListConverter.class)
	private List<String> checkboxList;

}
