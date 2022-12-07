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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.healthcare.EntityFactory;
import com.healthcare.model.entity.Training;
import com.healthcare.model.entity.Visit;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.TrainingService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TrainingServiceTest extends EntityFactory {
	@Autowired
	private TrainingService trainingService;

	@Autowired
	private RedisTemplate<String, Visit> redisTemplate;

	@Value("${spring.data.rest.default-page-size}")
	private int defaultMaxSize;

	private Long id;

	@Before
	public void init() {
		id = 0L;
		redisTemplate.delete(Training.class.getSimpleName());
	}

	@After
	public void rollback() {
		if (id != 0L)
			trainingService.deleteById(id);
	}

	@Test
	public void testSaveTraining() {
		Training training = createNewTraining();
		trainingService.save(training);
		Assert.assertNotNull(training.getId());
		id = training.getId();
	}

	@Test
	public void testGetTraining() {
		Training training = createNewTraining();
		trainingService.save(training);
		Assert.assertNotNull(trainingService.findById(training.getId()));
		id = training.getId();
	}

	@Test
	public void testUpdateTraining() {
		String newTrainer = "Trainer 2";
		String newLocation = "Location 2";

		Training training = createNewTraining();
		trainingService.save(training);
		Assert.assertEquals(training.getTrainer(), trainer);
		Assert.assertEquals(training.getLocation(), location);
		Training trainingSaved = trainingService.findById(training.getId());
		trainingSaved.setTrainer(newTrainer);
		trainingSaved.setLocation(newLocation);
		trainingService.save(trainingSaved);
		Training trainingMofified = trainingService.findById(training.getId());
		Assert.assertEquals(trainingMofified.getTrainer(), newTrainer);
		Assert.assertEquals(trainingMofified.getLocation(), newLocation);
		id = trainingMofified.getId();
	}

	@Test
	public void testDeleteTraining() {
		Training training = createNewTraining();
		trainingService.save(training);
		Assert.assertNotNull(training.getId());
		trainingService.deleteById(training.getId());
		Assert.assertNull(trainingService.findById(training.getId()));
	}

	@Test
	public void testFindAllPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			Training obj = trainingService.save(createNewTraining());
			listIds.add(obj.getId());
		}

		// PAGE 0
		// given
		Pageable pageable = new CustomPageRequest();
		// when
		Page<Training> result = trainingService.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == defaultMaxSize);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 0);

		// PAGE 2
		pageable = new PageRequest(1, 5);
		// when
		result = trainingService.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == 5);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 1);

		// delete created data
		for (Long id : listIds) {
			trainingService.deleteById(id);
		}
	}

	@Test
	public void testSearchWithPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			Training obj = trainingService.save(createNewTraining());
			listIds.add(obj.getId());
		}
		// SEARCH 1 : FIND BY ID
		// given
		Pageable pageable = new PageRequest(0, 25);
		Training obj = new Training();
		obj.setId(listIds.get(0));
		obj.setStatus(1);

		// when
		Page<Training> result = trainingService.findAll(obj, pageable);
		// then
		assertTrue(result.getContent().size() == 1);
		assertTrue(result.getTotalElements() >= 1);
		assertTrue(result.getTotalPages() >= 1);
		assertTrue(result.getNumber() == 0);

		// SEARCH 2
		// given
		pageable = new PageRequest(3, 5);
		obj = new Training();
		obj.setStatus(1);

		// when
		result = trainingService.findAll(obj, pageable);
		// then
		assertTrue(result.getContent().size() == 5);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 3);

		// Delete created data
		for (Long id : listIds) {
			trainingService.deleteById(id);
		}
	}

	@Test
	public void testFindByTraineeIdPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			String trainee = Integer.valueOf(25 / (i + 1)).toString();
			System.out.println(trainee);
			Training obj = trainingService.save(createNewTraining(trainee));
			listIds.add(obj.getId());
		}

		List<Training> trainings = trainingService.findByTraineeId("5");
		Assert.assertEquals(1, trainings.size());
		trainings = trainingService.findByTraineeId("2");
		Assert.assertEquals(4, trainings.size());
	}
}
