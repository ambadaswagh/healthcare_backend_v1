package com.healthcare.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
@Data
public class HealthStatusDTO implements Serializable{

	private Long id;

    private Long userId;

	private String primaryPhysician;

    private String clinicHmo;

    private String hospital;

    private String other;

    private Date dateOfLastPCP;

    private String participantIllnessAndDisability;

    private boolean alzheimers;

    private boolean arthritis;

    private boolean cancer;

    private boolean cellulitis;

    private boolean colitis;

    private boolean colostomy;

    private boolean constipation;

    private boolean diabetes;

    private boolean decubitusUlcers;

    private boolean developmentalDisabilities;

    private boolean digestiveProblems;

    private boolean diverticulitis;

    private boolean gallBladderDisease;

    private boolean hearingImpairment;

    private boolean hiatalHernia;

    private boolean highCholesterol;

    private boolean legallyBlind;

    private boolean liverDisease;

    private boolean lowBloodPressure;

    private boolean renalDisease;

    private boolean dependent;

    private boolean stroke;

    private boolean paralysis;

    private boolean speechProblems;

    private boolean swallowingDifficulties;

    private boolean tasteImpairment;

    private boolean ulcer;

    private boolean visualImpairment;

    private boolean alcoholism;

    private boolean asthma;

    private boolean cardiovascularDisorder;

    private boolean chronicObstructivePulmonaryDisease;

    private boolean congestiveHeartFailure;

    private boolean chronicPain;

    private boolean diarrhea;

    private boolean dialysis;

    private boolean dehydration;

    private boolean dentalProblems;

    private boolean dementia;

    private boolean frequentFalls;

    private boolean fractures;

    private boolean heartDisease;

    private boolean highBloodPressure;

    private boolean oxygen;

    private boolean hypoglycemia;

    private boolean hiv;

    private boolean perniciousAnemia;

    private boolean osteoporosis;

    private boolean parkinson;

    private boolean respiratoryProblems;

    private boolean shingles;

    private boolean urinaryTractInfection;

    private boolean traumaticbrainInjury;

    private boolean tremors;

    private boolean others;

    private String haveAssistiveDevice;

    private boolean wheelchair;

    private boolean walker;

    private boolean scooter;

    private boolean denture;

    private boolean partial;

    private boolean hearingAid;

    private boolean cane;

    private boolean bedRail;

    private boolean accessibleVehicle;

    private boolean glasses;

    private boolean full;

    private String otherHealthStatus;

    private Long understandOthers;

    private  Long hospitalizedOrEmergencyRoomLastSixMonths;

    private String reserved_1;

    private String reserved_2;

    private String reserved_3;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPrimaryPhysician() {
        return primaryPhysician;
    }

    public void setPrimaryPhysician(String primaryPhysician) {
        this.primaryPhysician = primaryPhysician;
    }

    public String getClinicHmo() {
        return clinicHmo;
    }

