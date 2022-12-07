package com.healthcare.integration.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.healthcare.model.entity.Document;
import com.healthcare.model.entity.Document;
import com.healthcare.model.enums.DocumentStatusEnum;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.DocumentService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class DocumentServiceTest {

	@Autowired
	DocumentService documentService;

	@Value("${spring.data.rest.default-page-size}")
	private int defaultMaxSize;
	
	@Before
	public void setup() {
		document = null;
	}

	Document document ;

	@After
	public void rollback() {
		if(document!=null)
			documentService.deleteById(document.getId());
	}
	
	@Test
	public void shouldSaveDocument() throws Exception  {
		document = createNewDocument();
		documentService.save(document);
		Assert.assertNotNull(document.getId());
	}

	@Test
	public void shouldGetDocument() {
		document = createNewDocument();
		documentService.save(document);
		Assert.assertNotNull(documentService.findById(document.getId()));
	}

	@Test
	public void shouldUpdateDocument() {
		document = createNewDocument();
		documentService.save(document);
		String oldEntityValue = document.getEntity();
		Long oldEntityIdValue = document.getEntityId();
		String oldFileClassValue = document.getFileClass();
		Document savedDocument = documentService.findById(document.getId());
		Assert.assertNotNull(savedDocument);
		Assert.assertNotNull(savedDocument.getId());

		savedDocument.setEntity("Emp");
		savedDocument.setEntityId(852L);
		savedDocument.setFileClass("Re-Certification");
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
	
    
	@Test
	public void testFindAllPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			Document obj = documentService.save(createNewDocument());
			listIds.add(obj.getId());
		}

		// PAGE 0
		// given
		Pageable pageable = new CustomPageRequest();
		// when
		Page<Document> result = documentService.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == defaultMaxSize);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 0);

		// PAGE 2
		pageable = new PageRequest(1, 5);
		// when
		result = documentService.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == 5);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 1);

		// delete created data
		for (Long id : listIds) {
			documentService.deleteById(id);
		}
	}

	@Test
	public void testSearchWithPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			Document obj = documentService.save(createNewDocument());
			listIds.add(obj.getId());
		}
		// SEARCH 1 : FIND BY ID
		// given
		Pageable pageable = new PageRequest(0, 25);
		Document obj = new Document();
		obj.setId(listIds.get(0));

		// when
		Page<Document> result = documentService.findAll(obj, pageable);
		// then
		assertTrue(result.getContent().size() == 1);
		assertTrue(result.getTotalElements() >= 1);
		assertTrue(result.getTotalPages() >= 1);
		assertTrue(result.getNumber() == 0);

		// SEARCH 2
		// given
		pageable = new PageRequest(3, 5);
		obj = new Document();

		// when
		result = documentService.findAll(obj, pageable);
		// then
		assertTrue(result.getContent().size() == 5);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 3);

		// Delete created data
		for (Long id : listIds) {
			documentService.deleteById(id);
		}
	}

}
