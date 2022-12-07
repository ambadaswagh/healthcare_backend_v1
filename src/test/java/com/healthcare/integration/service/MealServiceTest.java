package com.healthcare.integration.service;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.transaction.Transactional;

import org.hamcrest.collection.IsEmptyCollection;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.healthcare.model.entity.Activity;
import com.healthcare.model.entity.Agency;
import com.healthcare.model.entity.AgencyType;
import com.healthcare.model.entity.Company;
import com.healthcare.model.entity.Employee;
import com.healthcare.model.entity.Meal;
import com.healthcare.model.entity.ServicePlan;
import com.healthcare.model.entity.User;
import com.healthcare.model.entity.Visit;
import com.healthcare.service.AgencyService;
import com.healthcare.service.AgencyTypeService;
import com.healthcare.service.CompanyService;
import com.healthcare.service.EmployeeService;
import com.healthcare.service.MealService;
import com.healthcare.service.ServicePlanService;
import com.healthcare.service.UserService;
import com.healthcare.service.VisitService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MealServiceTest {

    @Autowired
    private MealService mealService;

    @Autowired
    private VisitService visitService;

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private AgencyTypeService agencyTypeService;

    @Autowired
    private ServicePlanService servicePlanService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserService userService;



    private Meal meal;
    private Visit visit;
    private Agency agency;
    private Company company;
    private AgencyType agencyType;
    private ServicePlan servicePlan;
    private Employee employee;
    private User user;
    
    @Before
    public void setup() {
    	company = companyService.save(TestEntityFactory.createNewCompany());
    	agencyType = agencyTypeService.save(TestEntityFactory.createNewAgencyType());
    	agency = agencyService.save(TestEntityFactory.createNewAgency(company, agencyType));
        user =  userService.save(TestEntityFactory.createNewUser());
        employee = employeeService.save(TestEntityFactory.createNewEmployee(agency));
        servicePlan = servicePlanService.save(TestEntityFactory.createNewServicePlan(employee,user));
    	visit = visitService.save(TestEntityFactory.createNewVisit(user,agency,servicePlan));
        meal =null;
    }

	@After
	public void rollback() {
		if(meal!=null){
			mealService.deleteById(meal.getId());
		}
		visitService.deleteById(visit.getId());
		servicePlanService.deleteById(servicePlan.getId());
		employeeService.deleteById(employee.getId());
		userService.deleteById(user.getId());
        agencyService.deleteById(agency.getId());
        agencyTypeService.deleteById(agencyType.getId());
        companyService.deleteById(company.getId());
	}

    /**
     * Meal
     */
    String mealClass = "mealClass";
    String ingredients = "ingredients";
    String notes = "notes";
    Integer verifiedByNutritionist = 1;
    String name = "name";
    int status = 1;
   
    @Test
    public void testSaveMeal() {
        meal = createNewMeal(visit);
        mealService.save(meal);
        Assert.assertNotNull(meal.getId());
    }

    @Test
    public void testGetMeal() {
        meal = createNewMeal(visit);
        mealService.save(meal);
        Assert.assertNotNull(mealService.findById(meal.getId()));
    }

    @Test
    public void testUpdateMeal() {
        String newMealClass = "mealClass 2";
        String newIngredients = "ingredients";

        meal = createNewMeal(visit);
        mealService.save(meal);
        Assert.assertEquals(meal.getMealClass(), mealClass);
        Assert.assertEquals(meal.getIngredients(), ingredients);
        Meal mealSaved = mealService.findById(meal.getId());
        mealSaved.setMealClass(newMealClass);
        mealSaved.setIngredients(newIngredients);
        mealService.save(mealSaved);
        Meal mealMofified = mealService.findById(meal.getId());
        Assert.assertEquals(mealMofified.getMealClass(), newMealClass);
        Assert.assertEquals(mealMofified.getIngredients(), newIngredients);
    }

    @Test
    public void testDeleteMeal() {
        Meal meal = createNewMeal(visit);
        mealService.save(meal);
        Assert.assertNotNull(meal.getId());
        mealService.deleteById(meal.getId());
        Assert.assertNull(mealService.findById(meal.getId()));
    }

    @Test
    public void testFindAllMeals() {
    	//given
    	meal = createNewMeal(visit);
    	mealService.save(meal);
    	List<Meal> expectedList = new ArrayList<>();
    	expectedList.add(meal);
    	
        // when
        List<Meal> result = mealService.findAll();
       
        // then
        assertTrue(result.size() > 0);
    	//assert not null
    	assertThat(result, notNullValue());
    	//assert is expected
    	assertThat(result, is(expectedList));
        //assert has item
        assertThat(result, hasItems(meal));
        //assert size
        assertThat(result, hasSize(1));	
        //check empty list
        assertThat(result, not(IsEmptyCollection.empty()));
    }

    @Test
    public void testDisableMeal() {
        meal = createNewMeal(visit);
        mealService.save(meal);
        Assert.assertNotNull(meal.getId());
        Long disableMealId = mealService.disableById(meal.getId());
        Assert.assertNotNull(disableMealId);
        Meal disableMeal = mealService.findById(disableMealId);
        Assert.assertNotNull(disableMeal.getId());
        Assert.assertEquals(0, disableMeal.getStatus().intValue());
    }

    private Meal createNewMeal(Visit visit) {
        Meal meal = new Meal();
        meal.setMealClass(mealClass);
        meal.setName(name);
        meal.setIngredients(ingredients);
        meal.setNotes(notes);
        meal.setVerifiedByNutritionist(verifiedByNutritionist);
        meal.setStatus(status);
       
        return meal;
    }

}
