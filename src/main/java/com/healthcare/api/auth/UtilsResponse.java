package com.healthcare.api.auth;

import org.springframework.stereotype.Component;

import com.healthcare.model.response.Response;

/**
 * 
 * @author orange
 *
 */
@Component
public class UtilsResponse {

	public UtilsResponse() {
	}

	Response response;

	/**
	 * 
	 * @param data
	 * @param desciption
	 * @return
	 */
	public Response successResponse(Object data, String desciption) {
		response = new Response(Response.ResultCode.SUCCESS, data, desciption);
		return response;
	}

	/**
	 * 
	 * @param code
	 * @param data
	 * @param desciption
	 * @return
	 */
	public Response buildResponse(Response.ResultCode code, Object data, String desciption) {
		response = new Response(code, data, desciption);
		return response;
	}

	/**
	 * 
	 * @param message
	 * @return
	 */
	public Response errorResponse(String message) {
		response = new Response(Response.ResultCode.ERROR, null, message);
		return response;
	}

}