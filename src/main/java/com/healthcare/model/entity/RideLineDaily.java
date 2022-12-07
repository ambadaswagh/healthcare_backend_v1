package com.healthcare.model.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ride_line_daily")
@EqualsAndHashCode(callSuper = true)
public @Data class RideLineDaily extends Audit implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "borough")
    private String borough;

    @Column(name = "note")
    private String note;

    @Column(name = "date")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "ride_line_id")
    private RideLine rideLine;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "inbound_driver_id")
    private Driver inboundDriver;

    @ManyToOne
    @JoinColumn(name = "outbound_driver_id")
    private Driver outboundDriver;

    @Column(name = "status")
    private Long status;

    @JsonInclude()
    @Transient
    private String seniors;

    @JsonInclude()
    @Transient
    private Long count;

    @Column(name = "ride_line_daily_color")
    private String rideLineDailyColor;

}