    public void setClinicHmo(String clinicHmo) {
        this.clinicHmo = clinicHmo;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public Date getDateOfLastPCP() {
        return dateOfLastPCP;
    }

    public void setDateOfLastPCP(Date dateOfLastPCP) {
        this.dateOfLastPCP = dateOfLastPCP;
    }

    public String getParticipantIllnessAndDisability() {
        return participantIllnessAndDisability;
    }

    public void setParticipantIllnessAndDisability(String participantIllnessAndDisability) {
        this.participantIllnessAndDisability = participantIllnessAndDisability;
    }

    public boolean isAlzheimers() {
        return alzheimers;
    }

    public void setAlzheimers(boolean alzheimers) {
        this.alzheimers = alzheimers;
    }

    public boolean isArthritis() {
        return arthritis;
    }

    public void setArthritis(boolean arthritis) {
        this.arthritis = arthritis;
    }

    public boolean isCancer() {
        return cancer;
    }

    public void setCancer(boolean cancer) {
        this.cancer = cancer;
    }

    public boolean isCellulitis() {
        return cellulitis;
    }

    public void setCellulitis(boolean cellulitis) {
        this.cellulitis = cellulitis;
    }

    public boolean isColitis() {
        return colitis;
    }

    public void setColitis(boolean colitis) {
        this.colitis = colitis;
    }

    public boolean isColostomy() {
        return colostomy;
    }

    public void setColostomy(boolean colostomy) {
        this.colostomy = colostomy;
    }

    public boolean isConstipation() {
        return constipation;
    }

    public void setConstipation(boolean constipation) {
        this.constipation = constipation;
    }

    public boolean isDiabetes() {
        return diabetes;
    }

    public void setDiabetes(boolean diabetes) {
        this.diabetes = diabetes;
    }

    public boolean isDecubitusUlcers() {
        return decubitusUlcers;
    }

    public void setDecubitusUlcers(boolean decubitusUlcers) {
        this.decubitusUlcers = decubitusUlcers;
    }

    public boolean isDevelopmentalDisabilities() {
        return developmentalDisabilities;
    }

    public void setDevelopmentalDisabilities(boolean developmentalDisabilities) {
        this.developmentalDisabilities = developmentalDisabilities;
    }

    public boolean isDigestiveProblems() {
        return digestiveProblems;
    }

    public void setDigestiveProblems(boolean digestiveProblems) {
        this.digestiveProblems = digestiveProblems;
    }

    public boolean isDiverticulitis() {
        return diverticulitis;
    }

    public void setDiverticulitis(boolean diverticulitis) {
        this.diverticulitis = diverticulitis;
    }

    public boolean isGallBladderDisease() {
        return gallBladderDisease;
    }

    public void setGallBladderDisease(boolean gallBladderDisease) {
        this.gallBladderDisease = gallBladderDisease;
    }

    public boolean isHearingImpairment() {
        return hearingImpairment;
    }

    public void setHearingImpairment(boolean hearingImpairment) {
        this.hearingImpairment = hearingImpairment;
    }

    public boolean isHiatalHernia() {
        return hiatalHernia;
    }

    public void setHiatalHernia(boolean hiatalHernia) {
        this.hiatalHernia = hiatalHernia;
    }

    public boolean isHighCholesterol() {
        return highCholesterol;
    }

    public void setHighCholesterol(boolean highCholesterol) {
        this.highCholesterol = highCholesterol;
    }

    public boolean isLegallyBlind() {
        return legallyBlind;
    }

    public void setLegallyBlind(boolean legallyBlind) {
        this.legallyBlind = legallyBlind;
    }

    public boolean isLiverDisease() {
        return liverDisease;
    }

    public void setLiverDisease(boolean liverDisease) {
        this.liverDisease = liverDisease;
    }

    public boolean isLowBloodPressure() {
        return lowBloodPressure;
    }

    public void setLowBloodPressure(boolean lowBloodPressure) {
        this.lowBloodPressure = lowBloodPressure;
    }

    public boolean isRenalDisease() {
        return renalDisease;
    }

    public void setRenalDisease(boolean renalDisease) {
        this.renalDisease = renalDisease;
    }

    public boolean isDependent() {
        return dependent;
    }

    public void setDependent(boolean dependent) {
        this.dependent = dependent;
    }

    public boolean isStroke() {
        return stroke;
    }

    public void setStroke(boolean stroke) {
        this.stroke = stroke;
    }

    public boolean isParalysis() {
        return paralysis;
    }

    public void setParalysis(boolean paralysis) {
        this.paralysis = paralysis;
    }

    public boolean isSpeechProblems() {
        return speechProblems;
    }

    public void setSpeechProblems(boolean speechProblems) {
        this.speechProblems = speechProblems;
    }

    public boolean isSwallowingDifficulties() {
        return swallowingDifficulties;
    }

    public void setSwallowingDifficulties(boolean swallowingDifficulties) {
        this.swallowingDifficulties = swallowingDifficulties;
    }

    public boolean isTasteImpairment() {
        return tasteImpairment;
    }

    public void setTasteImpairment(boolean tasteImpairment) {
        this.tasteImpairment = tasteImpairment;
    }

    public boolean isUlcer() {
        return ulcer;
    }

    public void setUlcer(boolean ulcer) {
        this.ulcer = ulcer;
    }

    public boolean isVisualImpairment() {
        return visualImpairment;
    }

    public void setVisualImpairment(boolean visualImpairment) {
        this.visualImpairment = visualImpairment;
    }

    public boolean isAlcoholism() {
        return alcoholism;
    }

    public void setAlcoholism(boolean alcoholism) {
        this.alcoholism = alcoholism;
    }

    public boolean isAsthma() {
        return asthma;
    }

    public void setAsthma(boolean asthma) {
        this.asthma = asthma;
    }

    public boolean isCardiovascularDisorder() {
        return cardiovascularDisorder;
    }

    public void setCardiovascularDisorder(boolean cardiovascularDisorder) {
        this.cardiovascularDisorder = cardiovascularDisorder;
    }

    public boolean isChronicObstructivePulmonaryDisease() {
        return chronicObstructivePulmonaryDisease;
    }

    public void setChronicObstructivePulmonaryDisease(boolean chronicObstructivePulmonaryDisease) {
        this.chronicObstructivePulmonaryDisease = chronicObstructivePulmonaryDisease;
    }

    public boolean isCongestiveHeartFailure() {
        return congestiveHeartFailure;
    }

    public void setCongestiveHeartFailure(boolean congestiveHeartFailure) {
        this.congestiveHeartFailure = congestiveHeartFailure;
    }

    public boolean isChronicPain() {
        return chronicPain;
    }

    public void setChronicPain(boolean chronicPain) {
        this.chronicPain = chronicPain;
    }

    public boolean isDiarrhea() {
        return diarrhea;
    }

    public void setDiarrhea(boolean diarrhea) {
        this.diarrhea = diarrhea;
    }

    public boolean isDialysis() {
        return dialysis;
    }

    public void setDialysis(boolean dialysis) {
        this.dialysis = dialysis;
    }

    public boolean isDehydration() {
        return dehydration;
    }

    public void setDehydration(boolean dehydration) {
        this.dehydration = dehydration;
    }

    public boolean isDentalProblems() {
        return dentalProblems;
    }

    public void setDentalProblems(boolean dentalProblems) {
        this.dentalProblems = dentalProblems;
    }

    public boolean isDementia() {
        return dementia;
    }

    public void setDementia(boolean dementia) {
        this.dementia = dementia;
    }

    public boolean isFrequentFalls() {
        return frequentFalls;
    }

    public void setFrequentFalls(boolean frequentFalls) {
        this.frequentFalls = frequentFalls;
    }

    public boolean isFractures() {
        return fractures;
    }

    public void setFractures(boolean fractures) {
        this.fractures = fractures;
    }

    public boolean isHeartDisease() {
        return heartDisease;
    }

    public void setHeartDisease(boolean heartDisease) {
        this.heartDisease = heartDisease;
    }

    public boolean isHighBloodPressure() {
        return highBloodPressure;
    }

    public void setHighBloodPressure(boolean highBloodPressure) {
        this.highBloodPressure = highBloodPressure;
    }

    public boolean isOxygen() {
        return oxygen;
    }

    public void setOxygen(boolean oxygen) {
        this.oxygen = oxygen;
    }

    public boolean isHypoglycemia() {
        return hypoglycemia;
    }

    public void setHypoglycemia(boolean hypoglycemia) {
        this.hypoglycemia = hypoglycemia;
    }

    public boolean isHiv() {
        return hiv;
    }

    public void setHiv(boolean hiv) {
        this.hiv = hiv;
    }

    public boolean isPerniciousAnemia() {
        return perniciousAnemia;
    }

    public void setPerniciousAnemia(boolean perniciousAnemia) {
        this.perniciousAnemia = perniciousAnemia;
    }

    public boolean isOsteoporosis() {
        return osteoporosis;
    }

    public void setOsteoporosis(boolean osteoporosis) {
        this.osteoporosis = osteoporosis;
    }

    public boolean isParkinson() {
        return parkinson;
    }

    public void setParkinson(boolean parkinson) {
        this.parkinson = parkinson;
    }

    public boolean isRespiratoryProblems() {
        return respiratoryProblems;
    }

    public void setRespiratoryProblems(boolean respiratoryProblems) {
        this.respiratoryProblems = respiratoryProblems;
    }

    public boolean isShingles() {
        return shingles;
    }

    public void setShingles(boolean shingles) {
        this.shingles = shingles;
    }

    public boolean isUrinaryTractInfection() {
        return urinaryTractInfection;
    }

    public void setUrinaryTractInfection(boolean urinaryTractInfection) {
        this.urinaryTractInfection = urinaryTractInfection;
    }

    public boolean isTraumaticbrainInjury() {
        return traumaticbrainInjury;
    }

    public void setTraumaticbrainInjury(boolean traumaticbrainInjury) {
        this.traumaticbrainInjury = traumaticbrainInjury;
    }

    public boolean isTremors() {
        return tremors;
    }

    public void setTremors(boolean tremors) {
        this.tremors = tremors;
    }

    public boolean isOthers() {
        return others;
    }

    public void setOthers(boolean others) {
        this.others = others;
    }

    public String getHaveAssistiveDevice() {
        return haveAssistiveDevice;
    }

    public void setHaveAssistiveDevice(String haveAssistiveDevice) {
        this.haveAssistiveDevice = haveAssistiveDevice;
    }

    public boolean isWheelchair() {
        return wheelchair;
    }

    public void setWheelchair(boolean wheelchair) {
        this.wheelchair = wheelchair;
    }

    public boolean isWalker() {
        return walker;
    }

    public void setWalker(boolean walker) {
        this.walker = walker;
    }

    public boolean isScooter() {
        return scooter;
    }

    public void setScooter(boolean scooter) {
        this.scooter = scooter;
    }

    public boolean isDenture() {
        return denture;
    }

    public void setDenture(boolean denture) {
        this.denture = denture;
    }

    public boolean isPartial() {
        return partial;
    }

    public void setPartial(boolean partial) {
        this.partial = partial;
    }

    public boolean isHearingAid() {
        return hearingAid;
    }

    public void setHearingAid(boolean hearingAid) {
        this.hearingAid = hearingAid;
    }

    public boolean isCane() {
        return cane;
    }

    public void setCane(boolean cane) {
        this.cane = cane;
    }

    public boolean isBedRail() {
        return bedRail;
    }

    public void setBedRail(boolean bedRail) {
        this.bedRail = bedRail;
    }

    public boolean isAccessibleVehicle() {
        return accessibleVehicle;
    }

    public void setAccessibleVehicle(boolean accessibleVehicle) {
        this.accessibleVehicle = accessibleVehicle;
    }

    public boolean isGlasses() {
        return glasses;
    }

    public void setGlasses(boolean glasses) {
        this.glasses = glasses;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public String getOtherHealthStatus() {
        return otherHealthStatus;
    }

    public void setOtherHealthStatus(String otherHealthStatus) {
        this.otherHealthStatus = otherHealthStatus;
    }

    public Long getUnderstandOthers() {
        return understandOthers;
    }

    public void setUnderstandOthers(Long understandOthers) {
        this.understandOthers = understandOthers;
    }

    public Long getHospitalizedOrEmergencyRoomLastSixMonths() {
        return hospitalizedOrEmergencyRoomLastSixMonths;
    }

    public void setHospitalizedOrEmergencyRoomLastSixMonths(Long hospitalizedOrEmergencyRoomLastSixMonths) {
        this.hospitalizedOrEmergencyRoomLastSixMonths = hospitalizedOrEmergencyRoomLastSixMonths;
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
