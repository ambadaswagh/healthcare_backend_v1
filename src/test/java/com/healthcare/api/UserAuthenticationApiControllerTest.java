package com.healthcare.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.api.auth.AuthTokenUtil;
import com.healthcare.api.auth.UtilsResponse;
import com.healthcare.api.auth.login.UserAuthenticationAPI;
import com.healthcare.api.auth.model.AuthRequest;
import com.healthcare.model.entity.Role;
import com.healthcare.model.entity.User;
import com.healthcare.model.response.Response;
import com.healthcare.model.response.Response.ResultCode;
import com.healthcare.service.UserService;
import com.healthcare.util.PasswordUtils;

@RunWith(MockitoJUnitRunner.class)
public class UserAuthenticationApiControllerTest {

	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private AuthTokenUtil tokenUtil;
	@Mock
	private UserService userService;

	private MockMvc mockMvc;
	private ObjectMapper objectMapper;
	private String token = "cd55fdsf5s4Xdfsfdsdfds5";

	@Before
	public void setUp() {
		UserAuthenticationAPI controller = new UserAuthenticationAPI(authenticationManager, tokenUtil, userService,
				new UtilsResponse());
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		objectMapper = new ObjectMapper();
	}

	@Test
	public void testLogin_ValidUser() throws JsonProcessingException, Exception {
		String username = "user";
		String password = "pass1234";

		// Mock Objects
		User user = getUser();

		Response expectedResonse = new Response(ResultCode.SUCCESS, user, "");
		doReturn(expectedResonse).when(userService).login(any(AuthRequest.class));
		doReturn(mock(Authentication.class)).when(authenticationManager)
				.authenticate(any(UsernamePasswordAuthenticationToken.class));
		doReturn(token).when(tokenUtil).generateToken(any(UserDetails.class));

		AuthRequest authReq = new AuthRequest();
		authReq.setUsername(username);
		authReq.setPassword(password);

		// Call API
		mockMvc.perform(post("/api/user/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(authReq)))
				.andExpect(jsonPath("result").value(ResultCode.SUCCESS.toString()))
				.andExpect(jsonPath("message").value("OK")).andExpect(jsonPath("data.token").value(token));

		assertEquals(token, user.getRememberToken());
		Mockito.verify(userService, times(1)).save(user);
	}

	@Test
	public void testLogin_WrongUserNamePassword() throws JsonProcessingException, Exception {

		// Mock And Setup
		AuthRequest authReq = new AuthRequest();
		authReq.setUsername("user");
		authReq.setPassword("pa");

		User user = getUser();
		Response expectedResponse = new Response(ResultCode.INVALID_PASSWORD, user, "");
		doReturn(expectedResponse).when(userService).login(any(AuthRequest.class));

		// Call API
		mockMvc.perform(post("/api/user/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(authReq)))
				.andExpect(jsonPath("result").value(ResultCode.INVALID_PASSWORD.toString()))
				.andExpect(jsonPath("data.token").doesNotExist());

		assertNull(user.getRememberToken());
		Mockito.verify(userService, times(0)).save(any(User.class));
	}

	@Test
	public void testLogin_Error() throws JsonProcessingException, Exception {
		AuthRequest authReq = new AuthRequest();
		authReq.setUsername("user");
		authReq.setPassword("pa");
		User user = getUser();
		doReturn(null).when(userService).login(any(AuthRequest.class));

		mockMvc.perform(post("/api/user/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(authReq)))
				.andExpect(jsonPath("result").value(ResultCode.ERROR.toString()))
				.andExpect(jsonPath("message").value("Wrong user name or password"))
				.andExpect(jsonPath("data.token").doesNotExist());

		assertNull(user.getRememberToken());
		Mockito.verify(userService, times(0)).save(any(User.class));
	}

	@Test
	public void testLogout_success() throws JsonProcessingException, Exception {
		doReturn(true).when(tokenUtil).removeToken(token);

		mockMvc.perform(post("/api/user/auth/logout").contentType(MediaType.APPLICATION_JSON).header("token", token))
				.andExpect(jsonPath("result").value(ResultCode.SUCCESS.toString()))
				.andExpect(jsonPath("message").value("Logout success! Token has been removed"));

		assertNull(SecurityContextHolder.getContext().getAuthentication());
	}

	@Test
	public void testLogout_fail() throws JsonProcessingException, Exception {
		doReturn(false).when(tokenUtil).removeToken(token);
		mockMvc.perform(post("/api/user/auth/logout").contentType(MediaType.APPLICATION_JSON).header("token", token))
				.andExpect(jsonPath("result").value(ResultCode.ERROR.toString()))
				.andExpect(jsonPath("message").value("Token is not valid"));
	}

	@Test
	public void testGetUser() throws JsonProcessingException, Exception {
		User user = getUser();
		doReturn(false).when(tokenUtil).removeToken(token);
		doReturn(user.getUsername()).when(tokenUtil).getUsernameFromToken(token);
		doReturn(getUser()).when(userService).getUser(user.getUsername());
		doReturn(true).when(tokenUtil).validateToken(anyString(), any());

		mockMvc.perform(get("/api/user/auth/profile").contentType(MediaType.APPLICATION_JSON).header("token", token))
				.andExpect(jsonPath("result").value(ResultCode.SUCCESS.toString()))
				.andExpect(jsonPath("data.username").value(user.getUsername()));

	}

	@Test
	public void testRefreshAndGetAuthenticationToken_Sucess() throws JsonProcessingException, Exception {
		String username = "user";
		String refreshToken = "Zdddssde2dde";

		// Mock Setup
		User user = getUser();
		doReturn(false).when(tokenUtil).removeToken(token);
		doReturn(username).when(tokenUtil).getUsernameFromToken(token);
		doReturn(user).when(userService).getUser(username);
		doReturn(true).when(tokenUtil).validateToken(anyString(), any());
		doReturn(refreshToken).when(tokenUtil).refreshToken(token);

		// Call API
		mockMvc.perform(
				get("/api/user/auth/token/refresh").contentType(MediaType.APPLICATION_JSON).header("token", token))
				.andExpect(jsonPath("result").value(ResultCode.SUCCESS.toString()))
				.andExpect(jsonPath("data.token").value(refreshToken));

		assertEquals(refreshToken, user.getRememberToken());
		Mockito.verify(userService, times(1)).save(user);
	}

	@Test
	public void testRefreshAndGetAuthenticationToken_Error() throws JsonProcessingException, Exception {
		String username = "user";

		// Mock
		User user = getUser();
		doReturn(false).when(tokenUtil).removeToken(token);
		doReturn(username).when(tokenUtil).getUsernameFromToken(token);
		doReturn(user).when(userService).getUser(username);
		doReturn(false).when(tokenUtil).validateToken(anyString(), any());

		// Call API
		mockMvc.perform(
				get("/api/user/auth/token/refresh").contentType(MediaType.APPLICATION_JSON).header("token", token))
				.andExpect(jsonPath("result").value(ResultCode.ERROR.toString()))
				.andExpect(jsonPath("data.token").doesNotExist());

		assertNull(user.getRememberToken());
		Mockito.verify(userService, times(0)).save(any(User.class));

	}

	@Test
	public void testRefreshAndGetAuthenticationToken_UserDetailsNotFound() throws JsonProcessingException, Exception {
		String username = "user";

		doReturn(null).when(tokenUtil).getUsernameFromToken(token);
		doReturn(null).when(userService).getUser(username);

		mockMvc.perform(
				get("/api/user/auth/token/refresh").contentType(MediaType.APPLICATION_JSON).header("token", token))
				.andExpect(jsonPath("result").value(ResultCode.ERROR.toString()))
				.andExpect(jsonPath("data.token").doesNotExist());

		Mockito.verify(userService, times(0)).save(any(User.class));
	}

	private User getUser() {
		Role role = new Role();
		role.setId(1L);
		role.setLevel(1);
		role.setLevelName("user");
		role.setStatus(1);

		User user = new User();
		user.setUsername("user");
		user.setPassword(PasswordUtils.hashPassword("pass1234"));
		user.setRole(role);
		return user;
	}
}
