package com.healthcare.integration.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.healthcare.EntityFactory;
import com.healthcare.model.entity.Training;
import com.healthcare.model.entity.Visit;
import com.healthcare.repository.TrainingRepository;
import com.healthcare.service.TrainingService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TrainingServiceRedisTest extends EntityFactory {
	@MockBean
	private TrainingRepository trainingRepository;

	@Autowired
	private TrainingService trainingService;

	@Autowired
	private RedisTemplate<String, Visit> redisTemplate;

	public String title = "title";
	public Calendar startTime = Calendar.getInstance();
	public Calendar endTime = Calendar.getInstance();
	public String type = "1";
	public String trainer = "trainer";
	public String location = "location";
	public String note = "note";

	@Before
	public void setup() {
		startTime.set(Calendar.YEAR, 2017);
		startTime.set(Calendar.MONTH, 1);
		startTime.set(Calendar.DAY_OF_MONTH, 1);
		endTime.set(Calendar.YEAR, 2017);
		endTime.set(Calendar.MONTH, 12);
		endTime.set(Calendar.DAY_OF_MONTH, 31);
		redisTemplate.delete(Training.class.getSimpleName());
	}

	private Long id = 1L;

	@After
	public void rollback() {
		trainingService.deleteById(id);
	}

	@Test
	public void shouldSaveATrainingToRedisAndRetrievedItFromRedis() {
		Training training = createNewTraining();
		training.setId(id);
		Mockito.when(trainingRepository.save(training)).thenReturn(training);
		trainingService.save(training);
		Training trainingSaved = trainingService.findById(id);
		Assert.assertNotNull(trainingSaved);
	}

	@Test
	public void shouldUpdateATrainingToRedis() {
		String newTitle = "title2";
		String newLocation = "location2";

		Training training = createNewTraining();
		training.setId(id);
		Mockito.when(trainingRepository.save(training)).thenReturn(training);
		trainingService.save(training);
		Training trainingSaved = trainingService.findById(training.getId());
		trainingSaved.setTitle(newTitle);
		trainingSaved.setLocation(newLocation);
		Mockito.when(trainingRepository.save(trainingSaved)).thenReturn(trainingSaved);
		trainingService.save(trainingSaved);
		Training trainingMofified = trainingService.findById(training.getId());
		Assert.assertEquals(trainingMofified.getTitle(), newTitle);
		Assert.assertEquals(trainingMofified.getLocation(), newLocation);
	}

	@Test
	public void shouldDeleteATraining() {
		Training training = createNewTraining();
		training.setId(id);
		Mockito.when(trainingRepository.save(training)).thenReturn(training);
		trainingService.save(training);
		Mockito.doNothing().when(trainingRepository).delete(id);
		Assert.assertNotNull(trainingService.deleteById(training.getId()));
	}

	@Test
	public void shouldDisableATraining() {
		Training training = createNewTraining();
		training.setId(id);
		Mockito.when(trainingRepository.save(training)).thenReturn(training);
		trainingService.save(training);
		Training savedTraining = trainingService.findById(training.getId());
		savedTraining.setStatus(0);
		Mockito.when(trainingRepository.save(savedTraining)).thenReturn(savedTraining);
		Long disableTrainingId = trainingService.disableById(savedTraining.getId());
		Assert.assertNotNull(disableTrainingId);
		Training disableTraining = trainingService.findById(disableTrainingId);
		Assert.assertEquals(0, disableTraining.getStatus());
	}

	@Test
	public void souldFindAll() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			String trainee = Integer.valueOf(25 / (i + 1)).toString();
			Training training = createNewTraining(trainee);
			training.setId(Long.valueOf(i));
			Mockito.when(trainingRepository.save(training)).thenReturn(training);
			Training obj = trainingService.save(training);
			listIds.add(obj.getId());
		}

		List<Training> trainings = trainingService.findAll();
		assertNotNull(trainings);
		assertEquals(25, trainings.size());
	}

}
