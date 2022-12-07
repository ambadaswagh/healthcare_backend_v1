package com.healthcare.dto;

import java.io.Serializable;

import lombok.Data;

public @Data class TodolistDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String num;
	private String content;
	private String priority;
	private String color;
	private int status;
	private Long adminId;
}
