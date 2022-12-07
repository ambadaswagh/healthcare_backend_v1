package com.healthcare.api.auth.login;

import javax.servlet.http.HttpServletRequest;

import com.healthcare.api.auth.model.LoginPinRequest;
import com.healthcare.api.common.HealthcareUtil;
import com.healthcare.exception.UserException;
import com.healthcare.model.entity.Employee;
import com.healthcare.service.EmployeeService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.healthcare.api.auth.AbstractBasedAPI;
import com.healthcare.api.auth.AuthTokenUtil;
import com.healthcare.api.auth.AuthUserFactory;
import com.healthcare.api.auth.UtilsResponse;
import com.healthcare.api.auth.model.AuthRequest;
import com.healthcare.api.auth.model.AuthUserResponse;
import com.healthcare.model.entity.User;
import com.healthcare.model.response.Response;
import com.healthcare.model.response.Response.ResultCode;
import com.healthcare.service.UserService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

/**
 * 
 * @author orange
 *
 */
@RestController(value = "UserLoginRestAPI")
@RequestMapping(value = "/api/user/auth")
public class UserAuthenticationAPI extends AbstractBasedAPI {

	// Token key required in header for every request
	private String tokenHeader = "token";

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private AuthTokenUtil tokenUtil;

	@Autowired
	private UserService userService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	public UserAuthenticationAPI(AuthenticationManager authenticationManager, AuthTokenUtil tokenUtil,
			UserService userService, UtilsResponse responseBuilder) {
		super(responseBuilder, userService, tokenUtil);
		this.authenticationManager = authenticationManager;
		this.tokenUtil = tokenUtil;
		this.userService = userService;
	}

	/**
	 *
	 * @param req
	 * @return
	 * @throws AuthenticationException
	 */
	@RequestMapping(value = "/pin_login", method = RequestMethod.POST, produces = { "application/json" })
	@ApiOperation(value = "login user using pinOrBarcode", notes = "login user using pinOrBarcode")
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @ApiParam(name = "login", value = "login user", required = true)
	public ResponseEntity<JSONObject> loginByPIN(HttpServletRequest req, @RequestBody LoginPinRequest loginPinRequest)
			throws AuthenticationException {

		//long agencyId = HealthcareUtil.getAgencyIdOfLoggedInAdmin(req);

		JSONObject entity = new JSONObject();
		try {


			User user = userService.getUserByPIN(loginPinRequest.getPinOrBarcode(),loginPinRequest.getAgencyId());
			String loginType = null;
			Employee employee = null;
			if(user == null){
				employee = employeeService.getEmployeeByPin(loginPinRequest.getPinOrBarcode());
				if(employee != null) {
					loginType = "employee";
				}
			} else{
				loginType = "senior";
			}
			if(loginType != null) {
				JSONObject data = new JSONObject();
				data.put("senior", user != null ? user : new JSONObject());
				data.put("employee", employee != null ? employee : new JSONObject());
				data.put("loginType", loginType);
				entity.put("result", "SUCCESS");
				entity.put("data", data);
				entity.put("message", "");
			} else {
				error(entity);
			}
			return new ResponseEntity<JSONObject>(entity, HttpStatus.OK);
		} catch (Exception e) {
			return error(entity);
		}
	}

	private ResponseEntity<JSONObject> error(JSONObject entity) {
		entity.put("result", "ERROR");
		entity.put("data", new JSONObject());
		entity.put("message", "Invalid PIN!");
		return new ResponseEntity<JSONObject>(entity, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/QRCode_login", method = RequestMethod.POST, produces = { "application/json" })
    @ApiOperation(value = "login user using qrCode", notes = "login user using qrCode")
    @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
    @ApiParam(name = "login", value = "login user", required = true)
    public Response loginByQRCode(HttpServletRequest req, @RequestBody LoginPinRequest loginPinRequest)
            throws AuthenticationException {

		if(HealthcareUtil.isNull(loginPinRequest.getAgencyId())
                || HealthcareUtil.isEmpty(loginPinRequest.getPinOrBarcode())){
			throw new UserException("Either agency or QrCode is missing. ");
		}

        List<User> users = userService.getUsersByAgency(loginPinRequest.getAgencyId());

        User currentUser = null;
        for(User user : users){
            if(loginPinRequest.getPinOrBarcode().equals(user.getQrCode())){
                currentUser = user;
                break;
            }
        }

        if( currentUser == null){
            throw new UserException("No matching user found!");
        }

        return responseBulder.buildResponse(Response.ResultCode.SUCCESS,currentUser, "OK");
    }

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = { "application/json" })
	@ApiOperation(value = "login user", notes = "login user")
	@ApiParam(name = "login", value = "login user", required = true)
	public Response login(@RequestBody AuthRequest authenticationRequest) throws AuthenticationException {

		// Check user name & password
		Response checkUserResponse = userService.login(authenticationRequest);
		if (checkUserResponse != null && checkUserResponse.getData() != null) {
			if (checkUserResponse.getResult() == ResultCode.SUCCESS) {
				// Get authen Admin
				User user = (User) checkUserResponse.getData();
				// Perform the security
				final Authentication authentication = authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
								authenticationRequest.getPassword()));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				// Create user detail
				UserDetails userDetails = AuthUserFactory.create(user);
				// Generate token for authen user
				final String token = tokenUtil.generateToken(userDetails);
				// Update token for current user login
				user.setRememberToken(token);
				userService.save(user);
				// Return result
				AuthUserResponse authResponse = new AuthUserResponse(token, user);
				return responseBulder.buildResponse(Response.ResultCode.SUCCESS, authResponse, "OK");

			} else {
				return checkUserResponse;
			}
		}
		return responseBulder.buildResponse(Response.ResultCode.ERROR, "", "Wrong user name or password");
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST, produces = { "application/json" })
	@ApiOperation(value = "logout user", notes = "logout user")
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
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
	@RequestMapping(value = "/profile", method = RequestMethod.GET, produces = { "application/json;charset=utf-8" })
	@ApiOperation(value = "user profile", notes = "user profile")
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	@ResponseStatus(HttpStatus.OK)
	public Response getUser(HttpServletRequest request) {
		// This function will also auto hendle expire token & reponse error
		User currentAuthUser = getCurrentAuthenUser(request);
		return responseBulder.successResponse(currentAuthUser, "Logout success! Token has been removed");
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/token/refresh", method = RequestMethod.GET)
	@ApiOperation(value = "refresh token", notes = "refresh token")
	@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header")
	public Response refreshAndGetAuthenticationToken(HttpServletRequest request) {
		String token = request.getHeader(tokenHeader);
		String username = tokenUtil.getUsernameFromToken(token);
		User user = userService.getUser(username);
		if (user != null) {
			UserDetails userDetails = AuthUserFactory.create(user);
			if (tokenUtil.validateToken(token, userDetails)) {
				String refreshedToken = tokenUtil.refreshToken(token);
				user.setRememberToken(refreshedToken);
				userService.save(user);

				// Return result
				AuthUserResponse authResponse = new AuthUserResponse(refreshedToken, user);
				return responseBulder.buildResponse(Response.ResultCode.SUCCESS, authResponse, "OK");

			} else {
				return responseBulder.buildResponse(Response.ResultCode.ERROR, "", "Invalid token");

			}
		}
		return responseBulder.buildResponse(Response.ResultCode.ERROR, "", "Invalid token");

	}
}
