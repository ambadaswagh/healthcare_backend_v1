package com.healthcare.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "insurance_history")
@EqualsAndHashCode(callSuper = true)
public @Data class InsuranceHistory extends Audit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "insurance_id")
    private Organization insurance;

    @Column(name = "authorization_start")
    private Timestamp authorizationStart;

    @Column(name = "authorization_end")
    private Timestamp authorizationEnd;

    @Column(name = "insurance_code")
    private String insuranceCode;

    @Column(name = "authorization_code")
    private String authorizationCode;

    @Column(name = "reserved_1")
    private String reservedOne;

    @Column(name = "reserved_2")
    private String reservedTwo;
}
