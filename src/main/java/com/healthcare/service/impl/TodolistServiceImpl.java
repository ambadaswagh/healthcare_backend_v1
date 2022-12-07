package com.healthcare.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.dto.TodolistDTO;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.Todolist;
import com.healthcare.model.enums.EntityStatusEnum;
import com.healthcare.repository.TodolistRepository;
import com.healthcare.service.AdminService;
import com.healthcare.service.TodolistService;

/**
 * Todolist service
 */
@Service
@Transactional
public class TodolistServiceImpl implements TodolistService {

	@Autowired
	private AdminService adminService;
	@Autowired
	private TodolistRepository todolistRepository;

	@Override
	public Todolist save(Todolist todo) {
		return todolistRepository.save(todo);
	}

	@Override
	public Todolist findById(Long id) {
		if (id == null || id < 0)
			return null;
		return todolistRepository.findOne(id);
	}

	@Override
	public Long deleteById(Long id) {
		todolistRepository.delete(id);
		return id;
	}

	@Override
	public Long disableById(Long id) {
		Todolist todo = findById(id);
		if (todo == null)
			return null;
		todo.setStatus(EntityStatusEnum.DISABLE.getValue());
		save(todo);
		return todo.getId();
	}

	@Override
	public List<Todolist> getByAdminAndStatus(Long adminId, Integer status) {
		if (adminId == null || adminId < 0)
			return new ArrayList<Todolist>();
		return todolistRepository.getTodolistByAdminAndStatus(adminId, status);

	}

	@Override
	public List<Todolist> getAll() {
		return todolistRepository.findAll();
	}

	@Override
	public List<Todolist> getByAdmin(Long userId) {
		if (userId == null || userId < 0)
			return new ArrayList<Todolist>();
		return todolistRepository.getTodolistByAdmin(userId);
	}

	@Override
	public Long create(TodolistDTO todo) {
		if(todo == null) return -1L;
		Admin admin = adminService.findById(todo.getAdminId());
		if(admin == null){
			return -2L;
		}
		Todolist todolist = new Todolist();
		todolist.fromDto(todo, admin);
		return save(todolist).getId();
	}

	@Override
	public Long close(Long id) {
		Todolist todo = findById(id);
		if(todo == null) return -1L;
		if(todo.getStatus() == 1) return -2L;
		todo.setStatus(1);
		save(todo);
		return todo.getId();
	}

	@Override
	public Long update(TodolistDTO todo) {
		if(todo == null) return -1L;
		Admin admin = adminService.findById(todo.getAdminId());
		if(admin == null){
			return -2L;
		}
		Todolist todolist = new Todolist();
		todolist.fromDto(todo, admin);
		save(todolist);
		return todolist.getId();
	}

}
