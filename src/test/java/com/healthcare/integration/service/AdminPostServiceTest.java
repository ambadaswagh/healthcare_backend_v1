package com.healthcare.integration.service;

import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.healthcare.model.entity.Admin;
import com.healthcare.model.entity.AdminPost;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.AdminPostService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AdminPostServiceTest {
	@Autowired
	private AdminPostService adminPostService;

	@Value("${spring.data.rest.default-page-size}")
	private int defaultMaxSize;
	
	Calendar postDate = Calendar.getInstance();
	String postText = "This is post text";

	private Admin admin;
	private AdminPost adminPost =null;
	
	@Autowired
	private TestEntityServiceFactory testEntitySericeFactory;
	
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
		adminPostService.save(adminPost);
		Assert.assertNotNull(adminPost.getId());
	}

	@Test
	public void testGetAdminPost() {
		adminPost = createNewAdminPost();
		adminPostService.save(adminPost);
		Assert.assertNotNull(adminPostService.findById(adminPost.getId()));
	}

	@Test
	public void testUpdateAdminPost() {
		String newPostText = "This is new post text";

		adminPost = createNewAdminPost();
		adminPostService.save(adminPost);
		Assert.assertEquals(adminPost.getPostText(), postText);
		AdminPost adminPostSaved = adminPostService.findById(adminPost.getId());
		adminPostSaved.setPostText(newPostText);
		adminPostService.save(adminPostSaved);
		AdminPost adminPostMofified = adminPostService.findById(adminPost.getId());
		Assert.assertEquals(adminPostMofified.getPostText(), newPostText);
	}

	@Test
	public void testDeleteAdminPost() {
		AdminPost adminPost = createNewAdminPost();
		adminPostService.save(adminPost);
		Assert.assertNotNull(adminPost.getId());
		adminPostService.deleteById(adminPost.getId());
		Assert.assertNull(adminPostService.findById(adminPost.getId()));
	}

	private AdminPost createNewAdminPost() {
		AdminPost adminPost = new AdminPost();
		adminPost.setPostDate(new Timestamp(postDate.getTimeInMillis()));
		adminPost.setPostText(postText);
		adminPost.setStatus(1);
		adminPost.setAdmin(admin);
		return adminPost;
	}
	

	@Test
	public void testFindAllPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			AdminPost adminPost = adminPostService.save(createNewAdminPost());
			listIds.add(adminPost.getId());
		}

		// PAGE 0
		// given
		Pageable pageable = new CustomPageRequest();
		// when
		Page<AdminPost> result = adminPostService.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == defaultMaxSize);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 0);

		// PAGE 2
		pageable = new PageRequest(1, 5);
		// when
		result = adminPostService.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == 5);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 1);

		// delete created data
		for (Long id : listIds) {
			adminPostService.deleteById(id);
		}
	}

	@Test
	public void testSearchWithPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			AdminPost admin = adminPostService.save(createNewAdminPost());
			listIds.add(admin.getId());
		}
		// SEARCH 1 : FIND BY ID
		// given
		Pageable pageable = new PageRequest(0, 25);
		AdminPost adminPostin = new AdminPost();
		adminPostin.setId(listIds.get(0));
		adminPostin.setStatus(1);

		// when
		Page<AdminPost> result = adminPostService.findAll(adminPostin, pageable);
		// then
		assertTrue(result.getContent().size() == 1);
		assertTrue(result.getTotalElements() >= 1);
		assertTrue(result.getTotalPages() >= 1);
		assertTrue(result.getNumber() == 0);

		// SEARCH 2
		// given
		pageable = new PageRequest(3, 5);
		adminPostin = new AdminPost();
		adminPostin.setPostText(postText);
		adminPostin.setStatus(1);

		// when
		result = adminPostService.findAll(adminPostin, pageable);
		// then
		assertTrue(result.getContent().size() == 5);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 3);

		// Delete created data
		for (Long id : listIds) {
			adminPostService.deleteById(id);
		}
	}
}

