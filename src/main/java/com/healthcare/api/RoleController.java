package com.healthcare.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.model.entity.Role;
import com.healthcare.service.RoleService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/api/role")
public class RoleController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RoleService roleService;

	@ApiOperation(value = "save role", notes = "save role")
	@ApiParam(name = "role", value = "role to save", required = true)
	@PostMapping()
	public ResponseEntity<Role> create(@RequestBody Role role) {
		role = roleService.save(role);
		return new ResponseEntity<Role>(role, HttpStatus.OK);
	}

	@ApiOperation(value = "get role by id", notes = "get role by id")
	@ApiImplicitParam(name = "id", value = "role id", required = true, dataType = "Long" ,paramType = "path")
	@GetMapping("/{id}")
	public Role read(@PathVariable Long id) {
		logger.info("id : " + id);
		return roleService.findById(id);
	}

	@ApiOperation(value = "get all role", notes = "get all role")
	@GetMapping()
	public List<Role> readAll() {
		return roleService.findAll();
	}

	@ApiOperation(value = "update role", notes = "update role")
	@ApiParam(name = "role", value = "role to update", required = true)
	@PutMapping()
	public ResponseEntity<Role> update(@RequestBody Role role) {
		role = roleService.save(role);
		return new ResponseEntity<Role>(role, HttpStatus.OK);
	}

	@ApiOperation(value = "delete role", notes = "delete role")
	@ApiImplicitParam(name = "id", value = "role id", required = true, dataType = "Long" ,paramType = "path")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		roleService.deleteById(id);
	}

	@ApiOperation(value = "disable role", notes = "disable role")
	@ApiImplicitParam(name = "id", value = "role id", required = true, dataType = "Long" ,paramType = "path")
	@PutMapping("/{id}/disable")
	public void disable(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		roleService.disableById(id);
	}

	@ApiOperation(value = "get role by level", notes = "get role by level")
	@ApiImplicitParam(name = "level", value = "role level", required = true, dataType = "Long" ,paramType = "path")
	@GetMapping("/level/{level}")
	public Role findByLevel(@PathVariable Long level) {
		logger.info("level : " + level);
		return roleService.findByLevel(level);
	}

	@ApiOperation(value = "get role by status", notes = "get role by status")
	@ApiImplicitParam(name = "status", value = "role status", required = true, dataType = "Long")
	@GetMapping("/status/{status}")
	public List<Role> findByStatus(@PathVariable Long status) {
		logger.info("status : " + status);
		return roleService.findByStatus(status);
	}
}
