package com.healthcare.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.model.entity.Review;
import com.healthcare.service.ReviewService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.text.IsEmptyString.isEmptyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.internal.verification.VerificationModeFactory.only;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        ReviewController controller = new ReviewController(reviewService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreate() throws Exception {
        // given
        final Long reviewId = 1L;
        final Review review = new Review();
        final Review expected = new Review();
        expected.setId(reviewId);

        given(reviewService.save(any(Review.class)))
                .willReturn(expected);
        // when
        mockMvc.perform(
                    post("/api/review")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(review)))
                .andExpect(status().isOk())
                .andExpect(content().string(reviewId.toString()));
        // then
        verify(reviewService, only()).save(review);
    }

    @Test
    public void testGet() throws Exception {
        // given
        final Long reviewId = 1L;
        final Review review = new Review();
        review.setId(reviewId);
        final String expectedContent = "{" +
                    "\"id\":1" +
                "}";

        given(reviewService.findById(anyLong()))
                .willReturn(review);
        // when
        mockMvc.perform(
                    get("/api/review/" + reviewId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedContent));
        // then
        verify(reviewService, only()).findById(reviewId);
    }

    @Test
    public void testGetWithIncorrectIdReturnBadRequestStatus() throws Exception {
        // given
        final String reviewId = "abc";
        // given
        mockMvc.perform(
                    get("/api/review/" + reviewId))
                .andExpect(status().isBadRequest());
        // then
        verifyZeroInteractions(reviewService);
    }

    @Test
    public void testSave() throws Exception {
        // given
        final Integer reviewContent = 2;
        final Long reviewId = 1L;
        final Review review = new Review();
        review.setId(reviewId);

        given(reviewService.save(any(Review.class)))
                .willReturn(review);
        // when
        mockMvc.perform(
                    put("/api/review")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(review)))
                .andExpect(status().isOk())
                .andExpect(content().string(isEmptyString()));
        // then
        verify(reviewService, only()).save(review);
    }

    @Test
    public void testDelete() throws Exception {
        // given
        final Long reviewId = 1L;
        // when
        mockMvc.perform(
                    delete("/api/review/" + reviewId))
                .andExpect(status().isOk())
                .andExpect(content().string(isEmptyString()));
        // then
        verify(reviewService, only()).deleteById(reviewId);
    }

    @Test
    public void testDeleteWithIncorrectIdReturnBadRequestStatus() throws Exception {
        // given
        final String reviewId = "abc";
        // when
        mockMvc.perform(
                    delete("/api/review/" + reviewId))
                .andExpect(status().isBadRequest());
        // then
        verifyZeroInteractions(reviewService);
    }
}
