package com.healthcare.model.entity.assessment;

/**
 * Created by inimn on 25/12/2017.
 */

import com.healthcare.model.entity.Audit;
import com.healthcare.model.entity.User;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "assessment")
public @Data class Assessment extends Audit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "label")
    private String label;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
