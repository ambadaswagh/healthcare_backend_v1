package com.healthcare.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
public class NutritionDTO implements Serializable{

	private Long id;

	private String height;

	private String weight;

	private String allergy;

	private String bodyMassIndex;

	private Long refrigeratorOrFreezerFacilitiesAdequate;

	private Long openContainersOrCartonsCutFood;

	private String physicianPrescribedTherapeuticDiet;

	private boolean calorieControlledDiet;

	private boolean sodiumRestricted;

	private boolean fatRestricted;

	private boolean renal;

	private boolean highCalorie;

	private boolean vegetarian;

	private boolean highFiber;

	private boolean nutritionalSupplements;

	private boolean sugarRestricted;

	private boolean other;

	private String alcoholScreeningTest;

	private boolean feltYouCutDownYourDrinking;

	private boolean criticizingYourDrinking;

	private boolean guiltyAboutYourDrinking;

	private boolean getRidOfHangover;

	private String nutritionalRiskStatus;

	private boolean nutritionalRiskOne;

	private boolean nutritionalRiskTwo;

	private boolean nutritionalRiskThree;

	private boolean nutritionalRiskFour;

	private boolean nutritionalRiskFive;

	private boolean nutritionalRiskSix;

	private boolean nutritionalRiskSeven;

	private boolean nutritionalRiskEight;

	private boolean nutritionalRiskNine;

	private boolean nutritionalRiskTen;

	private String feltCutDownDrinking;

	private String annoyedYouByCriticizeYourDrinking;

	private String feltBadOrQuiltyAboutDrinking;

	private String needDrinkInMorning;

	private Long nutritionalScoreOne;

	private Long nutritionalScoreTwo;

	private Long nutritionalScoreThree;

	private Long nutritionalScoreFour;

	private Long nutritionalScoreFive;

	private Long nutritionalScoreSix;

	private Long nutritionalScoreSeven;

	private Long nutritionalScoreEight;

	private Long nutritionalScoreNine;

	private Long nutritionalScoreTen;

	private String nsiScore;

	private String conclusion;

	private boolean highRisk;

	private boolean moderateRisk;

	private boolean lowRisk;

	private String reserved_1;

	private String reserved_2;

	private String reserved_3;

	private Long userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getAllergy() {
		return allergy;
	}

	public void setAllergy(String allergy) {
		this.allergy = allergy;
	}

	public String getBodyMassIndex() {
		return bodyMassIndex;
	}

	public void setBodyMassIndex(String bodyMassIndex) {
		this.bodyMassIndex = bodyMassIndex;
	}

	public Long getRefrigeratorOrFreezerFacilitiesAdequate() {
		return refrigeratorOrFreezerFacilitiesAdequate;
	}

	public void setRefrigeratorOrFreezerFacilitiesAdequate(Long refrigeratorOrFreezerFacilitiesAdequate) {
		this.refrigeratorOrFreezerFacilitiesAdequate = refrigeratorOrFreezerFacilitiesAdequate;
	}

	public Long getOpenContainersOrCartonsCutFood() {
		return openContainersOrCartonsCutFood;
	}

	public void setOpenContainersOrCartonsCutFood(Long openContainersOrCartonsCutFood) {
		this.openContainersOrCartonsCutFood = openContainersOrCartonsCutFood;
	}

	public String getPhysicianPrescribedTherapeuticDiet() {
		return physicianPrescribedTherapeuticDiet;
	}

	public void setPhysicianPrescribedTherapeuticDiet(String physicianPrescribedTherapeuticDiet) {
		this.physicianPrescribedTherapeuticDiet = physicianPrescribedTherapeuticDiet;
	}

	public boolean isCalorieControlledDiet() {
		return calorieControlledDiet;
	}

	public void setCalorieControlledDiet(boolean calorieControlledDiet) {
		this.calorieControlledDiet = calorieControlledDiet;
	}

	public boolean isSodiumRestricted() {
		return sodiumRestricted;
	}

	public void setSodiumRestricted(boolean sodiumRestricted) {
		this.sodiumRestricted = sodiumRestricted;
	}

	public boolean isFatRestricted() {
		return fatRestricted;
	}

	public void setFatRestricted(boolean fatRestricted) {
		this.fatRestricted = fatRestricted;
	}

	public boolean isRenal() {
		return renal;
	}

	public void setRenal(boolean renal) {
		this.renal = renal;
	}

	public boolean isHighCalorie() {
		return highCalorie;
	}

	public void setHighCalorie(boolean highCalorie) {
		this.highCalorie = highCalorie;
	}

	public boolean isVegetarian() {
		return vegetarian;
	}

	public void setVegetarian(boolean vegetarian) {
		this.vegetarian = vegetarian;
	}

