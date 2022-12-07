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
@Table(name = "chat_section")
public class ChatSection implements java.io.Serializable {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Basic
    @Column(name = "user_ids", nullable = false)
    private String userIds;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "from_user", referencedColumnName = "id", nullable = false)
    private Admin fromUser;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "to_user", referencedColumnName = "id", nullable = false)
    private Admin toUser;

    @Basic
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Basic
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}
