package com.healthcare.model.entity;

import com.healthcare.model.enums.StatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Mostapha on 07/01/2018.
 */
@Entity
@Table(name = "vehicle_brand")
@Data
@EqualsAndHashCode(callSuper = true)
public class VehicleBrand extends Audit implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "status")
    private StatusEnum status;
}
