package com.healthcare.integration.service;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.healthcare.DbUnitIntegrationTestConfiguration;
import com.healthcare.model.entity.Employee;
import com.healthcare.model.entity.Review;
import com.healthcare.model.entity.User;
import com.healthcare.model.entity.review.ActivityDetails;
import com.healthcare.model.entity.review.BodyStatus;
import com.healthcare.model.entity.review.CardPulmCondition;
import com.healthcare.model.entity.review.CommunicationHearingCondition;
import com.healthcare.model.entity.review.FunctionalStatus;
import com.healthcare.model.entity.review.HealthCondition;
import com.healthcare.model.entity.review.HealthConditionIndicators;
import com.healthcare.model.entity.review.HealthProblems;
import com.healthcare.model.entity.review.NutritionCondition;
import com.healthcare.model.entity.review.PainDetails;
import com.healthcare.model.entity.review.PsychologicalSocialCondition;
import com.healthcare.service.ReviewService;

@RunWith(SpringRunner.class)
@TestExecutionListeners(
        value = {DbUnitTestExecutionListener.class},
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@DatabaseSetup(value = "/dataset/service/ReviewServiceImplIntegrationTest.xml")
@ContextConfiguration(classes = {DbUnitIntegrationTestConfiguration.class})
@Transactional
@SpringBootTest
public class ReviewServiceImplTest {

    @Autowired
    private ReviewService sut;

    @Autowired
    private EntityManager em;
    
	Review result;
	
	@Before
	public void setup(){
		result = null;
	}
	
	@After
	public void rollback() {
		if(result!=null)
			sut.deleteById(result.getId());
	}


    @Test
    public void testCreate() {
        // given
        final Long employeeId = 100L;
        final Long userId = 100L;
        final Review review = new Review();
        review.setAssessmentReason("Assessment Reason");
        review.setAssessmentSourceInformation("Assessment Source Information");
        review.setAssessmentDate(new Timestamp(new Date().getTime()));
//        review.setState("State");

        final Employee employee = new Employee();
        employee.setId(employeeId);
//        review.setEmployee(employee);

        final User user = new User();
        user.setId(userId);
        review.setUser(user);
        // when
        result = sut.save(review);
        // then
        assertThat(result, notNullValue());
        assertThat(result.getId(), notNullValue());
    }

    @Test
    @ExpectedDatabase(
            value = "/dataset/service/ReviewServiceImplIntegrationTest.testUpdate.expected.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    public void testUpdate() {
        // given
        final Long reviewId = 100L;
        final Long employeeId = 100L;
        final Long userId = 100L;
        final Review review = new Review();
        review.setId(reviewId);
        review.setAssessmentReason("New Assessment Reason");
        review.setAssessmentSourceInformation("Assessment Source Information");
        review.setAssessmentDate(new Timestamp(new Date().getTime()));
//        review.setState("State");
        completeReview(review);

        final Employee employee = new Employee();
        employee.setId(employeeId);
//        review.setEmployee(employee);

        final User user = new User();
        user.setId(userId);
        review.setUser(user);
        // when
        result = sut.save(review);
        // then
        assertThat(result, notNullValue());

        em.flush();
    }

    @Test
    public void testGet() {
    	HealthCondition healthCondition = getHealthCondition();
    	final ActivityDetails activityDetails = new ActivityDetails();
        activityDetails.setAmbulation("Ambulation");
        
    	result = createReview(activityDetails,healthCondition, 100L);
        
        // given
        final Long reviewId = result.getId();
        // when
        Review result = sut.findById(reviewId);
        // then
        assertThat(result, notNullValue());
        assertThat(result.getId(), equalTo(reviewId));
        assertThat(result.getHealthCondition(), equalTo(healthCondition));
        assertThat(result.getActivityDetails(), equalTo(activityDetails));
    }
    
    @Test
    public void testGetByUser() {
    	HealthCondition healthCondition = getHealthCondition();
    	final ActivityDetails activityDetails = new ActivityDetails();
        activityDetails.setAmbulation("Ambulation");
        
    	Review review1 = createReview(activityDetails,healthCondition, 100L);
    	result = createReview(activityDetails,healthCondition, 100L);
       
        // when
    	List<Review> list = sut.findByUser(100L);
        // then
        assertEquals(list.size(), 2);
        
        sut.deleteById(review1.getId());
    }

	private Review createReview(ActivityDetails activityDetails,HealthCondition healthCondition, Long userId) {
    	final Long employeeId = 100L;
        final Review review = new Review();
        review.setAssessmentReason("Assessment Reason");
        review.setAssessmentSourceInformation("Assessment Source Information");
        review.setAssessmentDate(new Timestamp(new Date().getTime()));
//        review.setState("State");
        review.setHealthCondition(healthCondition);
        review.setActivityDetails(activityDetails);
        
        final Employee employee = new Employee();
        employee.setId(employeeId);
//        review.setEmployee(employee);

        final User user = new User();
        user.setId(userId);
        review.setUser(user);
        Review result = sut.save(review);
		return result;
	}

	private HealthCondition getHealthCondition() {
		final HealthCondition healthCondition = new HealthCondition();
        healthCondition.setSmoker(true);
        final HealthConditionIndicators healthConditionIndicators = new HealthConditionIndicators();
        healthConditionIndicators.setAlzheimer(true);
        healthCondition.setHealthConditionIndicators(healthConditionIndicators);
        final BodyStatus bodyStatus = new BodyStatus();
        bodyStatus.setComments("Comment");
        healthCondition.setBodyStatus(bodyStatus);
        final HealthProblems healthProblems = new HealthProblems();
        healthProblems.setAsthma(true);
        healthCondition.setHealthProblems(healthProblems);
		return healthCondition;
	}

    @Test
    @ExpectedDatabase(
            value = "/dataset/service/ReviewServiceImplIntegrationTest.testDelete.expected.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    public void testDelete() {
        // given
        final Long reviewId = 100L;
        // when
        sut.deleteById(reviewId);
        // then
        em.flush();
    }

    private void completeReview(Review review) {
        HealthCondition healthCondition = new HealthCondition();
        healthCondition.setSmoker(true);
        HealthConditionIndicators healthConditionIndicators = new HealthConditionIndicators();
        healthConditionIndicators.setAlzheimer(true);
        healthCondition.setHealthConditionIndicators(healthConditionIndicators);
        BodyStatus bodyStatus = new BodyStatus();
        bodyStatus.setComments("Comment");
        healthCondition.setBodyStatus(bodyStatus);
        HealthProblems healthProblems = new HealthProblems();
        healthProblems.setAsthma(true);
        healthCondition.setHealthProblems(healthProblems);
        review.setHealthCondition(healthCondition);

        CardPulmCondition cardPulmCondition = new CardPulmCondition();
        cardPulmCondition.setChestPain(true);
        review.setCardPulmCondition(cardPulmCondition);

        ActivityDetails activityDetails = new ActivityDetails();
        activityDetails.setAmbulation("Ambulation");
        review.setActivityDetails(activityDetails);

        PainDetails painDetails = new PainDetails();
        painDetails.setBodyLocation("Body location");
        review.setPainDetails(painDetails);

        FunctionalStatus functionalStatus = new FunctionalStatus();
        functionalStatus.setEating(false);
        review.setFunctionalStatus(functionalStatus);

        NutritionCondition nutritionCondition = new NutritionCondition();
        nutritionCondition.setAppetite("Appetite");
        review.setNutritionCondition(nutritionCondition);

        CommunicationHearingCondition communicationHearingCondition = new CommunicationHearingCondition();
        communicationHearingCondition.setQuality("Quality");
        review.setCommunicationHearingCondition(communicationHearingCondition);

        PsychologicalSocialCondition psychologicalSocialCondition = new PsychologicalSocialCondition();
        psychologicalSocialCondition.setAnger(false);
        review.setPsychologicalSocialCondition(psychologicalSocialCondition);
    }
}
