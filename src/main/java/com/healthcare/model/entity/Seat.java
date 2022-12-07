package com.healthcare.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.healthcare.model.enums.SeatStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/*
 * Github #136
 */

@Entity
@Table(name = "seat")
@EqualsAndHashCode(callSuper = true)
public @Data class Seat extends Audit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private TableEntity table;

    @Column(name = "seat_called")
    private String seatCalled;

    @Column(name = "seat_number_capacity")
    private Integer seatNumberCapacity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private User user;

    @Column(name = "seat_status")
    @Enumerated(EnumType.STRING)
    private SeatStatusEnum status = SeatStatusEnum.AVAILABLE;

    @Column(name = "status")
    private Integer statusType;
}