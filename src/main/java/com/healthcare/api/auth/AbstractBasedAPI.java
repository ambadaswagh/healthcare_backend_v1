package com.healthcare.api.auth;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.healthcare.exception.ApplicationException;
import com.healthcare.exception.ResourceNotFoundException;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.User;
import com.healthcare.model.response.Response;
import com.healthcare.service.AdminService;
import com.healthcare.service.UserService;

/**
 * Created by orange on 2/23/17.
 *
 * This class is meant to be extended by all REST APIs. It contains exception
 * mapping and other common REST API functionality
 */
// @ControllerAdvice?
public abstract class AbstractBasedAPI implements ApplicationEventPublisherAware {

	@Autowired
	public UtilsResponse responseBulder;

	public AbstractBasedAPI() {
	}

	public AbstractBasedAPI(UtilsResponse responseBulder, AdminService adminService, AuthTokenUtil authTokenUtil) {
		this.responseBulder = responseBulder;
		this.adminService = adminService;
		this.authTokenUtil = authTokenUtil;
	}

	public AbstractBasedAPI(UtilsResponse responseBulder, UserService userService, AuthTokenUtil authTokenUtil) {
		this.responseBulder = responseBulder;
		this.userService = userService;
		this.authTokenUtil = authTokenUtil;
	}

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	protected ApplicationEventPublisher eventPublisher;

	protected static final String DEFAULT_PAGE_SIZE = "100";
	protected static final String DEFAULT_PAGE_NUM = "0";

	// Mapper object is used to convert object and etc...
	public final static ObjectMapper mapper = new ObjectMapper();
	// Set format
	static {
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
		// Set setting remove NULL property
		// .setSerializationInclusion(JsonInclude.Include.NON_NULL)
		// .setSerializationInclusion(JsonInclude.Include.ALWAYS);
		// .setDateFormat(new SimpleDateFormat(Constant.API_FORMAT_DATE));
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.eventPublisher = applicationEventPublisher;
	}

	// todo: replace with exception mapping
	public static <T> T checkResourceFound(final T resource) {
		if (resource == null) {
			throw new ResourceNotFoundException("resource not found");
		}
		return resource;
	}

	//
	// Write object as string using mapper
	//
	protected String writeObjectToJson(Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException ex) {
			// Throw our exception
			throw new ApplicationException(ex.getCause());
		}
	}

	@Autowired
	AdminService adminService;
	@Autowired
	UserService userService;
	@Autowired
	AuthTokenUtil authTokenUtil;

	// Get current Admin authen user
	protected Admin getCurrentAuthenAdminUser(HttpServletRequest request) {
		String token = request.getHeader("token");
		String username = authTokenUtil.getUsernameFromToken(token);
		Admin adminUser = adminService.getUser(username);
		if (adminUser != null) {
			UserDetails userDetails = AuthUserFactory.create(adminUser);
			if (authTokenUtil.validateToken(token, userDetails)) {
				return adminUser;
			} else {
				throw new ApplicationException(Response.ResultCode.INVALID_TOKEN, "Invalid request token");
			}
		} else
			throw new ApplicationException(Response.ResultCode.INVALID_TOKEN, "Invalid request token");
	}

	// Get current User authen user
	protected User getCurrentAuthenUser(HttpServletRequest request) {
		String token = request.getHeader("token");
		String username = authTokenUtil.getUsernameFromToken(token);
		User user = userService.getUser(username);
		if (user != null) {
			UserDetails userDetails = AuthUserFactory.create(user);
			if (authTokenUtil.validateToken(token, userDetails)) {
				return user;
			} else {
				throw new ApplicationException(Response.ResultCode.INVALID_TOKEN, "Invalid request token");
			}
		} else
			throw new ApplicationException(Response.ResultCode.INVALID_TOKEN, "Invalid request token");
	}
}