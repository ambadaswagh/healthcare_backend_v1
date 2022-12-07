package com.healthcare.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "employee_payment")
@EqualsAndHashCode(callSuper = true)
public @Data class EmployeePayment extends Audit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "date")
    private Date date;

    @Column(name = "type")
    private String type;

    @Column(name = "spread_of_hour")
    private Double spreadOfHour;

    @Column(name = "work_hours")
    private Double workHours;

    @Column(name = "total_hours")
    private Double totalHours;

    @Column(name = "payment_for_that_day")
    private Double paymentForThatDay;

    @Column(name = "adjustment")
    private Double adjustment;

    @Column(name = "note")
    private String note;

    @Transient
    private Double totalPayment;
}
