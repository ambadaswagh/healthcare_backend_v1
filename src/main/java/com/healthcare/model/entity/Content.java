package com.healthcare.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "content")
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class Content extends Audit implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 7211707812835215028L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String title;
    @Column
    @Lob
    private byte[] content;
    @Column
    private Integer status;
    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "content_type")
    private ContentType contentType;
    
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

}
