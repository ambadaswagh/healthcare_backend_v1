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
import com.healthcare.api.auth.login.AuthenticationAPI;
import com.healthcare.api.auth.model.AuthRequest;
import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.Role;
import com.healthcare.model.response.Response;
import com.healthcare.model.response.Response.ResultCode;
import com.healthcare.service.AdminService;
import com.healthcare.util.PasswordUtils;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationApiControllerTest {

	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private AuthTokenUtil tokenUtil;
	@Mock
	private AdminService adminService;
	
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
	private String token = "cd55fdsf5s4Xdfsfdsdfds5";

    @Before
    public void setUp() {
        AuthenticationAPI controller = new AuthenticationAPI(authenticationManager,tokenUtil,adminService,new UtilsResponse());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testLogin_ValidUser() throws JsonProcessingException, Exception{
    	String username = "admin";
    	String password = "pass1234";
    	
    	//Mock Objects
    	Admin admin = getAdmin();
        
    	Response expectedResonse = new Response(ResultCode.SUCCESS,admin,"");
    	doReturn(expectedResonse).when(adminService).login(any(AuthRequest.class));
        doReturn(mock(Authentication.class)).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        doReturn(token).when(tokenUtil).generateToken(any(UserDetails.class));
    	
        AuthRequest authReq = new AuthRequest();
    	authReq.setUsername(username);
    	authReq.setPassword(password);
    	
    	// Call API
        mockMvc.perform(
                   post("/api/auth/login")
                           .contentType(MediaType.APPLICATION_JSON)
                           .content(objectMapper.writeValueAsBytes(authReq)))
               .andExpect(jsonPath("result").value(ResultCode.SUCCESS.toString()))
               .andExpect(jsonPath("message").value("OK"))
               .andExpect(jsonPath("data.token").value(token));
        
        assertEquals(token, admin.getRememberToken());
        Mockito.verify(adminService, times(1)).save(admin);
    }

    @Test
    public void testLogin_WrongUserNamePassword() throws JsonProcessingException, Exception{
    	
    	//Mock And Setup
    	AuthRequest authReq = new AuthRequest();
    	authReq.setUsername("admin");
    	authReq.setPassword("pa");
    	
    	Admin admin = getAdmin();
    	Response expectedResponse = new Response(ResultCode.INVALID_PASSWORD,admin,"");
    	doReturn(expectedResponse).when(adminService).login(any(AuthRequest.class));
    	
    	// Call API
        mockMvc.perform(
                   post("/api/auth/login")
                           .contentType(MediaType.APPLICATION_JSON)
                           .content(objectMapper.writeValueAsBytes(authReq)))
               .andExpect(jsonPath("result").value(ResultCode.INVALID_PASSWORD.toString()))
               .andExpect(jsonPath("data.token").doesNotExist());

        assertNull(admin.getRememberToken());
        Mockito.verify(adminService, times(0)).save(any(Admin.class));
    }

    @Test
    public void testLogin_Error() throws JsonProcessingException, Exception{
    	AuthRequest authReq = new AuthRequest();
    	authReq.setUsername("admin");
    	authReq.setPassword("pa");
    	Admin admin = getAdmin();
    	doReturn(null).when(adminService).login(any(AuthRequest.class));
    	 
        mockMvc.perform(
                   post("/api/auth/login")
                           .contentType(MediaType.APPLICATION_JSON)
                           .content(objectMapper.writeValueAsBytes(authReq)))
               .andExpect(jsonPath("result").value(ResultCode.ERROR.toString()))
               .andExpect(jsonPath("message").value("Wrong user name or password"))
               .andExpect(jsonPath("data.token").doesNotExist());
        
        assertNull(admin.getRememberToken());
        Mockito.verify(adminService, times(0)).save(any(Admin.class));
    }
    
    
    @Test
    public void testLogout_success() throws JsonProcessingException, Exception{
    	doReturn(true).when(tokenUtil).removeToken(token);
        
    	mockMvc.perform(
                   post("/api/auth/logout")
                           .contentType(MediaType.APPLICATION_JSON)
                           .header("token", token))
               .andExpect(jsonPath("result").value(ResultCode.SUCCESS.toString()))
               .andExpect(jsonPath("message").value("Logout success! Token has been removed"));
    	
    	assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    
    @Test
    public void testLogout_fail() throws JsonProcessingException, Exception{
    	doReturn(false).when(tokenUtil).removeToken(token);
    	mockMvc.perform(
                   post("/api/auth/logout")
                           .contentType(MediaType.APPLICATION_JSON)
                           .header("token", token))
               .andExpect(jsonPath("result").value(ResultCode.ERROR.toString()))
               .andExpect(jsonPath("message").value("Token is not valid"));
    }
    
    @Test
    public void testGetUser() throws JsonProcessingException, Exception{
    	Admin admin = getAdmin();
    	doReturn(false).when(tokenUtil).removeToken(token);
    	doReturn(admin.getUsername()).when(tokenUtil).getUsernameFromToken(token);
    	doReturn(getAdmin()).when(adminService).getUser(admin.getUsername());
    	doReturn(true).when(tokenUtil).validateToken(anyString(),any());
    	
    	mockMvc.perform(
                   get("/api/auth/profile")
                           .contentType(MediaType.APPLICATION_JSON)
                           .header("token", token))
    	.andExpect(jsonPath("result").value(ResultCode.SUCCESS.toString()))
    	.andExpect(jsonPath("data.username").value(admin.getUsername()));
    	
    }
    
    @Test
    public void testRefreshAndGetAuthenticationToken_Sucess() throws JsonProcessingException, Exception{
    	String username = "admin";
    	String refreshToken = "Zdddssde2dde";

    	//Mock Setup
    	Admin admin = getAdmin();
    	doReturn(false).when(tokenUtil).removeToken(token);
        doReturn(username).when(tokenUtil).getUsernameFromToken(token);
        doReturn(admin).when(adminService).getUser(username);
        doReturn(true).when(tokenUtil).validateToken(anyString(),any());
        doReturn(refreshToken).when(tokenUtil).refreshToken(token);
        
        // Call API
        mockMvc.perform(
                   get("/api/auth/token/refresh")
                           .contentType(MediaType.APPLICATION_JSON)
                           .header("token", token))
               .andExpect(jsonPath("result").value(ResultCode.SUCCESS.toString()))
               .andExpect(jsonPath("data.token").value(refreshToken));
        

        assertEquals(refreshToken, admin.getRememberToken());
        Mockito.verify(adminService, times(1)).save(admin);
    }
    
    
    @Test
    public void testRefreshAndGetAuthenticationToken_Error() throws JsonProcessingException, Exception{
    	String username = "admin";
    	
    	// Mock
    	Admin admin = getAdmin();
    	doReturn(false).when(tokenUtil).removeToken(token);
        doReturn(username).when(tokenUtil).getUsernameFromToken(token);
        doReturn(admin).when(adminService).getUser(username);
        doReturn(false).when(tokenUtil).validateToken(anyString(),any());
        
        // Call API
        mockMvc.perform(
                   get("/api/auth/token/refresh")
                           .contentType(MediaType.APPLICATION_JSON)
                           .header("token", token))
               .andExpect(jsonPath("result").value(ResultCode.ERROR.toString()))
               .andExpect(jsonPath("data.token").doesNotExist());

        assertNull(admin.getRememberToken());
        Mockito.verify(adminService, times(0)).save(any(Admin.class));

    }
    
    @Test
    public void testRefreshAndGetAuthenticationToken_AdminDetailsNotFound() throws JsonProcessingException, Exception{
    	String username = "admin";
    	
        doReturn(null).when(tokenUtil).getUsernameFromToken(token);
        doReturn(null).when(adminService).getUser(username);
        
        mockMvc.perform(
                   get("/api/auth/token/refresh")
                           .contentType(MediaType.APPLICATION_JSON)
                           .header("token", token))
               .andExpect(jsonPath("result").value(ResultCode.ERROR.toString()))
               .andExpect(jsonPath("data.token").doesNotExist());
    
        Mockito.verify(adminService, times(0)).save(any(Admin.class));
    }
    
	private Admin getAdmin() {
		Role role = new Role();
		role.setId(1L);
		role.setLevel(1);
		role.setLevelName("admin");
		role.setStatus(1);
		
		Admin adminUser = new Admin();
    	adminUser.setUsername("admin");
    	adminUser.setPassword(PasswordUtils.hashPassword("pass1234"));
    	adminUser.setRole(role);
    	return adminUser;
	}
 }
