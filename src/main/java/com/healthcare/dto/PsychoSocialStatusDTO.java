package com.healthcare.dto;

import com.healthcare.model.entity.Audit;

import java.io.Serializable;

public class PsychoSocialStatusDTO extends Audit implements Serializable{

    private Long id;

    private String psychoSocialCondition;

    private boolean dementia;

    private boolean cooperative;

    private boolean anger;

    private boolean alert;

    private boolean delirium;

    private boolean depressed;

    private boolean diminishedInterpersonalSkills;

    private boolean disruptiveSocially;

    private boolean hallucinations;

    private boolean hoarding;

    private boolean impairedDecisionMakingIsolation;

    private boolean lonely;

    private boolean physicalAggressive;

    private boolean memoryDeficit;

    private boolean resistanceToCare;

    private boolean sleepingProblems;

    private boolean suicidalThoughts;

    private boolean selfNeglect;

    private boolean shortTermMemoryDeficit;

    private boolean suicidalBehavior;

    private boolean verbalDisrutive;

    private boolean wandering;

    private boolean  worriedOrAnxious;

    private boolean withdraw;

    private boolean other;

    private String otherComment;

    private Long evidenceSubstanceAbuse;

    private String evidenceSubstanceAbuseComment;

    private Long behaviorReported;

    private String behaviorReportedComment;

    private Long mentalHealthProblem;

    private String mentalHealthProblemComment;

    private Long mentalHealthTreatment;

    private String mentalHealthTreatmentComment;

    private Long mentalHealthEvaluation;

    private String mentalHealthEvaluationComment;

    private Long participanInProgram;

    private String participanInProgramComment;

    private String comment;

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

    public String getPsychoSocialCondition() {
        return psychoSocialCondition;
    }

    public void setPsychoSocialCondition(String psychoSocialCondition) {
        this.psychoSocialCondition = psychoSocialCondition;
    }

    public boolean isDementia() {
        return dementia;
    }

    public void setDementia(boolean dementia) {
        this.dementia = dementia;
    }

    public boolean isCooperative() {
        return cooperative;
    }

    public void setCooperative(boolean cooperative) {
        this.cooperative = cooperative;
    }

    public boolean isAnger() {
        return anger;
    }

