package com.healthcare.integration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.healthcare.DbUnitIntegrationTestConfiguration;
import com.healthcare.dto.TodolistDTO;
import com.healthcare.model.entity.Todolist;
import com.healthcare.service.TodolistService;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(value = {
		DbUnitTestExecutionListener.class }, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@DatabaseSetup(value = "/dataset/service/TodolistServiceImplIntegrationTest.xml", type = DatabaseOperation.CLEAN_INSERT)
@ContextConfiguration(classes = { DbUnitIntegrationTestConfiguration.class })
@Transactional
@SpringBootTest
public class TodolistServiceImplTest {

	@Autowired
	private TodolistService todolistService;

	private Todolist todolist;

	@Before
	public void setup() {
		todolist = null;
	}

	@After
	public void rollback() {
		if (todolist != null)
			todolistService.deleteById(todolist.getId());
	}

	@Test
	public void testGetAll() {
		List<Todolist> todos = todolistService.getAll();
		assertThat(todos, notNullValue());
		assertTrue(todos.size() > 0);
	}

	@Test
	public void testGetByAdmin() {
		List<Todolist> todos = todolistService.getByAdmin(1L);
		assertThat(todos, notNullValue());
		assertThat(todos.size() == 2);
		assertEquals(todos.get(0).getContent(), "Say hello");
	}

	@Test
	public void testGetByAdminAndStatus() {
		List<Todolist> todos = todolistService.getByAdminAndStatus(1L, 0);
		assertThat(todos, notNullValue());
		assertThat(todos.size() == 1);
		assertEquals(todos.get(0).getContent(), "Say hello 1");
	}

	@Test
	public void testClose() {
		todolistService.close(2L);
		Todolist todo = todolistService.findById(2L);
		assertEquals(todo.getStatus(), 1);

		Long result = todolistService.close(2L);
		assertEquals(result.longValue(), -2);
		
		result = todolistService.close(100L);
		assertEquals(result.longValue(), -1);
	}

	@Test
	public void testCreate() {
		TodolistDTO todo = new TodolistDTO();
		todo.setAdminId(1L);
		todo.setContent("Say goodbye");
		Long id = todolistService.create(todo);

		Todolist result = todolistService.findById(id);
		assertThat(result, notNullValue());
		assertThat(result.getId(), notNullValue());
	}
}