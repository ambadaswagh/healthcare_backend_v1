package com.healthcare.model.entity;



import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "ride_line")
@EqualsAndHashCode(callSuper = true)
public @Data class RideLine extends Audit implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "borough")
    private String borough;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column(name = "status")
    private Long status;

    @Column(name = "ride_line_color")
    private String rideLineColor;

    @JsonInclude()
    @Transient
    private String seniors;

    @JsonInclude()
    @Transient
    private Long count;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "ride_line_id")
    private List<RideLineHasDriver> rideLineHasDriver;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