    public void setAnger(boolean anger) {
        this.anger = anger;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public boolean isDelirium() {
        return delirium;
    }

    public void setDelirium(boolean delirium) {
        this.delirium = delirium;
    }

    public boolean isDepressed() {
        return depressed;
    }

    public void setDepressed(boolean depressed) {
        this.depressed = depressed;
    }

    public boolean isDiminishedInterpersonalSkills() {
        return diminishedInterpersonalSkills;
    }

    public void setDiminishedInterpersonalSkills(boolean diminishedInterpersonalSkills) {
        this.diminishedInterpersonalSkills = diminishedInterpersonalSkills;
    }

    public boolean isDisruptiveSocially() {
        return disruptiveSocially;
    }

    public void setDisruptiveSocially(boolean disruptiveSocially) {
        this.disruptiveSocially = disruptiveSocially;
    }

    public boolean isHallucinations() {
        return hallucinations;
    }

    public void setHallucinations(boolean hallucinations) {
        this.hallucinations = hallucinations;
    }

    public boolean isHoarding() {
        return hoarding;
    }

    public void setHoarding(boolean hoarding) {
        this.hoarding = hoarding;
    }

    public boolean isImpairedDecisionMakingIsolation() {
        return impairedDecisionMakingIsolation;
    }

    public void setImpairedDecisionMakingIsolation(boolean impairedDecisionMakingIsolation) {
        this.impairedDecisionMakingIsolation = impairedDecisionMakingIsolation;
    }

    public boolean isLonely() {
        return lonely;
    }

    public void setLonely(boolean lonely) {
        this.lonely = lonely;
    }

    public boolean isPhysicalAggressive() {
        return physicalAggressive;
    }

    public void setPhysicalAggressive(boolean physicalAggressive) {
        this.physicalAggressive = physicalAggressive;
    }

    public boolean isMemoryDeficit() {
        return memoryDeficit;
    }

    public void setMemoryDeficit(boolean memoryDeficit) {
        this.memoryDeficit = memoryDeficit;
    }

    public boolean isResistanceToCare() {
        return resistanceToCare;
    }

    public void setResistanceToCare(boolean resistanceToCare) {
        this.resistanceToCare = resistanceToCare;
    }

    public boolean isSleepingProblems() {
        return sleepingProblems;
    }

    public void setSleepingProblems(boolean sleepingProblems) {
        this.sleepingProblems = sleepingProblems;
    }

    public boolean isSuicidalThoughts() {
        return suicidalThoughts;
    }

    public void setSuicidalThoughts(boolean suicidalThoughts) {
        this.suicidalThoughts = suicidalThoughts;
    }

    public boolean isSelfNeglect() {
        return selfNeglect;
    }

    public void setSelfNeglect(boolean selfNeglect) {
        this.selfNeglect = selfNeglect;
    }

    public boolean isShortTermMemoryDeficit() {
        return shortTermMemoryDeficit;
    }

    public void setShortTermMemoryDeficit(boolean shortTermMemoryDeficit) {
        this.shortTermMemoryDeficit = shortTermMemoryDeficit;
    }

    public boolean isSuicidalBehavior() {
        return suicidalBehavior;
    }

    public void setSuicidalBehavior(boolean suicidalBehavior) {
        this.suicidalBehavior = suicidalBehavior;
    }

    public boolean isVerbalDisrutive() {
        return verbalDisrutive;
    }

    public void setVerbalDisrutive(boolean verbalDisrutive) {
        this.verbalDisrutive = verbalDisrutive;
    }

    public boolean isWandering() {
        return wandering;
    }

    public void setWandering(boolean wandering) {
        this.wandering = wandering;
    }

    public boolean isWorriedOrAnxious() {
        return worriedOrAnxious;
    }

    public void setWorriedOrAnxious(boolean worriedOrAnxious) {
        this.worriedOrAnxious = worriedOrAnxious;
    }

    public boolean isWithdraw() {
        return withdraw;
    }

    public void setWithdraw(boolean withdraw) {
        this.withdraw = withdraw;
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

    public Long getEvidenceSubstanceAbuse() {
        return evidenceSubstanceAbuse;
    }

    public void setEvidenceSubstanceAbuse(Long evidenceSubstanceAbuse) {
        this.evidenceSubstanceAbuse = evidenceSubstanceAbuse;
    }

    public String getEvidenceSubstanceAbuseComment() {
        return evidenceSubstanceAbuseComment;
    }

    public void setEvidenceSubstanceAbuseComment(String evidenceSubstanceAbuseComment) {
        this.evidenceSubstanceAbuseComment = evidenceSubstanceAbuseComment;
    }

    public Long getBehaviorReported() {
        return behaviorReported;
    }

    public void setBehaviorReported(Long behaviorReported) {
        this.behaviorReported = behaviorReported;
    }

    public String getBehaviorReportedComment() {
        return behaviorReportedComment;
    }

    public void setBehaviorReportedComment(String behaviorReportedComment) {
        this.behaviorReportedComment = behaviorReportedComment;
    }

    public Long getMentalHealthProblem() {
        return mentalHealthProblem;
    }

    public void setMentalHealthProblem(Long mentalHealthProblem) {
        this.mentalHealthProblem = mentalHealthProblem;
    }

    public String getMentalHealthProblemComment() {
        return mentalHealthProblemComment;
    }

    public void setMentalHealthProblemComment(String mentalHealthProblemComment) {
        this.mentalHealthProblemComment = mentalHealthProblemComment;
    }

    public Long getMentalHealthTreatment() {
        return mentalHealthTreatment;
    }

    public void setMentalHealthTreatment(Long mentalHealthTreatment) {
        this.mentalHealthTreatment = mentalHealthTreatment;
    }

    public String getMentalHealthTreatmentComment() {
        return mentalHealthTreatmentComment;
    }

    public void setMentalHealthTreatmentComment(String mentalHealthTreatmentComment) {
        this.mentalHealthTreatmentComment = mentalHealthTreatmentComment;
    }

    public Long getMentalHealthEvaluation() {
        return mentalHealthEvaluation;
    }

    public void setMentalHealthEvaluation(Long mentalHealthEvaluation) {
        this.mentalHealthEvaluation = mentalHealthEvaluation;
    }

    public String getMentalHealthEvaluationComment() {
        return mentalHealthEvaluationComment;
    }

    public void setMentalHealthEvaluationComment(String mentalHealthEvaluationComment) {
        this.mentalHealthEvaluationComment = mentalHealthEvaluationComment;
    }

    public Long getParticipanInProgram() {
        return participanInProgram;
    }

    public void setParticipanInProgram(Long participanInProgram) {
        this.participanInProgram = participanInProgram;
    }

    public String getParticipanInProgramComment() {
        return participanInProgramComment;
    }

    public void setParticipanInProgramComment(String participanInProgramComment) {
        this.participanInProgramComment = participanInProgramComment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

}
