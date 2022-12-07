package com.healthcare.conf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.healthcare.exception.HealthcareApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.healthcare.api.auth.UtilsResponse;
import com.healthcare.exception.ApplicationException;
import com.healthcare.model.response.Response;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends
  ResponseEntityExceptionHandler {
	@Autowired
	public UtilsResponse responseBulder;
	
	@ExceptionHandler(value = { DataIntegrityViolationException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException e, WebRequest request) {
	 String bodyOfResponse = e.getCause().getCause().getMessage();
	 bodyOfResponse = bodyOfResponse.replaceAll("Column", "Value of ");
	 bodyOfResponse = bodyOfResponse.replaceAll("_", " ");
	 
	 return new ResponseEntity<Object>(
				responseBulder.buildResponse(Response.ResultCode.ERROR, "",bodyOfResponse),
				HttpStatus.CONFLICT);
    }

	@ExceptionHandler(value = { ApplicationException.class})
	protected ResponseEntity<Object> handleConflict1(RuntimeException e, WebRequest request) {
		ApplicationException e1  = (ApplicationException) e;
		return new ResponseEntity<Object>(
				responseBulder.buildResponse(Response.ResultCode.ERROR, "",e1.getMessage()),
				HttpStatus.CONFLICT);
	}

	@ExceptionHandler(value = { HealthcareApplicationException.class})
	protected ResponseEntity<Object> handleHealthcareAppException(RuntimeException e, WebRequest request) {
		HealthcareApplicationException e1  = (HealthcareApplicationException) e;
		return new ResponseEntity<Object>(responseBulder.buildResponse(e1.getResultCode(), "",e1.getMessage()), e1.getStatus());
	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Object>  defaultErrorHandler(HttpServletRequest request, HttpServletResponse response,
			Exception exception) throws Exception {
	 return new ResponseEntity<Object>(
				responseBulder.buildResponse(Response.ResultCode.ERROR, "",exception.getMessage()),
				HttpStatus.CONFLICT);
    }
}