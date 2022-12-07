package com.healthcare.chat.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthcare.model.entity.Admin;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Data
@DynamicInsert
@DynamicUpdate
@Table(name = "chat_group")
public class ChatGroup implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "created_by", referencedColumnName = "id", nullable = false)
    private Admin createdBy;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Basic
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}
