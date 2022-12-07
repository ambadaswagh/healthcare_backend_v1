package com.healthcare.api;

import com.healthcare.api.auth.AbstractBasedAPI;
import com.healthcare.model.entity.Reservation;
import com.healthcare.service.ReservationService;
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
@RestController(value = "StandingOrderRestAPI")
@RequestMapping(value = "/api/standing_order")
public class ReservationController extends AbstractBasedAPI {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ReservationService reservationService;

	@ApiOperation(value = "Save Standing Order", notes = "Save Standing Order")
	@ApiParam(name = "standingOrder", value = "standingOrder to save", required = true)
	@PostMapping()
	public ResponseEntity<Reservation> create(@RequestBody Reservation reservation) {
		reservation = reservationService.save(reservation);
		return new ResponseEntity<Reservation>(reservation, HttpStatus.OK);
	}

  @ApiOperation(value = "get Standing Orders", notes = "get Standing Orders")
  @ApiParam(name = "seat", value = "seat", required = true)
  @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
  @GetMapping
  public List<Reservation> getAll() {
    return reservationService.findAll();
  }

  @ApiOperation(value = "get StandingOrder by Id", notes = "get StandingOrder by Id")
  @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
  @ApiParam(name = "id", value = "Standing Order id", required = true)
  @GetMapping("/{id}")
  public Reservation getStandingOrderById(@PathVariable("id") Long id) {
    return reservationService.findById(id);
  }

	@ApiOperation(value = "update Standing Order", notes = "update Standing Order")
	@ApiParam(name = "standingOrder", value = "standingOrder", required = true)
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@PutMapping
	public ResponseEntity<Reservation> update(@RequestBody Reservation reservation) {
		reservation = reservationService.save(reservation);
		return new ResponseEntity<Reservation>(reservation,HttpStatus.OK);
	}

	@ApiOperation(value = "delete standing Order", notes = "delete standing Order")
  @ApiParam(name = "id", value = "Standing Order id", required = true)
  @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		logger.info("id : " + id);
		reservationService.deleteById(id);
	}
	
	
	 @ApiOperation(value = "Get Standing Order for user", notes = "Get Standing Order for user")
	  @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	  @ApiParam(name = "id", value = "User id", required = true)
	  @GetMapping("/user/{id}")
	  public List<Reservation> getStandingOrderByUserId(@PathVariable("id") Long id) {
	    return reservationService.findBySeniorId(id);
	  }
}
