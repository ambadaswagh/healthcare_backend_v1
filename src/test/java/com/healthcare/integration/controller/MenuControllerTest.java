package com.healthcare.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.model.entity.Menu;
import com.healthcare.service.MenuService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@Transactional
public class MenuControllerTest {
	private MockMvc mockMvc;

	@MockBean
	private MenuService menuService;

	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testSaveMenu() throws Exception {
		Menu menu = new Menu();
		Mockito.when(menuService.save(menu)).thenReturn(menu);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(menu);
		this.mockMvc.perform(post("/api/menu").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetMenu() throws Exception {
		Mockito.when(menuService.findById(1L)).thenReturn(new Menu());
		this.mockMvc.perform(get("/api/menu/1")).andExpect(status().isOk());
	}

	@Test
	public void testFindAllMenu() throws Exception {
		Mockito.when(menuService.findAll()).thenReturn(new ArrayList<Menu>());
		this.mockMvc.perform(get("/api/menu")).andExpect(status().isOk());
	}

	@Test
	public void testUpdateMenu() throws Exception {
		Menu menu = new Menu();
		Mockito.when(menuService.save(menu)).thenReturn(menu);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(menu);
		this.mockMvc.perform(put("/api/menu").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testDeleteMenu() throws Exception {
		Mockito.when(menuService.deleteById(1L)).thenReturn(1L);
		this.mockMvc.perform(get("/api/menu/1")).andExpect(status().isOk());
	}

	@Test
	public void testDisableMenu() throws Exception {
		Mockito.when(menuService.disableById(1L)).thenReturn(1L);
		this.mockMvc.perform(put("/api/menu/1/disable")).andExpect(status().isOk());
	}
}
