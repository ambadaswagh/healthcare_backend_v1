package com.healthcare.model.entity.assessment;

import com.healthcare.model.entity.Audit;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by inimn on 25/12/2017.
 */
@Entity
@Table(name = "psycho_social_status")
public @Data class PsychoSocialStatus extends Audit implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "psycho_social_condition")
    private String psychoSocialCondition;

    @Column(name= "other_comment")
    private String otherComment;

    @Column(name= "evidence_substance_abuse")
    private Long evidenceSubstanceAbuse;

    @Column(name = "evidence_substance_abuse_comment")
    private String evidenceSubstanceAbuseComment;

    @Column(name= "behavior_reported")
    private Long behaviorReported;

    @Column(name = "behavior_reported_comment")
    private String behaviorReportedComment;

    @Column(name= "mental_health_problem")
    private Long mentalHealthProblem;

    @Column(name = "mental_health_problem_comment")
    private String mentalHealthProblemComment;

    @Column(name= "mental_health_treatment")
    private Long mentalHealthTreatment;

    @Column(name = "mental_health_treatment_comment")
    private String mentalHealthTreatmentComment;

    @Column(name= "mental_health_evaluation")
    private Long mentalHealthEvaluation;

    @Column(name = "mental_health_evaluation_comment")
    private String mentalHealthEvaluationComment;

    @Column(name= "participant_in_program")
    private Long participanInProgram;

    @Column(name = "participant_in_program_comment")
    private String participanInProgramComment;

    @Column(name= "comment")
    private String comment;

    @Column(name = "reserved_1")
    private String reserved_1;

    @Column(name = "reserved_2")
    private String reserved_2;

    @Column(name = "reserved_3")
    private String reserved_3;

    @Column(name = "user_id")
    private Long userId;
}
