package com.healthcare.integration.service;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

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

import com.healthcare.model.entity.Activity;
import com.healthcare.model.entity.Visit;
import com.healthcare.model.entity.VisitActivity;
import com.healthcare.model.entity.VisitActivityPK;
import com.healthcare.repository.VisitActivityRepository;
import com.healthcare.service.VisitActivityService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class VisitActivityServiceRedisTest {
	@MockBean
	private VisitActivityRepository visitActivityRepository;

	@Autowired
	private VisitActivityService visitActivityService;

	@Autowired
	private RedisTemplate<String, VisitActivity> redisTemplate;

	@Before
	public void setup() {
		redisTemplate.delete(VisitActivity.class.getSimpleName());
	}

	// Remove data added during test from redis once test case executed
	// successfully
	public void cleanup(Long visitId, Long activityId) {
		visitActivityService.deleteById(new VisitActivityPK(visitId, activityId));
	}

	private VisitActivityPK getPk(Long visitId, Long activityId) {
		return new VisitActivityPK(visitId, activityId);
	}

	@Test
	public void testSaveVisitActivityToRedisAndRetrievedItFromRedis() {
		// given
		final VisitActivity visitActivity = getVisitActivity();
		visitActivity.setVisitId(100L);
		visitActivity.setActivityId(100L);

		// Mock
		Mockito.when(visitActivityRepository.save(visitActivity)).thenReturn(visitActivity);
		visitActivityService.save(visitActivity);

		// Execute
		VisitActivity visitActivitySaved = visitActivityService.findById(getPk(100L, 100L));

		// Assert
		Assert.assertNotNull(visitActivitySaved);
		cleanup(100L, 100L);
	}

	@Test
	public void testUpdateVisitActivityToRedis() {
		String tableName = "table name updated in redis";

		// given
		final VisitActivity visitActivity = getVisitActivity();
		visitActivity.setActivityId(100L);
		visitActivity.setVisitId(100L);

		// Mock
		Mockito.when(visitActivityRepository.save(visitActivity)).thenReturn(visitActivity);
		visitActivityService.save(visitActivity);

		VisitActivityPK pk = new VisitActivityPK(100L, 100L);
		VisitActivity savedVisitActivityInRedis = visitActivityService.findById(pk);
		savedVisitActivityInRedis.setTableName(tableName);

		Mockito.when(visitActivityRepository.save(savedVisitActivityInRedis)).thenReturn(savedVisitActivityInRedis);
		visitActivityService.save(savedVisitActivityInRedis);

		VisitActivity modifiedVisitActivityFromRedis = visitActivityService.findById(pk);

		// Assert
		Assert.assertEquals(modifiedVisitActivityFromRedis.getTableName(), tableName);
		cleanup(100L, 100L);
	}

	@Test
	public void testDeleteVisitActivityFromRedis() {
		// given and save the data
		final VisitActivity visitActivity = getVisitActivity();
		Mockito.when(visitActivityRepository.save(visitActivity)).thenReturn(visitActivity);
		visitActivityService.save(visitActivity);

		// mock for delete
		VisitActivityPK pk = new VisitActivityPK(1L, 1L);
		Mockito.doNothing().when(visitActivityRepository).delete(pk);

		// Assert delete
		Assert.assertNotNull(visitActivityService.deleteById(pk));
	}

	// If activity or visit is not null then fetch from redis
	@Test
	public void testFindVisitActivityByActivityId_ActivityAndVisitAreNoteNull() {
		final VisitActivity visitActivity = getVisitActivity();
		Mockito.when(visitActivityRepository.save(visitActivity)).thenReturn(visitActivity);
		visitActivityService.save(visitActivity);

		List<VisitActivity> visitActivityList = visitActivityService.findVisitActivityByActivityId(1L);
		assertEquals(1, visitActivityList.size());

	}

	// If activity or visit is null in object then fetch from database and store
	// in redis
	@Test
	public void testFindVisitActivityByActivityId_ActivityOrVisitIsNull() {
		final Long visitId = 1L;
		final VisitActivity visitActivity = getVisitActivity();
		visitActivity.setActivity(null);
		Mockito.when(visitActivityRepository.save(visitActivity)).thenReturn(visitActivity);
		visitActivityService.save(visitActivity);

		// Return list
		VisitActivity visitActivityNotNullVisitAndActivity = getVisitActivity();
		Mockito.when(visitActivityRepository.findVisitActivityByActivityId(visitId))
				.thenReturn(Collections.singletonList(visitActivityNotNullVisitAndActivity));

		List<VisitActivity> visitActivityList = visitActivityService.findVisitActivityByActivityId(visitId);
		assertEquals(1, visitActivityList.size());
	}

	// If activity or visit is not null then fetch from redis
	@Test
	public void testFindVisitActivityByVisitId_ActivityAndVisitAreNoteNull() {
		final VisitActivity visitActivity = getVisitActivity();
		Mockito.when(visitActivityRepository.save(visitActivity)).thenReturn(visitActivity);
		visitActivityService.save(visitActivity);

		List<VisitActivity> visitActivityList = visitActivityService.findVisitActivityByVisitId(1L);
		assertEquals(1, visitActivityList.size());

	}

	// If activity or visit is null in object then fetch from database and store
	// in redis
	@Test
	public void testFindVisitActivityByVisitId_ActivityOrVisitIsNull() {
		final Long visitId = 1L;
		final VisitActivity visitActivity = getVisitActivity();
		visitActivity.setVisit(null);
		Mockito.when(visitActivityRepository.save(visitActivity)).thenReturn(visitActivity);
		visitActivityService.save(visitActivity);

		// Return list
		VisitActivity visitActivityNotNullVisitAndActivity = getVisitActivity();
		Mockito.when(visitActivityRepository.findVisitActivityByVisitId(visitId))
				.thenReturn(Collections.singletonList(visitActivityNotNullVisitAndActivity));

		List<VisitActivity> visitActivityList = visitActivityService.findVisitActivityByVisitId(visitId);
		assertEquals(1, visitActivityList.size());
	}

	public Activity getActivity() {
		Activity activity = new Activity();
		activity.setId(1L);
		activity.setStatus(1);
		activity.setName("activity name");

		return activity;
	}

	public Visit getVisit() {
		Visit visit = new Visit();
		visit.setId(1L);
		visit.setStatus("1");
		visit.setNotes("visit notes");

		return visit;
	}

	private VisitActivity getVisitActivity() {
		VisitActivity visitActivity = new VisitActivity();
		visitActivity.setActivityId(1L);
		visitActivity.setVisitId(1L);
		visitActivity.setTableName("table name");
		visitActivity.setVisit(getVisit());
		visitActivity.setActivity(getActivity());
		return visitActivity;
	}
}