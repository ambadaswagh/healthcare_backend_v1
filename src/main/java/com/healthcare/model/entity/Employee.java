package com.healthcare.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.jdo.annotations.Transactional;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.healthcare.model.enums.EmployeeTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "employee")
@EqualsAndHashCode(callSuper = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "manager")
public @Data class Employee extends Audit implements Serializable {

	private static final long serialVersionUID = 512962093355769597L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "gender")
	private String gender;
	@Column(name = "social_security_number")
	private String socialSecurityNumber;
	@Column(name = "date_of_birth")
	private Date dateOfBirth;

	@Column(name = "physical_exam")
	private String physicalExam;
	@Column(name = "certificate_name")
	private String certificateName;

	@Column(name = "certificate_start")
	private Date certificateStart;
	@Column(name = "certificate_end")
	private Date certificateEnd;

	@Column(name = "work_start")
	private Date workStart;
	@Column(name = "work_end")
	private Date workEnd;

	@Column(name = "position")
	private String position;

	private Long manager;
	private String type;

	@Column(name = "status")
	private Integer status;
	@Column(name = "background_check")
	private String backgroundCheck;
	@ManyToOne
	@JoinColumn(name = "agency_id")
	private Agency agency;

	@Column(name = "weekly_hours_worked")
	private String weeklyHoursWorked;

	@Column(name = "weekly_working_time_limitation")
	private Integer weeklyWorkingTimeLimitation;

	@Column(name = "accrual_period_start")
	private Date accrualPeriodStart;

	/*@Column(name = "sick_day")
	private Integer sickDay;*/

	@OneToOne
	@JoinColumn(name = "rules_and_regus_doc_id")
	private Document rulesAndRegusDocId;

	@Column(name = "employee_type")
	@Enumerated(EnumType.STRING)
	private EmployeeTypeEnum employeeType = EmployeeTypeEnum.SALARY;

	@Column(name = "pin")
	private String pin;

	@OneToOne
	@JoinColumn(name = "company_id")
	private Company company;

  @OneToMany(mappedBy = "employee")
  protected List<Leave> leaves;

	@OneToOne
	@JoinColumn(name = "organization_id")
	private Organization organization;

	@Transient
	private Integer weeklyHoursWorkedNumber;

    @Transient
    private String punchStatus;

	@Column(name = "rate")
	private Double rate;

	@Column(name = "vacation_start")
	private Timestamp vacationStart;

	@Column(name = "vacation_end")
	private Timestamp vacationEnd;

	@Column(name = "adjust_sick_hours")
	private Integer adjustSickHours;

}
