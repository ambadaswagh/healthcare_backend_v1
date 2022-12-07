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

import com.healthcare.model.entity.WorkItem;
import com.healthcare.repository.WorkItemRepository;
import com.healthcare.service.WorkItemService;

/**
 * Created by Mostafa Hamed on 30/06/17.
 * @author mhamed
 * @version 1.0
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class WorkItemServiceRedisTest {
	
	@Autowired
	public WorkItemService workItemService;
	
	@MockBean
	public WorkItemRepository workItemRepository;

	
	String itemName = "help on shopping";
	String itemNote = "help on shopping note";
	
	@Before
	public void setup() {
	}


	private Long id = 1L;

	@After
	public void rollback() {
		workItemService.deleteById(id);
	}

	@Test
	public void saveWorkItem() {
		WorkItem workItem = createWorkItem();
		workItem.setId(id);
		Mockito.when(workItemRepository.save(workItem)).thenReturn(workItem);
		workItem = workItemService.save(workItem);
		WorkItem savedWorkItem = workItemService.findById(workItem.getId());
		Assert.assertNotNull(savedWorkItem);
	}

	@Test
	public void updateWorkItem() {
		String newWorkItemName = "help on cooking";
		WorkItem workItem = createWorkItem();
		workItem.setId(id);
		Mockito.when(workItemRepository.save(workItem)).thenReturn(workItem);
		workItem = workItemService.save(workItem);
		WorkItem savedWorkItem = workItemService.findById(workItem.getId());
		savedWorkItem.setItemName(newWorkItemName);
		Mockito.when(workItemRepository.save(savedWorkItem)).thenReturn(savedWorkItem);
		workItemService.save(savedWorkItem);
		WorkItem modifiedWorkItem = workItemService.findById(workItem.getId());
		Assert.assertEquals(modifiedWorkItem.getItemName(), newWorkItemName);
	}
	
	@Test
	public void deleteWorkItem() {
		WorkItem workItem = createWorkItem();
		workItem.setId(id);
		Mockito.when(workItemRepository.save(workItem)).thenReturn(workItem);
		workItem = workItemService.save(workItem);
		Mockito.doNothing().when(workItemRepository).delete(workItem.getId());
		Assert.assertNotNull(workItemService.deleteById(workItem.getId()));
	}

	public WorkItem createWorkItem(){
		WorkItem workItem = new WorkItem();
		workItem.setItemName(itemName);
		workItem.setItemNote(itemNote);
		return workItem;
	}
	
}
