package com.healthcare.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.model.entity.WorkItem;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.WorkItemService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Created by Mostafa Hamed on 30/06/17.
 * @author mhamed
 * @version 1.0
 */

@RestController(value = "WorkItemRestAPI")
@RequestMapping(value = "/api/work_item")
public class WorkItemController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private WorkItemService workItemService;
	
	@ApiOperation(value = "post work item", notes = "post work item")
	@ApiParam(name = "workItem", value = "post work item", required = true)
	@PostMapping()
	public ResponseEntity<WorkItem> create(@RequestBody WorkItem workItem) {
		workItem = workItemService.save(workItem);
		return new ResponseEntity<>(workItem, HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "get work item", notes = "get work item")
	@ApiImplicitParam(name = "id", value = "get work item", required = true, dataType = "Long" ,paramType = "path")
	@GetMapping("/{id}")
	public WorkItem read(@PathVariable Long id) {
		logger.info("id : " + id);
		WorkItem workItem = workItemService.findById(id);
		return workItem;
	}
	
	
	@ApiOperation(value = "get all work items", notes = "get all work items")
	@GetMapping()
	public ResponseEntity<Page<WorkItem>> readAll(@RequestParam MultiValueMap<String, String> attributes) {
		MultiValueMapConverter<WorkItem> converter = new MultiValueMapConverter<>(attributes, WorkItem.class);
		return ResponseEntity.ok(workItemService.findAll(converter.getData(), converter.getPageable()));
	}

	
	
	@ApiOperation(value = "update work item", notes = "update work item")
	@ApiParam(name = "workItem", value = "update work item", required = true)
	@PutMapping()
	public ResponseEntity<WorkItem> update(@RequestBody WorkItem workItem) {
		workItem = workItemService.save(workItem);
		return new ResponseEntity<>(workItem, HttpStatus.OK);
	}

	
	@ApiOperation(value = "delete work item", notes = "delete work item")
	@ApiImplicitParam(name = "id", value = "delete work item", required = true, dataType = "Long" ,paramType = "path")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		workItemService.deleteById(id);
	}
	
}
