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

import com.healthcare.EntityFactory;
import com.healthcare.model.entity.WorkItem;
import com.healthcare.model.entity.WorkItem;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.WorkItemService;


/**
 * Created by Mostafa Hamed on 30/06/17.
 * @author mhamed
 * @version 1.0
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class WorkItemServiceTest extends EntityFactory{

	
	@Autowired 
	WorkItemService workItemService;

	@Value("${spring.data.rest.default-page-size}")
	private int defaultMaxSize;
	
	
	@Before
	public void setup() {

	}


	private Long id = 0L;

	@After
	public void rollback() {
		if(id!=0L)
			workItemService.deleteById(id);
	}

	@Test
	public void saveWorkItem() {
		WorkItem workItem = createNewWorkItem();
		workItem = workItemService.save(workItem);
		Assert.assertNotNull(workItem.getId());
		id=workItem.getId();
	}
	
	@Test
	public void getWorkItem() {
		WorkItem workItem = createNewWorkItem();
		workItem = workItemService.save(workItem);
		Assert.assertNotNull(workItemService.findById(workItem.getId()));
		id=workItem.getId();
	}

	@Test
	public void updateWorkItem() {
		String newWorkItemName = "help on cooking";
		WorkItem workItem = createNewWorkItem();
		workItem = workItemService.save(workItem);
		Assert.assertEquals(workItem.getItemName(), itemName);
		WorkItem savedWorkItem = workItemService.findById(workItem.getId());
		savedWorkItem.setItemName(newWorkItemName);
		workItemService.save(savedWorkItem);
		WorkItem modifiedWorkItem = workItemService.findById(workItem.getId());
		Assert.assertEquals(modifiedWorkItem.getItemName(), newWorkItemName);
		
		id=modifiedWorkItem.getId();
	}

	@Test
	public void deleteWorkItem() {
		WorkItem workItem = createNewWorkItem();
		workItem = workItemService.save(workItem);
		Assert.assertNotNull(workItem.getId());
		workItemService.deleteById(workItem.getId());
		Assert.assertNull(workItemService.findById(workItem.getId()));
	}
	
	
	@Test
	public void testFindAllPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			WorkItem obj = workItemService.save(createNewWorkItem());
			listIds.add(obj.getId());
		}

		// PAGE 0
		// given
		Pageable pageable = new CustomPageRequest();
		// when
		Page<WorkItem> result = workItemService.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == defaultMaxSize);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 0);

		// PAGE 2
		pageable = new PageRequest(1, 5);
		// when
		result = workItemService.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == 5);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 1);

		// delete created data
		for (Long id : listIds) {
			workItemService.deleteById(id);
		}
	}

	@Test
	public void testSearchWithPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			WorkItem obj = workItemService.save(createNewWorkItem());
			listIds.add(obj.getId());
		}
		// SEARCH 1 : FIND BY ID
		// given
		Pageable pageable = new PageRequest(0, 25);
		WorkItem obj = new WorkItem();
		obj.setId(listIds.get(0));

		// when
		Page<WorkItem> result = workItemService.findAll(obj, pageable);
		// then
		assertTrue(result.getContent().size() == 1);
		assertTrue(result.getTotalElements() >= 1);
		assertTrue(result.getTotalPages() >= 1);
		assertTrue(result.getNumber() == 0);

		// SEARCH 2
		// given
		pageable = new PageRequest(3, 5);
		obj = new WorkItem();
		obj.setItemName(itemName);

		// when
		result = workItemService.findAll(obj, pageable);
		// then
		assertTrue(result.getContent().size() == 5);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 3);

		// Delete created data
		for (Long id : listIds) {
			workItemService.deleteById(id);
		}
	}
}
