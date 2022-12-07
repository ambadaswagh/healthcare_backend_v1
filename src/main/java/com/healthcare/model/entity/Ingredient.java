package com.healthcare.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "`ingredient`")
public @Data class Ingredient extends Audit implements Serializable {
    private static final long serialVersionUID = 1425662189663784653L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

  @ManyToMany()
  @JoinTable(
    name = "ingredient_has_allergy",
    joinColumns = { @JoinColumn(name = "ingredient_id") },
    inverseJoinColumns = { @JoinColumn(name = "food_allergy_id") }
  )
  private List<FoodAllergy> foodAllergies;
}
