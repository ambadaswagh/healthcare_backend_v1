package com.healthcare.integration.service;

import java.sql.Timestamp;
import java.util.Calendar;

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

import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.AdminPost;
import com.healthcare.repository.AdminPostRepository;
import com.healthcare.service.AdminPostService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AdminPostServiceRedisTest {
	@Autowired
	private AdminPostService adminPostService;

	@MockBean
	private AdminPostRepository adminPostRepository;

	@Autowired
	private TestEntityServiceFactory testEntitySericeFactory;
	
	Calendar postDate = Calendar.getInstance();
	String postText = "This is post text";

	private Admin admin=null;
	private AdminPost adminPost =null;
	private Long id = 7L;
	
	@Before
	public void setup() {
		testEntitySericeFactory.saveData = true;
		admin  = testEntitySericeFactory.createAdmin();
    	adminPost = null;
	}
	
	@After
	public void rollback() {
		if(adminPost!=null){
			adminPostService.deleteById(adminPost.getId());
		}
		testEntitySericeFactory.redisCleanUp();
	}

	
	@Test
	public void testSaveAdminPost() {
		adminPost = createNewAdminPost();
		adminPost.setId(id);
		Mockito.when(adminPostRepository.save(adminPost)).thenReturn(adminPost);
		adminPostService.save(adminPost);
		AdminPost savedAdminPost = adminPostService.findById(adminPost.getId());
		Assert.assertNotNull(savedAdminPost);
	}

	@Test
	public void testUpdateAdminPost() {
		String newPostText = "This is new post text";

		adminPost = createNewAdminPost();
		adminPost.setId(id);
		Mockito.when(adminPostRepository.save(adminPost)).thenReturn(adminPost);
		adminPostService.save(adminPost);
		AdminPost savedAdminPost = adminPostService.findById(adminPost.getId());
		savedAdminPost.setPostText(newPostText);
		Mockito.when(adminPostRepository.save(savedAdminPost)).thenReturn(savedAdminPost);
		adminPostService.save(savedAdminPost);
		AdminPost adminPostMofified = adminPostService.findById(adminPost.getId());
		Assert.assertEquals(adminPostMofified.getPostText(), newPostText);
	}

	@Test
	public void testDeleteAdminPost() {
		AdminPost adminPost = createNewAdminPost();
		adminPost.setId(id);
		Mockito.when(adminPostRepository.save(adminPost)).thenReturn(adminPost);
		adminPostService.save(adminPost);
		Mockito.doNothing().when(adminPostRepository).delete(10L);
		Assert.assertNotNull(adminPostService.deleteById(adminPost.getId()));
	}

	private AdminPost createNewAdminPost() {
		AdminPost adminPost = new AdminPost();
		adminPost.setPostDate(new Timestamp(postDate.getTimeInMillis()));
		adminPost.setPostText(postText);
		adminPost.setStatus(1);
		adminPost.setAdmin(admin);
		return adminPost;
	}

}
