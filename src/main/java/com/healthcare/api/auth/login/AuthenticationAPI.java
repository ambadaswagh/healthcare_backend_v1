package com.healthcare.api.auth.login;

import javax.servlet.http.HttpServletRequest;

import com.healthcare.model.entity.AdminAgencyCompanyOrganization;
import com.healthcare.model.entity.AdminSetting;
import com.healthcare.service.AdminAgencyCompanyOrganizationService;
import com.healthcare.service.AdminSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.api.auth.AbstractBasedAPI;
import com.healthcare.api.auth.AuthTokenUtil;
import com.healthcare.api.auth.AuthUserFactory;
import com.healthcare.api.auth.UtilsResponse;
import com.healthcare.api.auth.model.AuthRequest;
import com.healthcare.api.auth.model.AuthResponse;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.response.Response;
import com.healthcare.model.response.Response.ResultCode;
import com.healthcare.service.AdminService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author orange
 *
 */
@RestController(value = "AdminLoginRestAPI")
@RequestMapping(value = "/api/auth")
public class AuthenticationAPI extends AbstractBasedAPI {

	// Token key required in header for every request
	private String tokenHeader = "token";

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private AuthTokenUtil tokenUtil;

	@Autowired
	private AdminService adminService;

	@Autowired
	private AdminSettingService adminSettingService;

	@Autowired
	private AdminAgencyCompanyOrganizationService adminAgencyCompanyOrganizationService;

	@Autowired
	private UserAuthenticationAPI userAuthenticationAPI;
	
	
	@Autowired
	public AuthenticationAPI(AuthenticationManager authenticationManager,
			AuthTokenUtil tokenUtil,
			AdminService adminService,
			UtilsResponse responseBuilder) {
		super(responseBuilder,adminService,tokenUtil);
		this.authenticationManager = authenticationManager;
		this.tokenUtil = tokenUtil;
		this.adminService = adminService;
	}


	/**
	 * 
	 * @param authenticationRequest
	 * @return
	 * @throws AuthenticationException
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = { "application/json" })
	@ApiOperation(value = "login admin", notes = "login admin")
	@ApiParam(name = "login", value = "login admin", required = true)
	public Response login(@RequestBody AuthRequest authenticationRequest) throws AuthenticationException {

		// Check user name & password
		Response checkUserResponse = adminService.login(authenticationRequest);
		if (checkUserResponse != null && checkUserResponse.getData() != null) {
			if (checkUserResponse.getResult() == ResultCode.SUCCESS) {
				// Get authen Admin
				Admin adminUser = (Admin) checkUserResponse.getData();
				// Perform the security
				final Authentication authentication = authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
								authenticationRequest.getPassword()));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				// Create user detail
				UserDetails userDetails = AuthUserFactory.create(adminUser);
				// Generate token for authen admin user
				final String token = tokenUtil.generateToken(userDetails);
				// Update token for current Admin login
				adminUser.setRememberToken(token);
				adminService.save(adminUser);
				// Return result
				setCompayAgencyInfo(adminUser);
				AuthResponse authResponse = new AuthResponse(token, adminUser);
				return responseBulder.buildResponse(Response.ResultCode.SUCCESS, authResponse, "OK");

			} else {
                return checkUserResponse;
			}
		}
		return responseBulder.buildResponse(Response.ResultCode.ERROR, "", "Wrong user name or password");
	}

	private void setCompayAgencyInfo(Admin adminUser) {
		AdminAgencyCompanyOrganization adminAgencyCompanyOrganization = adminAgencyCompanyOrganizationService.getCompanyAndAgency(adminUser.getId());
		if(adminAgencyCompanyOrganization != null) {
            if(adminAgencyCompanyOrganization.getCompany() != null) {
                adminUser.setCompanyId(adminAgencyCompanyOrganization.getCompany().getId());
            }
            if(adminAgencyCompanyOrganization.getAgency() != null) {
                adminUser.setAgencyId(adminAgencyCompanyOrganization.getAgency().getId());
                adminUser.setAgency(adminAgencyCompanyOrganization.getAgency());
            }
        }
		adminUser.setAdminSetting(adminSettingService.getAdminSetting(adminUser.getId()));
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST, produces = { "application/json" })
	@ApiOperation(value = "logout admin", notes = "logout admin")
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType= "header")
	public Response logout(HttpServletRequest request) {
		Response response = null;
		String authToken = request.getHeader(this.tokenHeader);
		boolean result = tokenUtil.removeToken(authToken);
		SecurityContextHolder.getContext().setAuthentication(null);
		if (result) {
			response = responseBulder.successResponse("", "Logout success! Token has been removed");
		} else {
			response = responseBulder.errorResponse("Token is not valid");
		}

		return response;
	}

	// API current user
	@RequestMapping(value = "/profile", method = RequestMethod.GET, produces = {
			"application/json;charset=utf-8" })
	@ApiOperation(value = "admin profile", notes = "admin profile")
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType= "header")
	@ResponseStatus(HttpStatus.OK)
	public Response getUser(HttpServletRequest request) {
		// This function will also auto hendle expire token & reponse error
		Admin currentAuthUser = getCurrentAuthenAdminUser(request);
		return responseBulder.successResponse(currentAuthUser, "Logout success! Token has been removed");
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/token/refresh", method = RequestMethod.GET)
	@ApiOperation(value = "refresh token", notes = "refresh token")
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType= "header")
	public Response refreshAndGetAuthenticationToken(HttpServletRequest request) {
		String token = request.getHeader(tokenHeader);
		String username = tokenUtil.getUsernameFromToken(token);
		Admin adminUser = adminService.getUser(username);
		if (adminUser != null) {
			UserDetails userDetails = AuthUserFactory.create(adminUser);
			if (tokenUtil.validateToken(token, userDetails)) {
				String refreshedToken = tokenUtil.refreshToken(token);
				adminUser.setRememberToken(refreshedToken);
				adminService.save(adminUser);
				
				// Return result
				setCompayAgencyInfo(adminUser);
				AuthResponse authResponse = new AuthResponse(refreshedToken, adminUser);
				return responseBulder.buildResponse(Response.ResultCode.SUCCESS, authResponse, "OK");

			} else {
				return responseBulder.buildResponse(Response.ResultCode.ERROR, "", "Invalid token");

			}
		}
		return responseBulder.buildResponse(Response.ResultCode.ERROR, "", "Invalid token");

	}
	
	
}
