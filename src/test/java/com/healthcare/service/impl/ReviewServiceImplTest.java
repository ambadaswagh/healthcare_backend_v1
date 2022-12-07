package com.healthcare.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.healthcare.model.entity.Review;
import com.healthcare.repository.ReviewRepository;
import com.healthcare.service.ReviewService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.only;

@RunWith(MockitoJUnitRunner.class)
public class ReviewServiceImplTest {

    private static final String REDIS_KEY = Review.class.getSimpleName();

    private ReviewService sut;

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private RedisTemplate redisTemplate;
    @Mock
    private HashOperations hashOperations;

    @Before
    public void setUp() {
        sut = new ReviewServiceImpl(reviewRepository, redisTemplate);
    }

    @Test
    public void testSave() {
        // given
        final Long reviewId = 1L;
        final Review review = new Review();
        final Review expected = new Review();
        expected.setId(reviewId);

        given(reviewRepository.save(any(Review.class)))
                .willReturn(expected);
        given(redisTemplate.opsForHash())
                .willReturn(hashOperations);
        // when
        Review result = sut.save(review);
        // then
        verify(reviewRepository, only()).save(review);
        verify(redisTemplate, only()).opsForHash();
        verify(hashOperations, only()).put(REDIS_KEY, reviewId, expected);

        assertThat(result, notNullValue());
        assertThat(result, sameInstance(expected));
    }

    @Test
    public void testFindByIdWhenRedisHaveData() {
        // given
        final Long reviewId = 1L;
        final Review expected = new Review();

        given(redisTemplate.opsForHash())
                .willReturn(hashOperations);
        given(hashOperations.get(anyString(), anyLong()))
                .willReturn(expected);
        // when
        Review result = sut.findById(reviewId);
        // then
        verify(redisTemplate, only()).opsForHash();
        verify(hashOperations, only()).get(REDIS_KEY, reviewId);

        assertThat(result, notNullValue());
        assertThat(result, sameInstance(expected));
    }

    @Test
    public void testFindByIdWhenRedisNotHaveData() {
        // given
        final Long reviewId = 1L;
        final Review expected = new Review();

        given(redisTemplate.opsForHash())
                .willReturn(hashOperations);
        given(hashOperations.get(anyString(), anyLong()))
                .willReturn(null);
        given(reviewRepository.findOne(anyLong()))
                .willReturn(expected);
        // when
        Review result = sut.findById(reviewId);
        // then
        verify(redisTemplate, only()).opsForHash();
        verify(hashOperations, only()).get(REDIS_KEY, reviewId);
        verify(reviewRepository, only()).findOne(reviewId);

        assertThat(result, notNullValue());
        assertThat(result, sameInstance(expected));
    }

    @Test
    public void testDeleteById() {
        // given
        final Long reviewId = 1L;

        given(redisTemplate.opsForHash())
                .willReturn(hashOperations);
        // when
        sut.deleteById(reviewId);
        // then
        verify(reviewRepository, only()).delete(reviewId);
        verify(redisTemplate, only()).opsForHash();
        verify(hashOperations, only()).delete(REDIS_KEY, reviewId);
    }

    @Test
    public void testFindAllWhenRedisHaveData() {
        // given
        final Review review = new Review();
        final Map<String, Review> reviewMap = Maps.newHashMap();
        reviewMap.put("key", review);

        given(redisTemplate.opsForHash())
                .willReturn(hashOperations);
        given(hashOperations.entries(anyString()))
                .willReturn(reviewMap);
        // when
        List<Review> result = sut.findAll();
        // then
        verify(redisTemplate, only()).opsForHash();
        verify(hashOperations, only()).entries(REDIS_KEY);

        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(reviewMap.size()));
        assertThat(result, hasItem(review));
    }

    @Test
    public void testFindAllWhenRedisNotHaveData() {
        // given
        final Review review = new Review();
        final List<Review> expected = Lists.newArrayList(review);

        given(redisTemplate.opsForHash())
                .willReturn(hashOperations);
        given(hashOperations.entries(anyString()))
                .willReturn(Maps.newHashMap());
        given(reviewRepository.findAll())
                .willReturn(expected);
        // when
        List<Review> result = sut.findAll();
        // then
        verify(redisTemplate, only()).opsForHash();
        verify(hashOperations, only()).entries(REDIS_KEY);
        verify(reviewRepository, only()).findAll();

        assertThat(result, notNullValue());
        assertThat(result, sameInstance(expected));
        assertThat(result, hasItem(review));
    }
}
