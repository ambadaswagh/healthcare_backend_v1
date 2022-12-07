package com.healthcare.model.entity;

import com.healthcare.conf.StringListConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "notification")
@EqualsAndHashCode(callSuper = true)
@Data
public class Notification extends Audit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @Column(name = "type")
    private String type;

    @Column(name = "email")
    private String email;

    @Column(name = "sms")
    private String sms;

    @Column(name = "frequency")
    private Integer frequency;

    @Column(name = "notification_time")
    private Timestamp notificationTime;
}
