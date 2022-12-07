package com.healthcare.integration.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.healthcare.EntityFactory;
import com.healthcare.model.entity.Agency;
import com.healthcare.model.entity.AgencyType;
import com.healthcare.model.entity.Company;
import com.healthcare.model.entity.User;
import com.healthcare.model.entity.User;
import com.healthcare.model.entity.Visit;
import com.healthcare.pagination.CustomPageRequest;
import com.healthcare.service.UserService;
import com.healthcare.util.DateUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceTest extends EntityFactory {
	@Autowired
	private UserService userService;
	
	@Autowired
	private RedisTemplate<String, User> redisTemplate;

	@Value("${spring.data.rest.default-page-size}")
	private int defaultMaxSize;

	@Before
	public void setup() {
		init();
		id=0L;
		redisTemplate.delete(User.class.getSimpleName());
	}

	private Long id = 0L;

	@After
	public void rollback() {
		if(id!=0L)
			userService.deleteById(id);
	}

	@Test
	public void shouldSaveAUser() {
		User user = createNewUser();
		userService.save(user);
		Assert.assertNotNull(user.getId());
		id = user.getId();
	}

	@Test
	public void shouldGetAUser() {
		User user = createNewUser();
		userService.save(user);
		Assert.assertNotNull(userService.findById(user.getId()));
		id = user.getId();

	}

	@Test
	public void shouldUpdateAUser() {
		String newPhone = "5967897788";
		String newAddress = "Av. 57 y 23 St.";

		User user = createNewUser();
		userService.save(user);
		Assert.assertEquals(user.getPhone(), phone);
		Assert.assertEquals(user.getAddressOne(), addressOne);
		User userSaved = userService.findById(user.getId());
		userSaved.setPhone(newPhone);
		userSaved.setAddressOne(newAddress);
		userService.save(userSaved);
		User userMofified = userService.findById(user.getId());
		Assert.assertEquals(userMofified.getPhone(), newPhone);
		Assert.assertEquals(userMofified.getAddressOne(), newAddress);
		id = userMofified.getId();

	}

	@Test
	public void shouldDeleteAUser() {
		User user = createNewUser();
		userService.save(user);
		Assert.assertNotNull(user.getId());
		userService.deleteById(user.getId());
		Assert.assertNull(userService.findById(user.getId()));
	}

	@Test
	public void souldFindAll() {
		User user = createNewUser();
		user = userService.save(user);

		User user1= createNewUser();
		user1 = userService.save(user1);

		User user2= createNewUser();
		user2 = userService.save(user2);

		List<User> list= userService.findAll();
		assertNotNull(list);
		assertEquals(3, list.size());


		id=user.getId();
		rollback();
		id=user1.getId();
		rollback();
		id=user2.getId();
	}

	@Test
	public void shouldDisableAUser() {
		User user = createNewUser();
		userService.save(user);
		Assert.assertNotNull(user.getId());
		Long disableUserId = userService.disableById(user.getId());
		Assert.assertNotNull(disableUserId);
		User disableUser = userService.findById(disableUserId);
		Assert.assertNotNull(disableUser.getId());
		Assert.assertEquals(0, disableUser.getStatus());
	}
	
	
	@Test
	public void testFindAllPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			User obj = userService.save(createNewUser());
			listIds.add(obj.getId());
		}

		// PAGE 0
		// given
		Pageable pageable = new CustomPageRequest();
		// when
		Page<User> result = userService.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == defaultMaxSize);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 0);

		// PAGE 2
		pageable = new PageRequest(1, 5);
		// when
		result = userService.findAll(pageable);
		// then
		assertTrue(result.getContent().size() == 5);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 1);

		// delete created data
		for (Long id : listIds) {
			userService.deleteById(id);
		}
	}

	@Test
	public void testSearchWithPagination() {
		List<Long> listIds = new ArrayList<Long>();
		for (int i = 0; i < 25; i++) {
			User obj = userService.save(createNewUser());
			listIds.add(obj.getId());
		}
		// SEARCH 1 : FIND BY ID
		// given
		Pageable pageable = new PageRequest(0, 25);
		User obj = new User();
		obj.setId(listIds.get(0));

		// when
		Page<User> result = userService.findAll(obj, pageable);
		// then
		assertTrue(result.getContent().size() == 1);
		assertTrue(result.getTotalElements() >= 1);
		assertTrue(result.getTotalPages() >= 1);
		assertTrue(result.getNumber() == 0);

		// SEARCH 2
		// given
		pageable = new PageRequest(3, 5);
		obj = new User();

		// when
		result = userService.findAll(obj, pageable);
		// then
		assertTrue(result.getContent().size() == 5);
		assertTrue(result.getTotalElements() >= 25);
		assertTrue(result.getNumber() == 3);

		// Delete created data
		for (Long id : listIds) {
			userService.deleteById(id);
		}
	}
}
