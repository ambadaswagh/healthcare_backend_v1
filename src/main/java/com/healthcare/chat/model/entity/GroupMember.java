package com.healthcare.chat.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthcare.model.entity.Admin;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 *
 * @author tungn
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Data
@DynamicInsert
@DynamicUpdate
@Table(name = "group_member")
public class GroupMember implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private Admin user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
    private ChatGroup group;

    @Basic
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Basic
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @Basic
    @Column(name = "status", nullable = false)
    private long status;
}
