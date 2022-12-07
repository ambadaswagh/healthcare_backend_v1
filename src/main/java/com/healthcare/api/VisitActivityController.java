package com.healthcare.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.model.entity.VisitActivity;
import com.healthcare.model.entity.VisitActivityPK;
import com.healthcare.pagination.MultiValueMapConverter;
import com.healthcare.service.VisitActivityService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController(value = "VisitActivityRestAPI")
@RequestMapping(value = "/api/visitActivity")
public class VisitActivityController {
	@Autowired
	private VisitActivityService visitActivityService;

	@ApiOperation(value = "save visit activity", notes = "save a new visit activity")
	@ApiParam(name = "visitActivity", value = "visit activity to update", required = true)
	@PutMapping()
	public ResponseEntity<VisitActivity> create(@RequestBody VisitActivity visitActivity) {
		visitActivity = visitActivityService.save(visitActivity);
		return new ResponseEntity<VisitActivity>(visitActivity, HttpStatus.OK);
	}

	@ApiOperation(value = "get visit activity", notes = "get visit activity info by visit id and activity id")
	@ApiImplicitParams(
	{@ApiImplicitParam(name = "visitId", value = "visit id", required = true, dataType = "Long", paramType = "path"),
	 @ApiImplicitParam(name = "activityId", value = "activity id", required = true, dataType = "Long", paramType = "path")
	})
	@GetMapping("visit/{visitId}/activity/{activityId}")
	public VisitActivity read(@PathVariable Long visitId,@PathVariable Long activityId) {
		VisitActivityPK pk = getPk(visitId, activityId);
		return visitActivityService.findById(pk);
	}
	
	@ApiOperation(value = "get all visit activity by visit id", notes = "get all visit activity by visit id")
	@ApiImplicitParam(name = "visitId", value = "visit id", required = true, dataType = "Long", paramType = "path")
	@GetMapping("visit/{visitId}")
	public ResponseEntity<Page<VisitActivity>> getAllVisitByVisit(@PathVariable Long visitId, @RequestParam MultiValueMap<String, String> attributes) {
		MultiValueMapConverter<VisitActivity> converter = new MultiValueMapConverter<>(attributes, VisitActivity.class);
		return ResponseEntity.ok(visitActivityService.findVisitActivityByVisitId(visitId, converter.getPageable()));
	}
	
	@ApiOperation(value = "get all visit activity by activity id", notes = "get all visit activity by activity id")
	@ApiImplicitParam(name = "activityId", value = "activity id", required = true, dataType = "Long", paramType = "path")
	@GetMapping("activity/{activityId}")
	public ResponseEntity<Page<VisitActivity>> getAllVisit(@PathVariable Long activityId, @RequestParam MultiValueMap<String, String> attributes) {
		MultiValueMapConverter<VisitActivity> converter = new MultiValueMapConverter<>(attributes, VisitActivity.class);
		return ResponseEntity.ok(visitActivityService.findVisitActivityByActivityId(activityId, converter.getPageable()));
	}

	@ApiOperation(value = "delete visit activity", notes = "delete a visit activity")
	@ApiImplicitParams(
	{@ApiImplicitParam(name = "visitId", value = "visit id", required = true, dataType = "Long", paramType = "path"),
	 @ApiImplicitParam(name = "activityId", value = "activity id", required = true, dataType = "Long", paramType = "path")
	})
	@DeleteMapping("visit/{visitId}/activity/{activityId}")
	public void delete(@PathVariable Long visitId,@PathVariable Long activityId) {
		VisitActivityPK pk = getPk(visitId, activityId);
		visitActivityService.deleteById(pk);
	}
	
	private VisitActivityPK getPk(Long visitId, Long activityId) {
		return new VisitActivityPK(visitId,activityId);
	}

}