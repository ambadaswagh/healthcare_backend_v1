package com.healthcare.model.entity.assessment;

import com.healthcare.model.entity.Audit;
import com.healthcare.model.entity.User;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by inimn on 25/12/2017.
 */
@Entity
@Table(name = "health_status")
public @Data class HealthStatus extends Audit implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "primary_physician")
    private String primaryPhysician;

    @Column(name = "clinic_hmo")
    private String clinicHmo;

    @Column(name = "hospital")
    private String hospital;

    @Column(name = "other")
    private String other;

    @Column(name = "date_of_last_PCP")
    private Date dateOfLastPCP;

    @Column(name = "participant_illness_and_disability")
    private String participantIllnessAndDisability;

    @Column(name = "other_comment")
    private String otherComment;

    @Column(name = "have_assistive_device")
    private String haveAssistiveDevice;

    @Column(name = "understand_others")
    private Long understandOthers;

    @Column(name = "hospitalized_or_emergency_room_last_6_months")
    private  Long hospitalizedOrEmergencyRoomLastSixMonths;

    @Column(name = "reserved_1")
    private String reserved_1;

    @Column(name = "reserved_2")
    private String reserved_2;

    @Column(name = "reserved_3")
    private String reserved_3;
    
    @Column(name = "user_id")
    private Long userId;
    
}
