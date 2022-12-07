package com.healthcare.exception;

import com.healthcare.model.response.Response.ResultCode;

/**
 * 
 * @author orange
 *
 */
public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = -6478276298509851491L;
	private ResultCode resultCode;
	private String desctiption;

	public ApplicationException(ResultCode resultCode, String message) {
		super(resultCode.name());
		this.resultCode = resultCode;
		this.desctiption = message;
	}

	public ApplicationException(Throwable cause) {
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

}
