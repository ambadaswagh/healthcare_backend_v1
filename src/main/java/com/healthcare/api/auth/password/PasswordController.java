package com.healthcare.api.auth.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.api.BaseController;
import com.healthcare.service.PasswordService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Activity controller
 */
@RestController
@RequestMapping("/api/password")
public class PasswordController extends BaseController {

	@Autowired
	private PasswordService passwordService;

	@ApiOperation(value = "Forgot password", notes = "Forgot password")
	@ApiParam(name = "activity", value = "activity to create", required = true)
	@PostMapping
	public ResponseEntity forgotPassword(@RequestParam("email") String email) {
		passwordService.forgotPassword(email);
		return new ResponseEntity(HttpStatus.OK);
	}
}
