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

import com.healthcare.model.entity.Menu;
import com.healthcare.service.MenuService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/api/menu")
public class MenuController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MenuService menuService;

	@ApiOperation(value = "save menu", notes = "save menu")
	@ApiParam(name = "menu", value = "menu to save", required = true)
	@PostMapping()
	public ResponseEntity<Menu> create(@RequestBody Menu menu) {
		menu = menuService.save(menu);
		return new ResponseEntity<Menu>(menu, HttpStatus.OK);
	}

	@ApiOperation(value = "get menu by id", notes = "get menu by id")
	@ApiImplicitParam(name = "id", value = "menu id", required = true, dataType = "Long" ,paramType = "path")
	@GetMapping("/{id}")
	public Menu read(@PathVariable Long id) {
		logger.info("id : " + id);
		return menuService.findById(id);
	}

	@ApiOperation(value = "get all menu", notes = "get all menu")
	@GetMapping()
	public List<Menu> readAll() {
		return menuService.findAll();
	}

	@ApiOperation(value = "update menu", notes = "update menu")
	@ApiParam(name = "menu", value = "menu to update", required = true)
	@PutMapping()
	public ResponseEntity<Menu> update(@RequestBody Menu menu) {
		menu = menuService.save(menu);
		return new ResponseEntity<Menu>(menu, HttpStatus.OK);
	}

	@ApiOperation(value = "delete menu", notes = "delete menu")
	@ApiImplicitParam(name = "id", value = "menu id", required = true, dataType = "Long" ,paramType = "path")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		menuService.deleteById(id);
	}

	@ApiOperation(value = "disable menu", notes = "disable menu")
	@ApiImplicitParam(name = "id", value = "menu id", required = true, dataType = "Long" ,paramType = "path")
	@PutMapping("/{id}/disable")
	public void disable(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		menuService.disableById(id);
	}
}
