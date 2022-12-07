package com.healthcare.integration.service;

import java.util.Base64;

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

import com.healthcare.model.entity.Content;
import com.healthcare.repository.ContentRepository;
import com.healthcare.service.ContentService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ContentServiceRedisTest {
    @MockBean
    private ContentRepository contentRepository;

    @Autowired
    private ContentService contentService;

    
    private Long id = 100L;
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
    public void testSaveContentToRedisAndRetrievedItFromRedis() {
    	//given
		content = getContent();
	    content.setId(id);
	    
	    //Mock
        Mockito.when(contentRepository.save(content)).thenReturn(content);
        content = contentService.save(content);
        
        //Execute
        Content contentSaved = contentService.findById(id);
        
        //Assert
        Assert.assertNotNull(contentSaved);
    }

    @Test
    public void testUpdateContentToRedis() {
    	String pageDescUpdated = "page description updated";
    	
    	//given
		content = getContent();
	    content.setId(id);
        
	    //Mock
	    Mockito.when(contentRepository.save(content)).thenReturn(content);
        contentService.save(content);
        
        Content savedContentInRedis = contentService.findById(content.getId());
        //savedContentInRedis.setPageDescription(pageDescUpdated);
        
        Mockito.when(contentRepository.save(savedContentInRedis)).thenReturn(savedContentInRedis);
        contentService.save(savedContentInRedis);

        Content modifiedContentFromRedis = contentService.findById(content.getId());
        
        //Assert
        //Assert.assertEquals(modifiedContentFromRedis.getPageDescription(), pageDescUpdated);
    }

    @Test
    public void testDeleteContentFromRedis() {
    	//given
		final Content content = getContent();
	    content.setId(id);
	    
        Mockito.when(contentRepository.save(content)).thenReturn(content);
        contentService.save(content);
        
        Mockito.doNothing().when(contentRepository).delete(id);
        Assert.assertNotNull(contentService.deleteById(content.getId()));
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
