package com.healthcare.model.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.common.base.Objects;
import com.healthcare.dto.TodolistDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/*
 * Github #159
 */

@Entity
@Table(name = "todolist")
@EqualsAndHashCode(callSuper = true)
public @Data class Todolist extends Audit implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Min(0)
	private Long id;
	private String num;
	private String content;
	private String priority = "HIGH";
	@Nullable
	private String color = "#000000";
	@Min(0)
	@Max(1)
	private int status = 0;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admin_id")
	@NotFound(action = NotFoundAction.IGNORE)
	private Admin admin;

	public void fromDto(TodolistDTO dto, Admin admin) {
		BeanUtils.copyProperties(dto, this, getNullPropertyNames(dto));
		this.setAdmin(admin);
	}

	private String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<>();
		for (java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null)
				emptyNames.add(pd.getName());
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.id, this.num, this.content, this.priority, this.status);
	}

}
