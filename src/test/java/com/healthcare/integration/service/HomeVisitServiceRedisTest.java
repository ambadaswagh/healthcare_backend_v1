package com.healthcare.integration.service;

import java.sql.Timestamp;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.healthcare.model.entity.CareGiver;
import com.healthcare.model.entity.HomeVisit;
import com.healthcare.model.entity.Meal;
import com.healthcare.model.entity.ServicePlan;
import com.healthcare.model.entity.User;
import com.healthcare.repository.HomeVisitRepository;
import com.healthcare.service.HomeVisitService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class HomeVisitServiceRedisTest {
    @MockBean
    private HomeVisitRepository homeVisitRepository;

    @Autowired
    private HomeVisitService homeVisitService;

    @Before
    public void setup() {
    }
    
    private Long id = 100L;
    @After
    public void rollback(){
    	homeVisitService.deleteById(id);
    }
    

    @Test
    public void testSaveHomeVisitToRedisAndRetrievedItFromRedis() {
    	//given
    	final Meal meal = getMeal();
		final ServicePlan serviceplan = getServicePlan();
		final User user = getUser();
		final CareGiver careGiver = getCareGiver();
		final HomeVisit homeVisit = getHomeVisit(meal, serviceplan, user, careGiver);
	    homeVisit.setId(id);
	    
	    //Mock
        Mockito.when(homeVisitRepository.save(homeVisit)).thenReturn(homeVisit);
        homeVisitService.save(homeVisit);
        
        //Execute
        HomeVisit homeVisitSaved = homeVisitService.findById(id);
        
        //Assert
        Assert.assertNotNull(homeVisitSaved);
    }

    @Test
    public void testUpdateHomeVisitToRedis() {
    	String notes = "notes updated in redis";
    	
    	//given
    	final Meal meal = getMeal();
		final ServicePlan serviceplan = getServicePlan();
		final User user = getUser();
		final CareGiver careGiver = getCareGiver();
		final HomeVisit homeVisit = getHomeVisit(meal, serviceplan, user, careGiver);
	    homeVisit.setId(id);
        
	    //Mock
	    Mockito.when(homeVisitRepository.save(homeVisit)).thenReturn(homeVisit);
        homeVisitService.save(homeVisit);
        
        HomeVisit savedHomeVisitInRedis = homeVisitService.findById(homeVisit.getId());
        savedHomeVisitInRedis.setNotes(notes);
        
        Mockito.when(homeVisitRepository.save(savedHomeVisitInRedis)).thenReturn(savedHomeVisitInRedis);
        homeVisitService.save(savedHomeVisitInRedis);

        HomeVisit modifiedHomeVisitFromRedis = homeVisitService.findById(homeVisit.getId());
        
        //Assert
        Assert.assertEquals(modifiedHomeVisitFromRedis.getNotes(), notes);
    }

    @Test
    public void testDeleteHomeVisitFromRedis() {
    	//given
    	final Meal meal = getMeal();
		final ServicePlan serviceplan = getServicePlan();
		final User user = getUser();
		final CareGiver careGiver = getCareGiver();
		final HomeVisit homeVisit = getHomeVisit(meal, serviceplan, user, careGiver);
	    homeVisit.setId(id);
        
        Mockito.doNothing().when(homeVisitRepository).delete(id);
        Assert.assertNotNull(homeVisitService.deleteById(homeVisit.getId()));
    }
    

    private HomeVisit getHomeVisit(final Meal meal,final ServicePlan serviceplan,final User user,final CareGiver careGiver) {
		final HomeVisit homeVisit = new HomeVisit();
        homeVisit.setCheckInTime(Timestamp.valueOf("2017-05-01 09:00:000"));
        homeVisit.setCareReceiverSignature("sign");
        homeVisit.setCheckOutTime(Timestamp.valueOf("2017-05-01 21:00:000"));
        homeVisit.setCareReceiverComments("comments");
        homeVisit.setNotes("notes");
        homeVisit.setStatus("verified");
        homeVisit.setCreatedAt(Timestamp.valueOf("2017-05-01 09:00:000"));
        homeVisit.setUpdatedAt(Timestamp.valueOf("2017-05-01 09:00:000"));
        homeVisit.setServiceplan(serviceplan);
        homeVisit.setUser(user);
        homeVisit.setCareGiver(careGiver);
        return homeVisit;
	}

	private Meal getMeal() {
		final Long mealId = 100L;
        final Meal meal = new Meal();
        meal.setId(mealId);
		return meal;
	}
	
	private User getUser() {
		final Long userId = 100L;
        final User user = new User();
        user.setId(userId);
		return user;
	}
	
	private ServicePlan getServicePlan() {
		final Long servicePlanId = 100L;
        final ServicePlan servicePlan = new ServicePlan();
        servicePlan.setId(servicePlanId);
		return servicePlan;
	}
	
	private CareGiver getCareGiver() {
		final Long careGiverId = 100L;
        final CareGiver careGiver = new CareGiver();
        careGiver.setId(careGiverId);
		return careGiver;
	}

}
