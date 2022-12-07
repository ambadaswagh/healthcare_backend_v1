package com.healthcare.model.entity;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.FilterDef;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.healthcare.api.common.HealthcareUtil;

import lombok.Data;

/**
 * Created by DuckUser on 10/13/2017.
 */

@Entity
@Table(name = "`leave`")
public @Data class Leave implements Serializable {

	private static final long serialVersionUID = 512962093355769597L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


	@OneToOne(cascade={CascadeType.ALL})
	@JoinColumn(name = "employee_id")
	private Employee employee;

  @JsonIgnore()
  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  @OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "sick_days_limitation")
	private Integer sickDaysLimitation;

	@Column(name = "sick_days_used")
	private Integer sickDaysUsed;

	@Column(name = "ask_for_leave_days_limitation")
	private Integer askForLeaveDaysLimitation;

	@Column(name = "ask_for_leave_days_used")
	private Integer askForLeaveDaysUsed;

	@Column(name = "vacation_days_limitation")
	private Integer vacationDaysLimitation;

	@Column(name = "vacation_days_used")
	private Integer vacationDaysUsed;

	@Column(name = "sick_days")
	private Integer sickDays;

	@Transient
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String errorMessage;

	private Integer year = HealthcareUtil.getCurrentYear();

}
