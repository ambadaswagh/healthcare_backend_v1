package com.healthcare.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.api.auth.AbstractBasedAPI;
import com.healthcare.model.entity.TrainingEmployee;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.TrainingEmployeeService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Created by jean on 03/07/17.
 */
@CrossOrigin
@RestController(value = "TrainingHasEmployeeRestAPI")
@RequestMapping(value = "/api/training_has_employee")
public class TrainingEmployeeController extends AbstractBasedAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TrainingEmployeeService trainingEmployeeService;

    @ApiOperation(value = "save training employee", notes = "save training employee")
    @ApiParam(name = "training employee", value = "training employee to save", required = true)
    @PostMapping()
    public ResponseEntity<TrainingEmployee> create(@RequestBody TrainingEmployee trainingEmployee) {
        trainingEmployee = trainingEmployeeService.save(trainingEmployee);
        return new ResponseEntity<TrainingEmployee>(trainingEmployee, HttpStatus.OK);
    }

    @ApiOperation(value = "get training employee by id", notes = "get training employee by id")
    @ApiImplicitParam(name = "id", value = "training employee id", required = true, dataType = "Long" ,paramType = "path")
    @GetMapping("/{training_has_employee}")
    public TrainingEmployee read(@PathVariable Long id) {
        logger.info("id : " + id);
        return trainingEmployeeService.findById(id);
    }

    @ApiOperation(value = "get all training employee", notes = "get all training employee")
    @GetMapping()
	public ResponseEntity<Page<TrainingEmployee>> readAll(@RequestParam MultiValueMap<String, String> attributes) {
		MultiValueMapConverter<TrainingEmployee> converter = new MultiValueMapConverter<>(attributes, TrainingEmployee.class);
		return ResponseEntity.ok(trainingEmployeeService.findAll(converter.getData(), converter.getPageable()));
	}
	
    @ApiOperation(value = "update training employee", notes = "update training employee")
    @ApiParam(name = "training employee", value = "training employee to update", required = true)
    @PutMapping()
    public ResponseEntity<TrainingEmployee> update(@RequestBody TrainingEmployee trainingEmployee) {
        trainingEmployee = trainingEmployeeService.save(trainingEmployee);
        return new ResponseEntity<TrainingEmployee>(trainingEmployee, HttpStatus.OK);
    }

    @ApiOperation(value = "delete training employee", notes = "delete training employee")
    @ApiImplicitParam(name = "id", value = "training employee id", required = true, dataType = "Long" ,paramType = "path")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        logger.info("id : " + id);
        trainingEmployeeService.deleteById(id);
    }


}

