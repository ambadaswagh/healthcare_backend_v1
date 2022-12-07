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
import com.healthcare.model.entity.Meal;
import com.healthcare.service.MealService;

@RunWith(MockitoJUnitRunner.class)
public class MealControllerTest {

    @Mock
    private MealService mealService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        MealController controller = new MealController(mealService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreate() throws Exception {
        // given
        final String mealName = "Meal name";
        final Long mealId = 1L;
        final Meal meal = new Meal();
        meal.setName(mealName);
        final Meal expected = new Meal();
        expected.setId(mealId);

        given(mealService.save(any(Meal.class)))
                .willReturn(expected);
        // when
        mockMvc.perform(
                    post("/api/meal")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(meal)))
                .andExpect(status().isOk())
                .andExpect(content().string(mealId.toString()));
        // then
        verify(mealService, only()).save(meal);
    }

    @Test
    public void testGet() throws Exception {
        // given
        final Long mealId = 1L;
        final Meal meal = new Meal();
        meal.setId(mealId);
        final String expectedContent = "{" +
                    "\"createdAt\":null," +
                    "\"updatedAt\":null," +
                    "\"id\":" + mealId + "," +
                    "\"mealClass\":null," +
                    "\"name\":null," +
                    "\"ingredients\":null," +
                    "\"notes\":null," +
                    "\"verifiedByNutritionist\":null," +
                    "\"status\":0"+
                "}";

        given(mealService.findById(anyLong()))
                .willReturn(meal);
        // when
        mockMvc.perform(
                    get("/api/meal/" + mealId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedContent));
        // then
        verify(mealService, only()).findById(mealId);
    }

    @Test
    public void testGetWithIncorrectIdReturnBadRequestStatus() throws Exception {
        // given
        final String mealId = "abc";
        // given
        mockMvc.perform(
                    get("/api/meal/" + mealId))
                .andExpect(status().isBadRequest());
        // then
        verifyZeroInteractions(mealService);
    }

    @Test
    public void testSave() throws Exception {
        // given
        final String mealName = "Meal name";
        final Long mealId = 1L;
        final Meal meal = new Meal();
        meal.setId(mealId);
        meal.setName(mealName);

        given(mealService.save(any(Meal.class)))
                .willReturn(meal);
        // when
        mockMvc.perform(
                    put("/api/meal")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(meal)))
                .andExpect(status().isOk())
                .andExpect(content().string(isEmptyString()));
        // then
        verify(mealService, only()).save(meal);
    }

    @Test
    public void testDelete() throws Exception {
        // given
        final Long mealId = 1L;
        // when
        mockMvc.perform(
                    delete("/api/meal/" + mealId))
                .andExpect(status().isOk())
                .andExpect(content().string(isEmptyString()));
        // then
        verify(mealService, only()).deleteById(mealId);
    }

    @Test
    public void testDeleteWithIncorrectIdReturnBadRequestStatus() throws Exception {
        // given
        final String mealId = "abc";
        // when
        mockMvc.perform(
                delete("/api/meal/" + mealId))
                .andExpect(status().isBadRequest());
        // then
        verifyZeroInteractions(mealService);
    }

    @Test
    public void testDisable() throws Exception {
        // given
        final Long mealId = 1L;
        // when
        mockMvc.perform(
                put("/api/meal/" + mealId + "/disable"))
                .andExpect(status().isOk())
                .andExpect(content().string(isEmptyString()));
        // then
        verify(mealService, only()).disableById(mealId);
    }

    @Test
    public void testDisableWithIncorrectIdReturnBadRequestStatus() throws Exception {
        // given
        final String mealId = "abc";
        // when
        mockMvc.perform(
                put("/api/meal/" + mealId + "/disable"))
                .andExpect(status().isBadRequest());
        // then
        verifyZeroInteractions(mealService);
    }

	@Test
	public void testFindAllMeals() throws Exception {
		Mockito.when(mealService.findAll()).thenReturn(new ArrayList<Meal>());
		this.mockMvc.perform(get("/api/meal")).andExpect(status().isOk());
	}
}
