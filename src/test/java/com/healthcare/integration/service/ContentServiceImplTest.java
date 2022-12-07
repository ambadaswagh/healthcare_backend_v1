package com.healthcare.integration.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

import java.util.Base64;
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
import com.healthcare.model.entity.Content;
import com.healthcare.service.ContentService;

@RunWith(SpringRunner.class)
@TestExecutionListeners(
        value = {DbUnitTestExecutionListener.class},
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@DatabaseSetup(value = "/dataset/service/ContentServiceImplIntegrationTest.xml")
@ContextConfiguration(classes = {DbUnitIntegrationTestConfiguration.class})
@Transactional
@SpringBootTest
public class ContentServiceImplTest {

    @Autowired
    private ContentService contentService;

    @Autowired
    private EntityManager em;

    private Content content;
    @Before
    public void setup() {
    	content = null;
    }
    @After
    public void rollback(){
    	if(content!=null)
    		contentService.deleteById(content.getId());
    }
    
    @Test
	public void testCreateContent() {
		// given
		final Content contentInput = getContent();
		
		// when
		content = contentService.save(contentInput);
		// then
		assertThat(content, notNullValue());
		assertThat(content.getId(), notNullValue());
	}

   @Test
    @ExpectedDatabase(
            value = "/dataset/service/ContentServiceImplIntegrationTest.testUpdate.expected.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    public void testUpdateContent() {
       String dbTiltle = "test title";
       String dbPageTitle = "login user";
       
       String updatedTiltle = "test title updated";
       String updatePageTitle = "login user updated";
       
	   // given
		content = contentService.findById(100L);
		assertEquals(dbTiltle, content.getTitle());
		//assertEquals(dbPageTitle, content.getPageTitle());
		
		content.setTitle(updatedTiltle);
		//content.setPageTitle(updatePageTitle);
		
       // when
        Content result = contentService.save(content);
        // then
        assertThat(result, notNullValue());
        assertEquals(updatedTiltle, result.getTitle());
        //assertEquals(updatePageTitle, result.getPageTitle());
        
		assertThat(result.getId() , is(100L));
	    em.flush();
    }

    @Test
    public void testFindById() {
        // given
        final Long id = 100L;
        // when
        Content result = contentService.findById(id);
        // then
        assertThat(result, notNullValue());
    }

    @Test
    public void testFindAll() {
        // when
        List<Content> result = contentService.findAll();
        // then
        assertTrue(result.size()>0);
    }
    
    @Test
    @ExpectedDatabase(
            value = "/dataset/service/ContentServiceImplIntegrationTest.testDelete.expected.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    public void testDeleteById() {
        // given
        final Long id = 100L;
        // when
        contentService.deleteById(id);
        // then
        em.flush();
    }    
    
    private Content getContent() {
		final Content content = new Content();
        content.setId(1L);
        //content.setAccessKey("New Acess Key");
        content.setContent(Base64.getEncoder().encode("SampleString".getBytes()));
        //content.setPageDescription("page desc");
        //content.setPageKeyword("page keyword");
        content.setTitle("title");
        //content.setPageTitle("page title");
        content.setStatus(1);
        return content;
	}

	
}