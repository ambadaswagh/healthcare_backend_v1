package com.healthcare.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.EntityFactory;
import com.healthcare.model.entity.User;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
// @WebMvcTest(controllers = {UserController.class}, secure = false)
@Transactional
public class UserControllerTest extends EntityFactory {
	private MockMvc mockMvc;
	private User user;

	@Autowired
	private WebApplicationContext wac;
	@MockBean
	private UserService userService;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		user = createNewUser();
	}

	@Test
	public void shouldAcceptSaveUserRequest() throws Exception {
		Mockito.when(userService.save(user)).thenReturn(user);

		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(user);
		
		System.out.println(jsonInString);

		this.mockMvc.perform(post("/api/user").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void shouldAcceptGetUserRequest() throws Exception {
		Mockito.when(userService.findById(1L)).thenReturn(user);
		this.mockMvc.perform(get("/api/user/1")).andExpect(status().isOk());
	}

	@Test
	public void shouldAcceptUpdateUserRequest() throws Exception {
		Mockito.when(userService.save(user)).thenReturn(user);

		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(user);

		this.mockMvc.perform(put("/api/user").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void shouldAcceptDeleteUserRequest() throws Exception {
		Mockito.when(userService.deleteById(1L)).thenReturn(1L);
		this.mockMvc.perform(get("/api/user/1")).andExpect(status().isOk());
	}
	
	@Test
	public void testFindAllUser() throws Exception {
        List<User> list = new ArrayList<>();
        Pageable pageable = new CustomPageRequest();
        User user = new User();
        PageImpl<User> page = new PageImpl<User>(list, pageable, list.size());
		Mockito.when(userService.findAll(user, pageable)).thenReturn(page);
		this.mockMvc.perform(get("/api/user")).andExpect(status().isOk());
	}

	@Test
	public void testDisableUser() throws Exception {
		Mockito.when(userService.disableById(1L)).thenReturn(1L);
		this.mockMvc.perform(put("/api/user/1/disable")).andExpect(status().isOk());
	}
}
