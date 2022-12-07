package com.healthcare.integration.service;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.healthcare.model.entity.Document;
import com.healthcare.model.enums.DocumentStatusEnum;
import com.healthcare.repository.DocumentRepository;
import com.healthcare.service.DocumentService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class DocumentServiceRedisTest {

	@Autowired
	DocumentService documentService;

	@MockBean
	DocumentRepository documentRepository;
	
	@Before
	public void setup() {
		document = null;
	}


	private Long id = 7L;
	private Document document;
	
	@After
	public void rollback() {
		if(document!=null)
			documentService.deleteById(document.getId());
	}

	@Test
	public void shouldSaveDocument() throws Exception  {
		document = createNewDocument();
		document.setId(id);
		Mockito.when(documentRepository.save(document)).thenReturn(document);
		document = documentService.save(document);
		Assert.assertNotNull(document.getId());
	}

	@Test
	public void shouldGetDocument() {
		document = createNewDocument();
		document.setId(id);
		Mockito.when(documentRepository.save(document)).thenReturn(document);
		document = documentService.save(document);
		Assert.assertNotNull(documentService.findById(id));
	}

	@Test
	public void shouldUpdateDocument() {
		document = createNewDocument();
		document.setId(id);
		Mockito.when(documentRepository.save(document)).thenReturn(document);
		documentService.save(document);

		String oldEntityValue = document.getEntity();
		Long oldEntityIdValue = document.getEntityId();
		String oldFileClassValue = document.getFileClass();
		Document savedDocument = documentService.findById(id);
		Assert.assertNotNull(savedDocument);
		Assert.assertNotNull(savedDocument.getId());

		savedDocument.setEntity("Emp");
		savedDocument.setEntityId(852L);
		savedDocument.setFileClass("Re-Certification");

		Mockito.when(documentRepository.save(savedDocument)).thenReturn(savedDocument);
		documentService.save(savedDocument);
		Document modifiedDocument = documentService.findById(document.getId());
		Assert.assertNotNull(modifiedDocument);
		Assert.assertNotNull(modifiedDocument.getId());
		Assert.assertNotEquals(modifiedDocument.getEntity(), oldEntityValue);
		Assert.assertNotEquals(modifiedDocument.getEntityId(), oldEntityIdValue);
		Assert.assertNotEquals(modifiedDocument.getFileClass(), oldFileClassValue);
	}
	
	@Test
	public void shouldDeleteDocument() {
		Document document = createNewDocument();
		document.setId(10L); 
		Mockito.when(documentRepository.save(document)).thenReturn(document);
		documentService.save(document);
		
		Assert.assertNotNull(documentService.findById(document.getId()));
		documentService.deleteById(document.getId());
		Assert.assertNull(documentService.findById(document.getId()));
	}

	private Document createNewDocument() {
		Document document = new Document();
		document.setEntity("User");
		document.setEntityId(123L);
		document.setFileClass("Transtportation");
		document.setStatus(DocumentStatusEnum.ACTIVE.getValue());
		document.setFilePath("/data/upload/");
		document.setFileUrl("/data/upload/test.txt");
		document.setFileName("test.txt");
		document.setFileSize(12566L);

		return document;
	}

}
