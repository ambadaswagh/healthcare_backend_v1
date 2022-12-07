package com.healthcare.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;

import javax.servlet.MultipartConfigElement;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.healthcare.model.entity.Document;
import com.healthcare.service.DocumentService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class DocumentControllerTest {

	private MockMvc mockMvc;
	private Document document;

	@Autowired
	MultipartConfigElement multipartConfigElement;

	@MockBean
	DocumentService documentService;

	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setup() {
		new File(multipartConfigElement.getLocation()).mkdirs();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		this.document = new Document();
	}

	@Test
	public void shouldAcceptSaveDocumentRequest() throws Exception {
		Mockito.when(documentService.save(document)).thenReturn(document);
		MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain",
				"Spring Framework".getBytes());

		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/document").file(multipartFile)
				.contentType(MediaType.MULTIPART_FORM_DATA).param("entity", "User").param("entityId", "12345")
				.param("fileClass", "Transportation")).andExpect(status().isOk());

		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/document").file(multipartFile)
				.contentType(MediaType.MULTIPART_FORM_DATA).param("entity", "User").param("entityId", "12345"))
				.andExpect(status().isBadRequest());

		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/document").file(multipartFile)
				.contentType(MediaType.MULTIPART_FORM_DATA).param("entity", "User")
				.param("fileClass", "Transportation")).andExpect(status().isBadRequest());

		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/document").file(multipartFile)
				.contentType(MediaType.MULTIPART_FORM_DATA).param("entityId", "12345")
				.param("fileClass", "Transportation")).andExpect(status().isBadRequest());

		multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "".getBytes());
		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/document").file(multipartFile)
				.contentType(MediaType.MULTIPART_FORM_DATA).param("entity", "User").param("entityId", "12345")
				.param("fileClass", "Transportation")).andExpect(status().isBadRequest());

	}

	@Test
	public void shouldAcceptGetDocumentRequest() throws Exception {
		Mockito.when(documentService.findById(1L)).thenReturn(document);
		this.mockMvc.perform(get("/api/document/1")).andExpect(status().isOk());
	}

	@Test
	public void shouldAcceptUpdateDocumentRequest() throws Exception {
		Mockito.when(documentService.save(document)).thenReturn(document);
		MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain",
				"Spring Framework".getBytes());

		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/document").file(multipartFile)
				.contentType(MediaType.MULTIPART_FORM_DATA).param("entity", "User").param("entityId", "12345")
				.param("fileClass", "Transportation")).andExpect(status().isOk());
	}

	@Test
	public void shouldAcceptDeleteDocumentRequest() throws Exception {
		Mockito.when(documentService.deleteById(1L)).thenReturn(1L);
		this.mockMvc.perform(get("/api/document/1")).andExpect(status().isOk());
	}
}
