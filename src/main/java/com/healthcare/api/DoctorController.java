package com.healthcare.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.api.auth.UtilsResponse;
import com.healthcare.dto.DoctorAppointmentDTO;
import com.healthcare.model.response.Response;
import com.healthcare.service.DoctorAppointmentService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Doctor controller
 */
@RestController
@RequestMapping("/api/doctor")
public class DoctorController extends BaseController {

	@Autowired
	private UtilsResponse responseBuilder;
	@Autowired
	private DoctorAppointmentService doctorAppointmentService;

	@ApiOperation(value = "List all doctor appointment", notes = "List all doctor appointment")
	@PostMapping("/appointment/search")
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	public Response getAllDoctorAppointment(@ApiParam @RequestBody DoctorAppointmentDTO dto) {
		return responseBuilder.successResponse(doctorAppointmentService.search(dto), "OK");
	}

	@ApiOperation(value = "List of doctor appointment in next X day", notes = "List of doctor appointment in next X day")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "day", value = "how many day next to current", required = true, dataType = "int", paramType = "path") })
	@GetMapping("/appointment/next/{day}")
	public Response read(@PathVariable int day) {
		return responseBuilder.successResponse(doctorAppointmentService.getAppointmentNextDays(day), "OK");

	}

}