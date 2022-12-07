package com.healthcare.model.entity.assessment;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by inimn on 25/12/2017.
 */

import com.healthcare.model.entity.Audit;
import com.healthcare.model.entity.User;

import lombok.Data;

@Entity
@Table(name = "housing")
public @Data class Housing extends Audit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "type_of_Housing")
    private String typeOfHousing;

    @Column(name = "other_comment")
    private String otherComment;

    @Column(name = "home_safety_checklist")
    private String homeSafetyChecklist;

    @Column(name = "hsc_other_comment")
    private String hscOtherComment;

    @Column(name = "is_neighborhodd_safety_an_issue")
    private Long isNeighborhoddSafetyAnIssue;

    @Column(name = "comment")
    private String comment;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "other_home_safety")
    private String otherHomeSafety;
    
    @Column(name = "other_housing")
    private String otherHousing;

    @Column(name = "reserved_1")
    private String reserved_1;

    @Column(name = "reserved_2")
    private String reserved_2;

    @Column(name = "reserved_3")
    private String reserved_3;

}
