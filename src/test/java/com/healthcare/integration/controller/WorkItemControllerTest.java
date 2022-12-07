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
import com.healthcare.model.entity.WorkItem;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.WorkItemService;


/**
 * Created by Mostafa Hamed on 30/06/17.
 * @author mhamed
 * @version 1.0
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@Transactional
public class WorkItemControllerTest {
	
	private MockMvc mockMvc;

	@MockBean
	private WorkItemService workItemService;

	@Autowired
	private WebApplicationContext webAppContext;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webAppContext).build();
	}
	

	@Test
	public void saveWorkItem() throws Exception {
		WorkItem workItem = new WorkItem();
		Mockito.when(workItemService.save(workItem)).thenReturn(workItem);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(workItem);
		this.mockMvc.perform(post("/api/work_item").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void getWorkItem() throws Exception {
		Mockito.when(workItemService.findById(1L)).thenReturn(new WorkItem());
		this.mockMvc.perform(get("/api/work_item/1")).andExpect(status().isOk());
	}

	@Test
	public void findAllWorkItems() throws Exception {
        List<WorkItem> list = new ArrayList<>();
        Pageable pageable = new CustomPageRequest();
        WorkItem workItem = new WorkItem();
        PageImpl<WorkItem> page = new PageImpl<WorkItem>(list, pageable, list.size());
		Mockito.when(workItemService.findAll(workItem, pageable)).thenReturn(page);
		this.mockMvc.perform(get("/api/work_item")).andExpect(status().isOk());
	}

	@Test
	public void updateWorkItem() throws Exception {
		WorkItem workItem = new WorkItem();
		Mockito.when(workItemService.save(workItem)).thenReturn(workItem);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(workItem);
		this.mockMvc.perform(put("/api/work_item").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void deleteWorkItem() throws Exception {
		Mockito.when(workItemService.deleteById(1L)).thenReturn(1L);
		this.mockMvc.perform(get("/api/work_item/1")).andExpect(status().isOk());
	}

}
