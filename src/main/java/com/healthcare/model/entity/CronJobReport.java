package com.healthcare.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by DuckUser on 10/13/2017.
 */

@Entity
@Table(name = "cronjob_report")
public @Data class CronJobReport implements Serializable {

    private static final long serialVersionUID = 512962093355769597L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    private Employee employee;

    private String jobStatus;

    private String errorDesc;

    private String message;

}
