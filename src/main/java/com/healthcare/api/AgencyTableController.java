package com.healthcare.api;

import com.healthcare.api.auth.AbstractBasedAPI;
import com.healthcare.model.entity.AgencyTable;
import com.healthcare.service.AgencyTableService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController(value = "AgencyTableRestAPI")
@RequestMapping(value = "/api/agency_table")
public class AgencyTableController extends AbstractBasedAPI {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AgencyTableService agencyTableService;

	@ApiOperation(value = "Save Table", notes = "Save Table")
	@ApiParam(name = "agencyTable", value = "agencyTable to save", required = true)
	@PostMapping()
	public ResponseEntity<AgencyTable> create(@RequestBody AgencyTable agencyTable) {
		agencyTable = agencyTableService.save(agencyTable);
		return new ResponseEntity<AgencyTable>(agencyTable, HttpStatus.OK);
	}

  @ApiOperation(value = "get Agency Tables", notes = "get Agency Tables")
  @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
  @GetMapping
  public List<AgencyTable> getAll() {
      return agencyTableService.findAll();
  }

  @ApiOperation(value = "get Agency Tables by Id", notes = "get Agency Tables by Id")
  @ApiParam(name = "id", value = "agency table id", required = true)
  @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "Long", paramType = "header")
  @GetMapping("/{id}")
  public AgencyTable getAgencyTableById(@PathVariable("id") Long id) {
    return agencyTableService.findById(id);
  }

	@ApiOperation(value = "update Agency Table", notes = "update Agency Table")
	@ApiParam(name = "agencyTable", value = "agencyTable", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@PutMapping
	public ResponseEntity<AgencyTable> update(@RequestBody AgencyTable agencyTable) {
		agencyTable = agencyTableService.save(agencyTable);
		return  new ResponseEntity<AgencyTable>(agencyTable,HttpStatus.OK);
	}

	@ApiOperation(value = "delete agency table", notes = "delete agency table")
  @ApiParam(name = "id", value = "agency table id", required = true)
  @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		agencyTableService.deleteById(id);
	}
}
