package com.healthcare.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tables")
@EqualsAndHashCode(callSuper = true)
public @Data class TableEntity extends Audit implements Serializable {

	private static final long serialVersionUID = 512962093355769597L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @Column(name = "table_called")
    private String tableCalled;

    @Column(name = "table_capacity")
    private Integer tableCapacity;
    
    @Column(name = "status")
    private Integer status;

}