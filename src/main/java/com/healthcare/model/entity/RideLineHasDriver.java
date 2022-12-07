package com.healthcare.model.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ride_line_has_driver")
@EqualsAndHashCode(callSuper = true)
public @Data
class RideLineHasDriver extends Audit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Column(name = "day_of_week")
    private Long dayOfWeek;

    @Column(name = "need_trip")
    private Long needTrip;
}
