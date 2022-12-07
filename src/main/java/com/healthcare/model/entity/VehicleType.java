package com.healthcare.model.entity;

import com.healthcare.model.enums.StatusEnum;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Mostapha on 07/01/2018.
 */
@Entity
@Table(name = "vehicle_type")
@Data
public class VehicleType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private StatusEnum status;

    @Column(name = "image_url")
    private String image;

    @Column(name = "vehicle_icon")
    private String vehicleIcon;
}
