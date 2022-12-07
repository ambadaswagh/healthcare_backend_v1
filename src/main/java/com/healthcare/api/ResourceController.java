package com.healthcare.api;

import static com.healthcare.api.common.HealthcareConstants.AUTHENTICATED_ADMIN;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.model.entity.Resource;
import com.healthcare.model.enums.StatusEnum;
import com.healthcare.service.ResourceService;
import com.healthcare.util.PasswordUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@RestController
@RequestMapping(value = "/api/resource")
public class ResourceController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ResourceService resourceService;
	
	@ApiOperation(value = "save resource", notes = "save a new resource")
	@ApiParam(name = "resource", value = "resource to save", required = true)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Resource> create(HttpServletRequest req,@RequestBody Resource resource) {
		return new ResponseEntity<Resource>(resourceService.save(resource), HttpStatus.OK);
	}
	
	@ApiOperation(value = "update resource", notes = "update resource")
    @ApiParam(name = "resource", value = "resource", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @PutMapping
    public ResponseEntity<Resource> update(@RequestBody Resource resource) {
		resource = resourceService.save(resource);
        return  new ResponseEntity<Resource>(resource,HttpStatus.OK);
    }
	
	@ApiOperation(value = "get resource by Id", notes = "get resource by Id")
    @ApiParam(name = "id", value = "resource id", required = true)
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @GetMapping("/{id}")
    public Resource getSeatById(@PathVariable("id") Long id) {
        return resourceService.findById(id);
    }
	
	@ApiOperation(value = "get all resources", notes = "get all resources")
	@GetMapping()
	public List<Resource> readAll() {
		return resourceService.findAll();
	}
}
