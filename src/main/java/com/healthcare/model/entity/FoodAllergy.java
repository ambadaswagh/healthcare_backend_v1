package com.healthcare.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "`food_allergy`")
public @Data
class FoodAllergy implements Serializable {
    private static final long serialVersionUID = 1425662189663784653L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(name = "added_by_user")
    private boolean addedByUser ;
}
