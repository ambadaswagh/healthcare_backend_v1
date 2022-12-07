package com.healthcare;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.healthcare.model.entity.User1;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisApplicationTests {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private RedisTemplate<String, User1> redisTemplate;

	@Test
	public void test() throws Exception {

		// save string
		stringRedisTemplate.opsForValue().set("aaa", "111");
		Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));

		// save object
		User1 user = new User1("Test20", 20);
		redisTemplate.opsForValue().set(user.getUsername(), user);

		user = new User1("Test30", 30);
		redisTemplate.opsForValue().set(user.getUsername(), user);

		user = new User1("Test40", 40);
		redisTemplate.opsForValue().set(user.getUsername(), user);

		Assert.assertEquals(20, redisTemplate.opsForValue().get("Test20").getAge().longValue());
		Assert.assertEquals(30, redisTemplate.opsForValue().get("Test30").getAge().longValue());
		Assert.assertEquals(40, redisTemplate.opsForValue().get("Test40").getAge().longValue());

	}

}