	public boolean isHighFiber() {
		return highFiber;
	}

	public void setHighFiber(boolean highFiber) {
		this.highFiber = highFiber;
	}

	public boolean isNutritionalSupplements() {
		return nutritionalSupplements;
	}

	public void setNutritionalSupplements(boolean nutritionalSupplements) {
		this.nutritionalSupplements = nutritionalSupplements;
	}

	public boolean isSugarRestricted() {
		return sugarRestricted;
	}

	public void setSugarRestricted(boolean sugarRestricted) {
		this.sugarRestricted = sugarRestricted;
	}

	public boolean isOther() {
		return other;
	}

	public void setOther(boolean other) {
		this.other = other;
	}

	public String getAlcoholScreeningTest() {
		return alcoholScreeningTest;
	}

	public void setAlcoholScreeningTest(String alcoholScreeningTest) {
		this.alcoholScreeningTest = alcoholScreeningTest;
	}

	public boolean isFeltYouCutDownYourDrinking() {
		return feltYouCutDownYourDrinking;
	}

	public void setFeltYouCutDownYourDrinking(boolean feltYouCutDownYourDrinking) {
		this.feltYouCutDownYourDrinking = feltYouCutDownYourDrinking;
	}

	public boolean isCriticizingYourDrinking() {
		return criticizingYourDrinking;
	}

	public void setCriticizingYourDrinking(boolean criticizingYourDrinking) {
		this.criticizingYourDrinking = criticizingYourDrinking;
	}

	public boolean isGuiltyAboutYourDrinking() {
		return guiltyAboutYourDrinking;
	}

	public void setGuiltyAboutYourDrinking(boolean guiltyAboutYourDrinking) {
		this.guiltyAboutYourDrinking = guiltyAboutYourDrinking;
	}

	public boolean isGetRidOfHangover() {
		return getRidOfHangover;
	}

	public void setGetRidOfHangover(boolean getRidOfHangover) {
		this.getRidOfHangover = getRidOfHangover;
	}

	public String getNutritionalRiskStatus() {
		return nutritionalRiskStatus;
	}

	public void setNutritionalRiskStatus(String nutritionalRiskStatus) {
		this.nutritionalRiskStatus = nutritionalRiskStatus;
	}

	public boolean isNutritionalRiskOne() {
		return nutritionalRiskOne;
	}

	public void setNutritionalRiskOne(boolean nutritionalRiskOne) {
		this.nutritionalRiskOne = nutritionalRiskOne;
	}

	public boolean isNutritionalRiskTwo() {
		return nutritionalRiskTwo;
	}

	public void setNutritionalRiskTwo(boolean nutritionalRiskTwo) {
		this.nutritionalRiskTwo = nutritionalRiskTwo;
	}

	public boolean isNutritionalRiskThree() {
		return nutritionalRiskThree;
	}

	public void setNutritionalRiskThree(boolean nutritionalRiskThree) {
		this.nutritionalRiskThree = nutritionalRiskThree;
	}

	public boolean isNutritionalRiskFour() {
		return nutritionalRiskFour;
	}

	public void setNutritionalRiskFour(boolean nutritionalRiskFour) {
		this.nutritionalRiskFour = nutritionalRiskFour;
	}

	public boolean isNutritionalRiskFive() {
		return nutritionalRiskFive;
	}

	public void setNutritionalRiskFive(boolean nutritionalRiskFive) {
		this.nutritionalRiskFive = nutritionalRiskFive;
	}

	public boolean isNutritionalRiskSix() {
		return nutritionalRiskSix;
	}

	public void setNutritionalRiskSix(boolean nutritionalRiskSix) {
		this.nutritionalRiskSix = nutritionalRiskSix;
	}

	public boolean isNutritionalRiskSeven() {
		return nutritionalRiskSeven;
	}

	public void setNutritionalRiskSeven(boolean nutritionalRiskSeven) {
		this.nutritionalRiskSeven = nutritionalRiskSeven;
	}

	public boolean isNutritionalRiskEight() {
		return nutritionalRiskEight;
	}

	public void setNutritionalRiskEight(boolean nutritionalRiskEight) {
		this.nutritionalRiskEight = nutritionalRiskEight;
	}

	public boolean isNutritionalRiskNine() {
		return nutritionalRiskNine;
	}

	public void setNutritionalRiskNine(boolean nutritionalRiskNine) {
		this.nutritionalRiskNine = nutritionalRiskNine;
	}

	public boolean isNutritionalRiskTen() {
		return nutritionalRiskTen;
	}

	public void setNutritionalRiskTen(boolean nutritionalRiskTen) {
		this.nutritionalRiskTen = nutritionalRiskTen;
	}

