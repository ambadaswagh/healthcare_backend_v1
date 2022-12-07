package com.healthcare.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = "trip_analyze")
@EqualsAndHashCode(callSuper = true)
@Data
public class TripAnalyze extends Audit implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private User user;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "ride_line_id")
    private RideLine rideLine;

    @Column(name = "analyze_date")
    private Date analyzeDate;

    @Column(name = "user_full_name")
    private String userFullName;

    @Column(name = "max_trip_id")
    private Integer maxTripId;

    @Column(name = "pickup_zip")
    private String pickupZip;

    @Column(name = "dropoff_zip")
    private String dropoffZip;

    @Column(name = "trip_count")
    private BigInteger tripCount;
}
