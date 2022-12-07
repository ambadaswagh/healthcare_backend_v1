package com.healthcare.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.api.auth.UtilsResponse;
import com.healthcare.dto.TodolistDTO;
import com.healthcare.model.entity.Todolist;
import com.healthcare.model.response.Response;
import com.healthcare.service.TodolistService;
import com.healthcare.validator.TodolistValidator;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@CrossOrigin
@RestController(value = "TodolistAPI")
@RequestMapping(value = "/api/todolist")
public class TodolistController extends BaseController {

	@Autowired
	private TodolistService todolistService;
	@Autowired
	private UtilsResponse responseBuilder;

	public TodolistController() {

	}

	public TodolistController(TodolistService todolistService, UtilsResponse responseBuilder) {
		this.todolistService = todolistService;
		this.responseBuilder = responseBuilder;
	}

	@ApiOperation(value = "Create new todolist", notes = "API to create new todolist")
	@ApiParam(name = "todo", value = "todolist to save", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@PostMapping()
	public Response create(@ApiParam @RequestBody TodolistDTO todo) {
		DataBinder binder = new DataBinder(todo);
		binder.setValidator(new TodolistValidator());
		binder.validate();
		BindingResult result = binder.getBindingResult();
		if (result.hasErrors()) {
			return responseBuilder.errorResponse("Data validation error");
		}
		Long id = todolistService.create(todo);
		switch (id.intValue()) {
		case -1:
			return responseBuilder.errorResponse("No data to save");
		case -2:
			return responseBuilder.errorResponse("Admin doesn't existed");
		default:
		}
		return responseBuilder.successResponse(id, "OK");
	}

	@ApiOperation(value = "Delete todolist", notes = "Use for admin to delete a todolist. Here any admin can delete any todolist at the moment without restrict")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "todolist id", required = true, dataType = "Long", paramType = "path"),
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header") })
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		todolistService.deleteById(id);
	}

	@ApiOperation(value = "update todolist", notes = "API to update a existed todolist")
	@ApiParam(name = "todolist", value = "todolist to update", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@PutMapping()
	public Response update(@ApiParam @RequestBody TodolistDTO todo) {
		DataBinder binder = new DataBinder(todo);
		binder.setValidator(new TodolistValidator());
		binder.validate();
		BindingResult result = binder.getBindingResult();
		if (result.hasErrors()) {
			return responseBuilder.errorResponse("Data validation error");
		}
		todolistService.update(todo);
		return responseBuilder.successResponse(todo.getId(), "OK");
	}

	@ApiOperation(value = "close a todolist", notes = "Close a todolist that have been completed")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "id", value = "todolist id", required = true, dataType = "Long", paramType = "path") })
	@GetMapping("/close/{id}")
	public Response closeTodolist(@PathVariable("id") Long id) {
		Long result = todolistService.close(id);
		switch (result.intValue()) {
		case -1:
			return responseBuilder.buildResponse(Response.ResultCode.ERROR, "-1", "Todolist doesn't existed");
		case -2:
			return responseBuilder.buildResponse(Response.ResultCode.ERROR, "-2", "Todolist already closed");
		default:
			return responseBuilder.successResponse(result, "OK");
		}
	}

	@ApiOperation(value = "List todolist by admin", notes = "List all todolist by admin")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "adminid", value = "admin id", required = true, dataType = "Long", paramType = "path") })
	@GetMapping("/admin/{adminid}")
	public Response listTodolistByUser(@PathVariable("adminid") Long adminId) {
		List<Todolist> todos = todolistService.getByAdmin(adminId);
		return responseBuilder.successResponse(todos, "OK");
	}

	@ApiOperation(value = "List todolist by user", notes = "List all todolist by admin")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "adminid", value = "admin id", required = true, dataType = "Long", paramType = "path"),
			@ApiImplicitParam(name = "status", value = "status", required = true, dataType = "Long", paramType = "path") })
	@GetMapping("/admin/{adminid}/{status}")
	public Response listTodolistByUserAndStatus(@PathVariable("adminid") Long adminid,
			@PathVariable("status") Integer status) {
		List<Todolist> todos = todolistService.getByAdminAndStatus(adminid, status);
		return responseBuilder.successResponse(todos, "OK");
	}

	@ApiOperation(value = "Get all todolist", notes = "List all todolist")
	@GetMapping()
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	public Response getAllTodolist() {
		List<Todolist> todos = todolistService.getAll();
		return responseBuilder.successResponse(todos, "OK");
	}

}
