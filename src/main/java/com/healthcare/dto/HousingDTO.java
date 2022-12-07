package com.healthcare.dto;

import java.io.Serializable;
import java.util.List;

import com.healthcare.model.entity.Audit;

import lombok.Data;
@Data
public class HousingDTO implements Serializable{

	private Long id;

    private String typeOfHousing;

    private boolean multiUnitHousing;

    private boolean singleFamilyHome;

    private boolean owns;

    private boolean rents;

    private boolean other;

    private String otherComment;

    private String homeSafetyChecklist;

    private boolean hscAccumulatedGarbadge;

    private boolean hscCarbonMonoxide;

    private boolean hscDoorwayWidths;

    private boolean hscLooseScatterRugs;

    private boolean hscLampOrLightSwitch;

    private boolean hscNoRubberMats;

    private boolean hscSmokeDetectors;

    private boolean hscTelephoneAndAppliance;

    private boolean hscTrafficLane;

    private boolean hscHandrails;

    private boolean hscBadOdors;

    private boolean hscFloorsAndStairways;

    private boolean hscNoLightsIn;

    private boolean hscNoLocksOnDoors;

    private boolean hscNoGrabBar;

    private boolean hscStairways;

    private boolean hscOther;

    private String hscOtherComment;

    private Long isNeighborhoddSafetyAnIssue;

    private String comment;

    private Long userId;

    private String otherHomeSafety;

    private String otherHousing;

    private String reserved_1;

    private String reserved_2;

    private String reserved_3;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeOfHousing() {
        return typeOfHousing;
    }

    public void setTypeOfHousing(String typeOfHousing) {
        this.typeOfHousing = typeOfHousing;
    }

    public boolean isMultiUnitHousing() {
        return multiUnitHousing;
    }

    public void setMultiUnitHousing(boolean multiUnitHousing) {
        this.multiUnitHousing = multiUnitHousing;
    }

    public boolean isSingleFamilyHome() {
        return singleFamilyHome;
    }

    public void setSingleFamilyHome(boolean singleFamilyHome) {
        this.singleFamilyHome = singleFamilyHome;
    }

    public boolean isOwns() {
        return owns;
    }

    public void setOwns(boolean owns) {
        this.owns = owns;
    }

    public boolean isRents() {
        return rents;
    }

    public void setRents(boolean rents) {
        this.rents = rents;
    }

    public boolean isOther() {
        return other;
    }

    public void setOther(boolean other) {
        this.other = other;
    }

    public String getOtherComment() {
        return otherComment;
    }

    public void setOtherComment(String otherComment) {
        this.otherComment = otherComment;
    }

    public String getHomeSafetyChecklist() {
        return homeSafetyChecklist;
    }

    public void setHomeSafetyChecklist(String homeSafetyChecklist) {
        this.homeSafetyChecklist = homeSafetyChecklist;
    }

    public boolean isHscAccumulatedGarbadge() {
        return hscAccumulatedGarbadge;
    }

    public void setHscAccumulatedGarbadge(boolean hscAccumulatedGarbadge) {
        this.hscAccumulatedGarbadge = hscAccumulatedGarbadge;
    }

    public boolean isHscCarbonMonoxide() {
        return hscCarbonMonoxide;
    }

    public void setHscCarbonMonoxide(boolean hscCarbonMonoxide) {
        this.hscCarbonMonoxide = hscCarbonMonoxide;
    }

    public boolean isHscDoorwayWidths() {
        return hscDoorwayWidths;
    }

    public void setHscDoorwayWidths(boolean hscDoorwayWidths) {
        this.hscDoorwayWidths = hscDoorwayWidths;
    }

    public boolean isHscLooseScatterRugs() {
        return hscLooseScatterRugs;
    }

    public void setHscLooseScatterRugs(boolean hscLooseScatterRugs) {
        this.hscLooseScatterRugs = hscLooseScatterRugs;
    }

    public boolean isHscLampOrLightSwitch() {
        return hscLampOrLightSwitch;
    }

    public void setHscLampOrLightSwitch(boolean hscLampOrLightSwitch) {
        this.hscLampOrLightSwitch = hscLampOrLightSwitch;
    }

    public boolean isHscNoRubberMats() {
        return hscNoRubberMats;
    }

    public void setHscNoRubberMats(boolean hscNoRubberMats) {
        this.hscNoRubberMats = hscNoRubberMats;
    }

    public boolean isHscSmokeDetectors() {
        return hscSmokeDetectors;
    }

    public void setHscSmokeDetectors(boolean hscSmokeDetectors) {
        this.hscSmokeDetectors = hscSmokeDetectors;
    }

    public boolean isHscTelephoneAndAppliance() {
        return hscTelephoneAndAppliance;
    }

    public void setHscTelephoneAndAppliance(boolean hscTelephoneAndAppliance) {
        this.hscTelephoneAndAppliance = hscTelephoneAndAppliance;
    }

    public boolean isHscTrafficLane() {
        return hscTrafficLane;
    }

    public void setHscTrafficLane(boolean hscTrafficLane) {
        this.hscTrafficLane = hscTrafficLane;
    }

    public boolean isHscHandrails() {
        return hscHandrails;
    }

    public void setHscHandrails(boolean hscHandrails) {
        this.hscHandrails = hscHandrails;
    }

    public boolean isHscBadOdors() {
        return hscBadOdors;
    }

    public void setHscBadOdors(boolean hscBadOdors) {
        this.hscBadOdors = hscBadOdors;
    }

    public boolean isHscFloorsAndStairways() {
        return hscFloorsAndStairways;
    }

    public void setHscFloorsAndStairways(boolean hscFloorsAndStairways) {
        this.hscFloorsAndStairways = hscFloorsAndStairways;
    }

    public boolean isHscNoLightsIn() {
        return hscNoLightsIn;
    }

    public void setHscNoLightsIn(boolean hscNoLightsIn) {
        this.hscNoLightsIn = hscNoLightsIn;
    }

    public boolean isHscNoLocksOnDoors() {
        return hscNoLocksOnDoors;
    }

    public void setHscNoLocksOnDoors(boolean hscNoLocksOnDoors) {
        this.hscNoLocksOnDoors = hscNoLocksOnDoors;
    }

    public boolean isHscNoGrabBar() {
        return hscNoGrabBar;
    }

    public void setHscNoGrabBar(boolean hscNoGrabBar) {
        this.hscNoGrabBar = hscNoGrabBar;
    }

    public boolean isHscStairways() {
        return hscStairways;
    }

    public void setHscStairways(boolean hscStairways) {
        this.hscStairways = hscStairways;
    }

    public boolean isHscOther() {
        return hscOther;
    }

    public void setHscOther(boolean hscOther) {
        this.hscOther = hscOther;
    }

    public String getHscOtherComment() {
        return hscOtherComment;
    }

    public void setHscOtherComment(String hscOtherComment) {
        this.hscOtherComment = hscOtherComment;
    }

    public Long getIsNeighborhoddSafetyAnIssue() {
        return isNeighborhoddSafetyAnIssue;
    }

    public void setIsNeighborhoddSafetyAnIssue(Long isNeighborhoddSafetyAnIssue) {
        this.isNeighborhoddSafetyAnIssue = isNeighborhoddSafetyAnIssue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOtherHomeSafety() {
        return otherHomeSafety;
    }

    public void setOtherHomeSafety(String otherHomeSafety) {
        this.otherHomeSafety = otherHomeSafety;
    }

    public String getOtherHousing() {
        return otherHousing;
    }

    public void setOtherHousing(String otherHousing) {
        this.otherHousing = otherHousing;
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
}
