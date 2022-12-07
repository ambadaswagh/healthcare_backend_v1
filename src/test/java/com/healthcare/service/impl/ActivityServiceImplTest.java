package com.healthcare.service.impl;

import com.healthcare.model.entity.Activity;
import com.healthcare.repository.ActivityRepository;
import com.healthcare.service.ActivityService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.only;

@RunWith(MockitoJUnitRunner.class)
public class ActivityServiceImplTest {

    private static final String REDIS_KEY = Activity.class.getSimpleName();

    private ActivityService sut;

    @Mock
    private ActivityRepository activityRepository;
    @Mock
    private RedisTemplate redisTemplate;
    @Mock
    private HashOperations hashOperations;

    @Before
    public void setUp() {
        sut = new ActivityServiceImpl(activityRepository, redisTemplate);
    }

    @Test
    public void testSave() {
        // given
        final Long activityId = 1L;
        final Activity activity = new Activity();
        final Activity expected = new Activity();
        expected.setId(activityId);

        given(activityRepository.save(any(Activity.class)))
                .willReturn(expected);
        given(redisTemplate.opsForHash())
                .willReturn(hashOperations);
        // when
        Activity result = sut.save(activity);
        // then
        verify(activityRepository, only()).save(activity);
        verify(redisTemplate, only()).opsForHash();
        verify(hashOperations, only()).put(REDIS_KEY, activityId, expected);

        assertThat(result, notNullValue());
        assertThat(result, sameInstance(expected));
    }

    @Test
    public void testFindByIdWhenRedisHaveData() {
        // given
        final Long activityId = 1L;
        final Activity expected = new Activity();

        given(redisTemplate.opsForHash())
                .willReturn(hashOperations);
        given(hashOperations.get(anyString(), anyLong()))
                .willReturn(expected);
        // when
        Activity result = sut.findById(activityId);
        // then
        verify(redisTemplate, only()).opsForHash();
        verify(hashOperations, only()).get(REDIS_KEY, activityId);

        assertThat(result, notNullValue());
        assertThat(result, sameInstance(expected));
    }

    @Test
    public void testFindByIdWhenRedisNotHaveData() {
        // given
        final Long activityId = 1L;
        final Activity expected = new Activity();

        given(redisTemplate.opsForHash())
                .willReturn(hashOperations);
        given(hashOperations.get(anyString(), anyLong()))
                .willReturn(null);
        given(activityRepository.findOne(anyLong()))
                .willReturn(expected);
        // when
        Activity result = sut.findById(activityId);
        // then
        verify(redisTemplate, only()).opsForHash();
        verify(hashOperations, only()).get(REDIS_KEY, activityId);
        verify(activityRepository, only()).findOne(activityId);

        assertThat(result, notNullValue());
        assertThat(result, sameInstance(expected));
    }

    @Test
    public void testDeleteById() {
        // given
        final Long activityId = 1L;

        given(redisTemplate.opsForHash())
                .willReturn(hashOperations);
        // when
        sut.deleteById(activityId);
        // then
        verify(activityRepository, only()).delete(activityId);
        verify(redisTemplate, only()).opsForHash();
        verify(hashOperations, only()).delete(REDIS_KEY, activityId);
    }
}
