package com.healthcare.integration.service;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.healthcare.DbUnitIntegrationTestConfiguration;
import com.healthcare.model.entity.Activity;
import com.healthcare.model.entity.Employee;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.ActivityService;

@RunWith(SpringRunner.class)
@TestExecutionListeners(value = {
		DbUnitTestExecutionListener.class }, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@DatabaseSetup(value = "/dataset/service/ActivityServiceImplIntegrationTest.xml")
@ContextConfiguration(classes = { DbUnitIntegrationTestConfiguration.class })
@Transactional
@SpringBootTest
public class ActivityServiceImplTest {

	@Value("${spring.data.rest.default-page-size}")
	private int defaultMaxSize;

	@Autowired
	private ActivityService sut;

	@Autowired
	private EntityManager em;
	private Activity activity;

	@Before
	public void setup() {
		activity = null;
	}

	@After
	public void rollback() {
		if (activity != null && activity.getId() != null) {
			if (sut.findById(activity.getId()) != null)
				sut.deleteById(activity.getId());
		}
	}

	@Test
	public void testCreate() {
		// given
		final Long employeeId = 100L;
		final Long activityId = 100L;
		Activity activity = new Activity();
		activity.setId(activityId);
		activity.setName("Name");

		final Employee employee = new Employee();
		employee.setId(employeeId);
		activity.setInstructorEmployee(employee);
		// when
		Activity result = sut.save(activity);
		// then
		assertThat(result, notNullValue());
		assertThat(result.getId(), notNullValue());
	}

	@Test
	@ExpectedDatabase(value = "/dataset/service/ActivityServiceImplIntegrationTest.testUpdate.expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void testUpdate() {
		// given
		final Long activityId = 100L;
		final Long employeeId = 100L;
		activity = new Activity();
		activity.setId(activityId);
		activity.setName("Name 1");

		final Employee employee = new Employee();
		employee.setId(employeeId);
		activity.setInstructorEmployee(employee);
		// when
		Activity result = sut.save(activity);
		// then
		assertThat(result, notNullValue());

		em.flush();
	}

	@Test
	public void testFindById() {
		// given
		final Long activityId = 100L;
		// when
		Activity result = sut.findById(activityId);
		// then
		assertThat(result, notNullValue());
	}

	@Test
	public void testFindAll() {
		// when
		List<Activity> result = sut.findAll();
		// then
		assertTrue(result.size() > 0);
	}

	@Test
	@ExpectedDatabase(value = "/dataset/service/ActivityServiceImplIntegrationTest.testDelete.expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void testDeleteById() {
		// given
		final Long activityId = 100L;
		// when
		sut.deleteById(activityId);
		// then
		em.flush();
	}

	@Test
	public void testFindAllPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			Activity activity = createActivity();
			listIds.add(activity.getId());
		}

		// PAGE 0
		// given
		Pageable pageable = new CustomPageRequest();
		// when
		Page<Activity> result = sut.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == defaultMaxSize);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 0);

		// PAGE 2
		pageable = new PageRequest(1, 5);
		// when
		result = sut.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == 5);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 1);

		// delete created data
		for (Long id : listIds) {
			sut.deleteById(id);
		}
	}

	@Test
	public void testSearchWithPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			Activity activity = createActivity();
			listIds.add(activity.getId());
		}
		// SEARCH 1 : FIND BY ID
		// given
		Pageable pageable = new PageRequest(0, 25);
		Activity activity = new Activity();
		activity.setId(listIds.get(0));

		// when
		Page<Activity> result = sut.findAll(activity, pageable);
		// then
		assertTrue(result.getContent().size() == 1);
		assertTrue(result.getTotalElements() >= 1);
		assertTrue(result.getTotalPages() >= 1);
		assertTrue(result.getNumber() == 0);

		// SEARCH 2 : FIND BY NAME
		// given
		pageable = new PageRequest(3, 5);
		activity = new Activity();
		activity.setName("Name");

		// when
		result = sut.findAll(activity, pageable);
		// then
		assertTrue(result.getContent().size() == 5);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 3);

		// Delete created data
		for (Long id : listIds) {
			sut.deleteById(id);
		}
	}

	private Activity createActivity() {
		// given
		final Long employeeId = 100L;
		Activity activity = new Activity();
		activity.setName("Name");

		final Employee employee = new Employee();
		employee.setId(employeeId);
		activity.setInstructorEmployee(employee);
		// when
		return sut.save(activity);
	}

}