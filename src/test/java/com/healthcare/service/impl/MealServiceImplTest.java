package com.healthcare.service.impl;

import com.healthcare.model.entity.Meal;
import com.healthcare.repository.MealRepository;
import com.healthcare.service.MealService;
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
public class MealServiceImplTest {

    private static final String REDIS_KEY = Meal.class.getSimpleName();

    private MealService sut;

    @Mock
    private MealRepository mealRepository;
    @Mock
    private RedisTemplate redisTemplate;
    @Mock
    private HashOperations hashOperations;

    @Before
    public void setUp() {
        sut = new MealServiceImpl(mealRepository, redisTemplate);
    }

    @Test
    public void testSave() {
        // given
        final Long mealId = 1L;
        final Meal meal = new Meal();
        final Meal expected = new Meal();
        expected.setId(mealId);

        given(mealRepository.save(any(Meal.class)))
                .willReturn(expected);
        given(redisTemplate.opsForHash())
                .willReturn(hashOperations);
        // when
        Meal result = sut.save(meal);
        // then
        verify(mealRepository, only()).save(meal);
        verify(redisTemplate, only()).opsForHash();
        verify(hashOperations, only()).put(REDIS_KEY, mealId, expected);

        assertThat(result, notNullValue());
        assertThat(result, sameInstance(expected));
    }

    @Test
    public void testFindByIdWhenRedisHaveData() {
        // given
        final Long mealId = 1L;
        final Meal expected = new Meal();

        given(redisTemplate.opsForHash())
                .willReturn(hashOperations);
        given(hashOperations.get(anyString(), anyLong()))
                .willReturn(expected);
        // when
        Meal result = sut.findById(mealId);
        // then
        verify(redisTemplate, only()).opsForHash();
        verify(hashOperations, only()).get(REDIS_KEY, mealId);

        assertThat(result, notNullValue());
        assertThat(result, sameInstance(expected));
    }

    @Test
    public void testFindByIdWhenRedisNotHaveData() {
        // given
        final Long mealId = 1L;
        final Meal expected = new Meal();

        given(redisTemplate.opsForHash())
                .willReturn(hashOperations);
        given(hashOperations.get(anyString(), anyLong()))
                .willReturn(null);
        given(mealRepository.findOne(anyLong()))
                .willReturn(expected);
        // when
        Meal result = sut.findById(mealId);
        // then
        verify(redisTemplate, only()).opsForHash();
        verify(hashOperations, only()).get(REDIS_KEY, mealId);
        verify(mealRepository, only()).findOne(mealId);

        assertThat(result, notNullValue());
        assertThat(result, sameInstance(expected));
    }

    @Test
    public void testDeleteById() {
        // given
        final Long mealId = 1L;

        given(redisTemplate.opsForHash())
                .willReturn(hashOperations);
        // when
        sut.deleteById(mealId);
        // then
        verify(mealRepository, only()).delete(mealId);
        verify(redisTemplate, only()).opsForHash();
        verify(hashOperations, only()).delete(REDIS_KEY, mealId);
    }
}
