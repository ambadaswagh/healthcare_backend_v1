package com.healthcare.exception;

import com.healthcare.model.response.Response.ResultCode;
import org.springframework.http.HttpStatus;

/**
 * 
 * @author bharat
 *
 */
public class HealthcareApplicationException extends RuntimeException {

	private ResultCode resultCode;
	private String desctiption;
	private HttpStatus status;

	public HealthcareApplicationException(ResultCode resultCode, String message, HttpStatus status) {
		super(resultCode.name());
		this.resultCode = resultCode;
		this.desctiption = message;
		this.status = status;
	}

	public HealthcareApplicationException(Throwable cause) {
		super(cause);
	}

	public ResultCode getResultCode() {
		return resultCode;
	}

	public void setResultCode(ResultCode resultCode) {
		this.resultCode = resultCode;
	}

	public String getDesctiption() {
		return desctiption;
	}

	public void setDesctiption(String desctiption) {
		this.desctiption = desctiption;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}
