package com.healthcare.service;

import java.util.List;

import com.healthcare.dto.TodolistDTO;
import com.healthcare.model.entity.Todolist;

/**
 * Activity service methods
 */
public interface TodolistService extends IService<Todolist> {
	public List<Todolist> getAll();
	public List<Todolist> getByAdminAndStatus(Long userId, Integer status);
	public List<Todolist> getByAdmin(Long userId);
	public Long create(TodolistDTO todo);
	public Long update(TodolistDTO todo);
	public Long close(Long id);

}
