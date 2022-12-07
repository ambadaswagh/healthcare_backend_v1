package com.healthcare.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Alter by Jean Antunes on 7/6/17.
 */
@Entity
@Table(name = "employee_has_activity", schema = "health_care_v1_dev", catalog = "")
public @Data class EmployeeActivity implements Serializable {

    private static final long serialVersionUID = 1976459796513221823L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;

}