package com.healthcare.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "ride")
@EqualsAndHashCode(callSuper = true)
public @Data class Ride extends Audit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "pickup")
    private String pickup;

    @Column(name = "dropoff")
    private String dropoff;

    @Column(name = "date")
    private Date date;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "need_trip")
    private Long needTrip;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "status")
    private Long status;

    @ManyToOne
    @JoinColumn(name = "ride_line_id")
    private RideLine rideLine;

    @ManyToOne
    @JoinColumn(name = "ride_line_daily_id")
    private RideLineDaily rideLineDaily;

    @Column(name = "ride_color")
    private String rideColor;

    @Transient
    private boolean hasBlacklistDriver;

    @Transient
    private String blacklistDriverNote;
}
