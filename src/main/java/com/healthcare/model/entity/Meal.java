package com.healthcare.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "meal")
@EqualsAndHashCode(callSuper = true)
public @Data class Meal extends Audit implements Serializable {

    private static final long serialVersionUID = -6955987587452175363L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "restaurant_id")
	private Organization organization;

	@Column(name = "meal_class")
	private String mealClass;
	private String name;
	private Double price;
	private String ingredients;
	private String notes;
	@Column(name = "cuisine")
	private String cuisine;

	@Column(name = "verified_by_nutritionist")
	private Integer verifiedByNutritionist;

	private Integer status = 0;

  @ManyToMany()
  @JoinTable(
    name = "meal_has_ingredient",
    joinColumns = { @JoinColumn(name = "meal_id") },
    inverseJoinColumns = { @JoinColumn(name = "ingredient_id") }
  )
  private List<Ingredient> ingredientList;

  @OneToOne(cascade = { CascadeType.ALL })
  @JoinColumn(name = "meal_image_id")
  private Document mealPhoto;

  @Column(name = "meal_status")
  private Integer mealStatus;

	@ManyToOne
	@JoinColumn(name = "agency_id")
	private Agency agency;

	@Column(name = "selected")
	private int selected;
}
