package com.healthcare.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "`user_has_food_allergy`")
public @Data
class UserFoodAllergy implements Serializable {
    private static final long serialVersionUID = 1425662189663784653L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "food_alleries_id")
    private FoodAllergy foodAllergy;

}