	public String getFeltCutDownDrinking() {
		return feltCutDownDrinking;
	}

	public void setFeltCutDownDrinking(String feltCutDownDrinking) {
		this.feltCutDownDrinking = feltCutDownDrinking;
	}

	public String getAnnoyedYouByCriticizeYourDrinking() {
		return annoyedYouByCriticizeYourDrinking;
	}

	public void setAnnoyedYouByCriticizeYourDrinking(String annoyedYouByCriticizeYourDrinking) {
		this.annoyedYouByCriticizeYourDrinking = annoyedYouByCriticizeYourDrinking;
	}

	public String getFeltBadOrQuiltyAboutDrinking() {
		return feltBadOrQuiltyAboutDrinking;
	}

	public void setFeltBadOrQuiltyAboutDrinking(String feltBadOrQuiltyAboutDrinking) {
		this.feltBadOrQuiltyAboutDrinking = feltBadOrQuiltyAboutDrinking;
	}

	public String getNeedDrinkInMorning() {
		return needDrinkInMorning;
	}

	public void setNeedDrinkInMorning(String needDrinkInMorning) {
		this.needDrinkInMorning = needDrinkInMorning;
	}

	public Long getNutritionalScoreOne() {
		return nutritionalScoreOne;
	}

	public void setNutritionalScoreOne(Long nutritionalScoreOne) {
		this.nutritionalScoreOne = nutritionalScoreOne;
	}

	public Long getNutritionalScoreTwo() {
		return nutritionalScoreTwo;
	}

	public void setNutritionalScoreTwo(Long nutritionalScoreTwo) {
		this.nutritionalScoreTwo = nutritionalScoreTwo;
	}

	public Long getNutritionalScoreThree() {
		return nutritionalScoreThree;
	}

	public void setNutritionalScoreThree(Long nutritionalScoreThree) {
		this.nutritionalScoreThree = nutritionalScoreThree;
	}

	public Long getNutritionalScoreFour() {
		return nutritionalScoreFour;
	}

	public void setNutritionalScoreFour(Long nutritionalScoreFour) {
		this.nutritionalScoreFour = nutritionalScoreFour;
	}

	public Long getNutritionalScoreFive() {
		return nutritionalScoreFive;
	}

	public void setNutritionalScoreFive(Long nutritionalScoreFive) {
		this.nutritionalScoreFive = nutritionalScoreFive;
	}

	public Long getNutritionalScoreSix() {
		return nutritionalScoreSix;
	}

	public void setNutritionalScoreSix(Long nutritionalScoreSix) {
		this.nutritionalScoreSix = nutritionalScoreSix;
	}

	public Long getNutritionalScoreSeven() {
		return nutritionalScoreSeven;
	}

	public void setNutritionalScoreSeven(Long nutritionalScoreSeven) {
		this.nutritionalScoreSeven = nutritionalScoreSeven;
	}

	public Long getNutritionalScoreEight() {
		return nutritionalScoreEight;
	}

	public void setNutritionalScoreEight(Long nutritionalScoreEight) {
		this.nutritionalScoreEight = nutritionalScoreEight;
	}

	public Long getNutritionalScoreNine() {
		return nutritionalScoreNine;
	}

	public void setNutritionalScoreNine(Long nutritionalScoreNine) {
		this.nutritionalScoreNine = nutritionalScoreNine;
	}

	public Long getNutritionalScoreTen() {
		return nutritionalScoreTen;
	}

	public void setNutritionalScoreTen(Long nutritionalScoreTen) {
		this.nutritionalScoreTen = nutritionalScoreTen;
	}

	public String getNsiScore() {
		return nsiScore;
	}

	public void setNsiScore(String nsiScore) {
		this.nsiScore = nsiScore;
	}

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public String getReserved_1() {
		return reserved_1;
	}

	public void setReserved_1(String reserved_1) {
		this.reserved_1 = reserved_1;
	}

	public String getReserved_2() {
		return reserved_2;
	}

	public void setReserved_2(String reserved_2) {
		this.reserved_2 = reserved_2;
	}

	public String getReserved_3() {
		return reserved_3;
	}

	public void setReserved_3(String reserved_3) {
		this.reserved_3 = reserved_3;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public boolean isHighRisk() {
		return highRisk;
	}

	public void setHighRisk(boolean highRisk) {
		this.highRisk = highRisk;
	}

	public boolean isModerateRisk() {
		return moderateRisk;
	}

	public void setModerateRisk(boolean moderateRisk) {
		this.moderateRisk = moderateRisk;
	}

	public boolean isLowRisk() {
		return lowRisk;
	}

	public void setLowRisk(boolean lowRisk) {
		this.lowRisk = lowRisk;
	}
}
