package com.healthcare.integration.service;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.only;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.healthcare.model.entity.Meal;
import com.healthcare.repository.MealRepository;
import com.healthcare.service.MealService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MealServiceImplRedisTest {

    @Autowired
    private MealService sut;

    @MockBean
    private MealRepository mealRepository;


	private Long id = 1L;

	@After
	public void rollback() {
		sut.deleteById(id);
	}

    @Test
    public void testCreate() {
        // given
        final Long mealId = id;
        final Meal meal = new Meal();
        meal.setId(mealId);

        given(mealRepository.save(any(Meal.class)))
                .willReturn(meal);
        // when
        sut.save(meal);
        // then
        verify(mealRepository, only()).save(meal);

        Meal result = sut.findById(meal.getId());

        assertThat(result, notNullValue());
    }

    @Test
    public void testUpdate() {
        // given
        final Long mealId = id;
        final String name = "Meal name";
        final Meal meal = new Meal();
        meal.setId(mealId);

        given(mealRepository.save(any(Meal.class)))
                .willReturn(meal);
        sut.save(meal);
        meal.setName(name);
        // when
        sut.save(meal);
        // then
        verify(mealRepository, atLeast(1)).save(meal);

        Meal result = sut.findById(meal.getId());

        assertThat(result, notNullValue());
        assertThat(result.getName(), equalTo(name));
    }

    @Test
    public void testDeleteById() {
        // given
        final Long mealId = id;
        final Meal meal = new Meal();
        meal.setId(mealId);

        given(mealRepository.save(any(Meal.class)))
                .willReturn(meal);
        sut.save(meal);
        // when
        Long result = sut.deleteById(meal.getId());
        // then
        verify(mealRepository).delete(meal.getId());
        assertThat(result, notNullValue());

        Meal savedMeal = sut.findById(meal.getId());

        assertThat(savedMeal, nullValue());
    }

    @Test
	public void shouldFindAllMeals() {
    	Meal meal = new Meal();
    	meal.setId(1L);
		Mockito.when(mealRepository.save(meal)).thenReturn(meal);
		meal = sut.save(meal);

		Meal meal1= new Meal();
		meal1.setId(2L);
		Mockito.when(mealRepository.save(meal1)).thenReturn(meal1);
		meal1 = sut.save(meal1);
		
		Meal meal2= new Meal();
		meal2.setId(3L);
		Mockito.when(mealRepository.save(meal2)).thenReturn(meal2);
		meal2 = sut.save(meal2);

		List<Meal> list= sut.findAll();
		assertNotNull(list);
		assertEquals(3, list.size());

		id=meal.getId();
		rollback();
		id=meal1.getId();
		rollback();
		id=meal2.getId();
		rollback();
	}

    @Test
    public void testDisableById() {
        final Long mealId = id;
        final Meal meal = new Meal();
        meal.setId(mealId);
        Mockito.when(mealRepository.save(meal)).thenReturn(meal);
        sut.save(meal);
        Meal savedMeal = sut.findById(mealId);
        savedMeal.setStatus(0);
        Mockito.when(mealRepository.save(savedMeal)).thenReturn(savedMeal);
        Long disableMealId = sut.disableById(savedMeal.getId());
        Assert.assertNotNull(disableMealId);
        Meal disableMeal = sut.findById(disableMealId);
        Assert.assertNotNull(disableMeal.getId());
        Assert.assertEquals(0, disableMeal.getStatus().intValue());
    }
}
