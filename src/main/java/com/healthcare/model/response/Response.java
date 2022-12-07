package com.healthcare.model.response;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 
 * @author orange
 *
 */
public class Response implements Serializable {

	public enum ResultCode {
		SUCCESS, ERROR, USERNAME_EXIST, EMAIL_EXIST, PHONE_EXIST, USER_NOT_FOUND, INVALID_PASSWORD, INVALID_TOKEN,
		INVALID_USERNAME, INVALID_STATUS, VISIT_NOT_FOUND, MODEL_NOT_FOUND, INVALID_DATA, NO_MORE_DATA;
		ResultCode() {
		}
	}

	private ResultCode result;
	private Object data;
	private String message;

	public Response(ResultCode result, Object data, String message) {
		this.result = result;
		this.data = data == null ? new HashMap() : data;
		this.message = message == null ? "" : message;
	}

	public Response(ResultCode result) {
		this.result = result;
		this.data = new HashMap();
		this.message = "";
	}

	public Response(ResultCode result, Object data) {
		this.result = result;
		this.data = data == null ? new HashMap() : data;
		this.message = "";
	}

	public ResultCode getResult() {
		return result;
	}

	public void setResult(ResultCode result) {
		this.result = result;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
