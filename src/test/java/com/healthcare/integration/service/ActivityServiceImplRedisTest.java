package com.healthcare.integration.service;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.only;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.healthcare.model.entity.Activity;
import com.healthcare.repository.ActivityRepository;
import com.healthcare.service.ActivityService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityServiceImplRedisTest {

	@Autowired
	private ActivityService activityService;

	@MockBean
	private ActivityRepository activityRepository;

	private Long id = 1L;
	private Activity activity;

	@Before
	public void setup() {
		activity = null;
	}

	@After
	public void rollback() {
		if (activity != null)
			activityService.deleteById(activity.getId());
	}

	@Test
	public void testCreate() {
		// given
		final Long activityId = id;
		activity = new Activity();
		activity.setId(activityId);

		given(activityRepository.save(any(Activity.class))).willReturn(activity);
		// when
		activityService.save(activity);
		// then
		verify(activityRepository, only()).save(activity);

		Activity result = activityService.findById(activity.getId());

		assertThat(result, notNullValue());
	}

	@Test
	public void testUpdate() {
		// given
		final Long activityId = id;
		final String name = "Activity name";
		activity = new Activity();
		activity.setId(activityId);

		given(activityRepository.save(any(Activity.class))).willReturn(activity);
		activityService.save(activity);
		activity.setName(name);
		// when
		activityService.save(activity);
		// then
		verify(activityRepository, atLeast(1)).save(activity);

		Activity result = activityService.findById(activity.getId());

		assertThat(result, notNullValue());
		assertThat(result.getName(), IsEqual.equalTo(name));
	}

	@Test
	public void testDeleteById() {
		// given
		final Long activityId = 1L;
		final Activity activity = new Activity();
		activity.setId(activityId);

		given(activityRepository.save(any(Activity.class))).willReturn(activity);
		activityService.save(activity);
		// when
		Long result = activityService.deleteById(activity.getId());
		// then
		verify(activityRepository).delete(activity.getId());
		assertThat(result, notNullValue());

		Activity savedActivity = activityService.findById(activity.getId());

		assertThat(savedActivity, nullValue());
	}

	@Test
	public void testDisableById() {
		// given
		final Long activityId = 1L;
		final Activity activity = new Activity();
		activity.setId(activityId);
		activity.setStatus(1);
		given(activityRepository.save(any(Activity.class))).willReturn(activity);
		activityService.save(activity);

		// when
		Long result = activityService.disableById(activity.getId());

		// then
		verify(activityRepository).save(activity);
		assertThat(result, notNullValue());

		Activity disableActivity = activityService.findById(activity.getId());
		assertThat(disableActivity, notNullValue());
		assertThat(disableActivity.getStatus(), comparesEqualTo(0));
	}
}