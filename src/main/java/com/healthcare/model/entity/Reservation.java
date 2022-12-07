package com.healthcare.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "reservation")
@EqualsAndHashCode(callSuper = true)
public @Data class Reservation extends Audit implements Serializable {

    private static final long serialVersionUID = 512962093355769597L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "booked_date")
    private Date bookedDate;

    @ManyToOne
    @JoinColumn(name = "meal_id")
    private Meal meal;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private TableEntity agencyTable;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;
}


