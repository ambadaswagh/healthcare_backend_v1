package com.healthcare.integration.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Base64;

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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.model.entity.Content;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.ContentService;
import com.healthcare.util.UserTypeJsonConverter;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@Transactional
public class ContentControllerTest {
	private MockMvc mockMvc;

	@MockBean
	private ContentService contentService;

	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testSaveContent() throws Exception {
		Content content = getContent();
		Mockito.when(contentService.save(content)).thenReturn(content);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(content);
		this.mockMvc.perform(post("/api/content").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testSaveAndReadContent() throws Exception {
		String textualDataOfFile = "Test file read from server and now put back to server";
		Content content = getContent();
		content.setContent(Base64.getEncoder().encode(textualDataOfFile.getBytes()));
		
		Mockito.when(contentService.save(content)).thenReturn(content);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(content);
		MvcResult result = (MvcResult) this.mockMvc.perform(post("/api/content").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk()).andReturn();
		
		//Capture response and validate byte array data
		String json = result.getResponse().getContentAsString();
		Content response = (Content) UserTypeJsonConverter.fromJsonString(json, Content.class);
		byte[] responseDecodedContent = Base64.getDecoder().decode(response.getContent());
		
		assertEquals(textualDataOfFile, new String(responseDecodedContent));
	}
	
	@Test
	public void testGetContent() throws Exception {
		Mockito.when(contentService.findById(1L)).thenReturn(new Content());
		this.mockMvc.perform(get("/api/content/1")).andExpect(status().isOk());
	}

	@Test
	public void testFindAllContent() throws Exception {
        ArrayList<Content> list = new ArrayList<>();
        Pageable pageable = new CustomPageRequest();
        Content content = new Content();
        PageImpl<Content> page = new PageImpl<Content>(list, pageable, list.size());
		Mockito.when(contentService.findAll(content, pageable)).thenReturn(page);
		this.mockMvc.perform(get("/api/content")).andExpect(status().isOk());
	}

	@Test
	public void testUpdateContent() throws Exception {
		Content Content = new Content();
		Mockito.when(contentService.save(Content)).thenReturn(Content);
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(Content);
		this.mockMvc.perform(put("/api/content").contentType(MediaType.APPLICATION_JSON).content(jsonInString))
				.andExpect(status().isOk());
	}

	@Test
	public void testDeleteContent() throws Exception {
		Mockito.when(contentService.deleteById(1L)).thenReturn(1L);
		this.mockMvc.perform(delete("/api/content/1")).andExpect(status().isOk());
	}
	
	private Content getContent() {
		final Content content = new Content();
		content.setId(1L);
		//content.setAccessKey("New Acess Key");
		//content.setBaseId(1L);
		content.setContent(Base64.getEncoder().encode("SampleString".getBytes()));
		//content.setPageDescription("page desc");
		//content.setPageKeyword("page keyword");
		content.setTitle("title");
		//content.setPageTitle("page title");
		content.setStatus(1);
		return content;
	}

}
