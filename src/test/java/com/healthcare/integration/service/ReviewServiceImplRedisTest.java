package com.healthcare.integration.service;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.only;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.healthcare.model.entity.Review;
import com.healthcare.repository.ReviewRepository;
import com.healthcare.service.ReviewService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReviewServiceImplRedisTest {

    @Autowired
    private ReviewService sut;

    @MockBean
    private ReviewRepository reviewRepository;


	private Long id = 1L;

	@After
	public void rollback() {
		sut.deleteById(id);
	}

    @Test
    public void testCreate() {
        // given
        final Long reviewId = id;
        final Review review = new Review();
        review.setId(reviewId);

        given(reviewRepository.save(any(Review.class)))
                .willReturn(review);
        // when
        sut.save(review);
        // then
        verify(reviewRepository, only()).save(review);

        Review result = sut.findById(review.getId());

        assertThat(result, notNullValue());
    }

    @Test
    public void testUpdate() {
        // given
        final Long reviewId = id;
        final String assessmentReason = "New Assessment Reason";
        final Review review = new Review();
        review.setId(reviewId);
        review.setAssessmentReason("Assessment Reason");

        given(reviewRepository.save(any(Review.class)))
                .willReturn(review);
        sut.save(review);
        review.setAssessmentReason(assessmentReason);
        // when
        sut.save(review);
        // then
        verify(reviewRepository, atLeast(1)).save(review);

        Review result = sut.findById(review.getId());

        assertThat(result, notNullValue());
        assertThat(result.getAssessmentReason(), equalTo(assessmentReason));
    }

    @Test
    public void testDeleteById() {
        // given
        final Long reviewId = id;
        final Review review = new Review();
        review.setId(reviewId);

        given(reviewRepository.save(any(Review.class)))
                .willReturn(review);
        sut.save(review);
        // when
        Long result = sut.deleteById(review.getId());
        // then
        verify(reviewRepository).delete(review.getId());
        assertThat(result, notNullValue());

        Review savedReview = sut.findById(review.getId());

        assertThat(savedReview, nullValue());
    }
}
