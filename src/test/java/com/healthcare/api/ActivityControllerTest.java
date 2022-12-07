package com.healthcare.api;

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

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.model.entity.Activity;
import com.healthcare.service.ActivityService;

@RunWith(MockitoJUnitRunner.class)
public class ActivityControllerTest {

	@Mock
	private ActivityService activityService;

	private MockMvc mockMvc;

	private ObjectMapper objectMapper;

	@Before
	public void setUp() {
		ActivityController controller = new ActivityController(activityService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

		objectMapper = new ObjectMapper();
	}

	@Test
	public void testCreate() throws Exception {
		// given
		final String activityName = "Activity name";
		final Long activityId = 1L;
		final Activity activity = new Activity();
		activity.setName(activityName);
		final Activity expected = new Activity();
		expected.setId(activityId);

		given(activityService.save(any(Activity.class))).willReturn(expected);
		// when
		mockMvc.perform(post("/api/activity").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(activity))).andExpect(status().isOk())
				.andExpect(content().string(activityId.toString()));
		// then
		verify(activityService, only()).save(activity);
	}

	@Test
	public void testGet() throws Exception {
		// given
		final Long activityId = 1L;
		final Activity activity = new Activity();
		activity.setId(activityId);
		final String expectedContent = "{" + "\"createdAt\":null," + "\"updatedAt\":null," + "\"id\":" + activityId
				+ "," + "\"name\":null," + "\"status\":null," + "\"instructorEmployee\":null," + "\"timeStart\":null,"
				+ "\"timeEnd\":null," + "\"date\":null," + "\"location\":null," + "\"note\":null" + "}";

		given(activityService.findById(anyLong())).willReturn(activity);
		// when
		mockMvc.perform(get("/api/activity/" + activityId)).andExpect(status().isOk())
				.andExpect(content().string(expectedContent));
		// then
		verify(activityService, only()).findById(activityId);
	}

	@Test
	public void testGetWithIncorrectIdReturnBadRequestStatus() throws Exception {
		// given
		final String activityId = "abc";
		// given
		mockMvc.perform(get("/api/activity/" + activityId)).andExpect(status().isBadRequest());
		// then
		verifyZeroInteractions(activityService);
	}

	@Test
	public void testSave() throws Exception {
		// given
		final String activityName = "Activity name";
		final Long activityId = 1L;
		final Activity activity = new Activity();
		activity.setId(activityId);
		activity.setName(activityName);

		given(activityService.save(any(Activity.class))).willReturn(activity);
		// when
		mockMvc.perform(put("/api/activity").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(activity))).andExpect(status().isOk())
				.andExpect(content().string(isEmptyString()));
		// then
		verify(activityService, only()).save(activity);
	}

	@Test
	public void testDelete() throws Exception {
		// given
		final Long activityId = 1L;
		// when
		mockMvc.perform(delete("/api/activity/" + activityId)).andExpect(status().isOk())
				.andExpect(content().string(isEmptyString()));
		// then
		verify(activityService, only()).deleteById(activityId);
	}

	@Test
	public void testDeleteWithIncorrectIdReturnBadRequestStatus() throws Exception {
		// given
		final String activityId = "abc";
		// when
		mockMvc.perform(delete("/api/activity/" + activityId)).andExpect(status().isBadRequest());
		// then
		verifyZeroInteractions(activityService);
	}

	@Test
	public void testFindAllActivities() throws Exception {
		Mockito.when(activityService.findAll()).thenReturn(new ArrayList<Activity>());
		this.mockMvc.perform(get("/api/activity")).andExpect(status().isOk());
	}

	@Test
	public void testDisable() throws Exception {
		// given
		final Long activityId = 1L;
		// when
		mockMvc.perform(put("/api/activity/" + activityId + "/disable")).andExpect(status().isOk())
				.andExpect(content().string(isEmptyString()));
		// then
		verify(activityService, only()).disableById(activityId);
	}
}
