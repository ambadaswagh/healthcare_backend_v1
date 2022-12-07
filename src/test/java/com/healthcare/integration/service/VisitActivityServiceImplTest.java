package com.healthcare.integration.service;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.healthcare.DbUnitIntegrationTestConfiguration;
import com.healthcare.model.entity.VisitActivity;
import com.healthcare.model.entity.VisitActivityPK;
import com.healthcare.service.VisitActivityService;

@RunWith(SpringRunner.class)
@TestExecutionListeners(
        value = {DbUnitTestExecutionListener.class},
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@DatabaseSetup(value = "/dataset/service/VisitActivityServiceImplIntegrationTest.xml")
@ContextConfiguration(classes = {DbUnitIntegrationTestConfiguration.class})
@Transactional
@SpringBootTest
public class VisitActivityServiceImplTest {

    @Autowired
    private VisitActivityService visitActivityService;
    
    @Autowired
	private RedisTemplate<String, VisitActivity> redisTemplate;

    @Autowired
    private EntityManager em;

    private Long visitId = 100L;
	private Long activityId = 100L;
	boolean isDeleted = false;

	@Before
	public void setup() {
		redisTemplate.delete(VisitActivity.class.getSimpleName());
	}
	
	@After
	public void rollback() {
		if(!isDeleted)
			visitActivityService.deleteById(getPk(visitId, activityId));
	}
	
	private VisitActivityPK getPk(Long visitId,Long activityId) {
		return new VisitActivityPK(visitId,activityId);
	}
    
    @Test
	public void testCreateVisitActivity() {
		// given
		final VisitActivity visitActivity = new VisitActivity();
		visitActivity.setActivityId(activityId);
		visitActivity.setVisitId(visitId);
		
		// when
		VisitActivity result = visitActivityService.save(visitActivity);
		// then
		assertThat(result, notNullValue());
		assertThat(result.getActivityId(), notNullValue());
		assertThat(result.getVisitId(), notNullValue());
	}

   @Test
    @ExpectedDatabase(
            value = "/dataset/service/VisitActivityServiceImplIntegrationTest.testUpdate.expected.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    public void testUpdateVisitActivity() {
       String dbTableName = "table name";
       String updatedTableName = "table name updated";
       
	   // given
		final VisitActivity visitActivity = visitActivityService.findById(getPk(visitId, activityId));
		assertEquals(dbTableName, visitActivity.getTableName());
		
		visitActivity.setTableName(updatedTableName);
		
       // when
        VisitActivity result = visitActivityService.save(visitActivity);
        // then
        assertThat(result, notNullValue());
        assertEquals(updatedTableName, result.getTableName());
		
        em.flush();
    }

    @Test
    public void testFindById() {
        // given
        final VisitActivityPK pk  = getPk(visitId, activityId);
        // when
        VisitActivity result = visitActivityService.findById(pk);
        // then
        assertThat(result, notNullValue());
    }

    @Test
    public void testFindByActivityId() {
        // when
        List<VisitActivity> result = visitActivityService.findVisitActivityByActivityId(activityId);
        // then
        assertThat(result, notNullValue());
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getTableName(), "table name");
    }
    
    @Test
    public void testFindByVisitId() {
        // when
        List<VisitActivity> result = visitActivityService.findVisitActivityByVisitId(visitId);
        // then
        assertThat(result, notNullValue());
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getTableName(), "table name");
    }
    
    @Test
    @ExpectedDatabase(
            value = "/dataset/service/VisitActivityServiceImplIntegrationTest.testDelete.expected.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    
    public void testDeleteById() {
        // given
    	 final VisitActivityPK pk  = new VisitActivityPK(visitId,activityId);
         // when
        visitActivityService.deleteById(pk);
        // then
        em.flush();
        isDeleted = true;
    }

}