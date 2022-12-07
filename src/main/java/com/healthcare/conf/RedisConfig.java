package com.healthcare.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.healthcare.util.RedisObjectSerializer;
/**
 *
 * @author zhao
 *
 */
@Configuration
public class RedisConfig {

	@Value("${spring.redis.host}")
	private String localhost;
	@Value("${spring.redis.port}")
	private int port;

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName(localhost);
		factory.setPort(port);
		return factory;
	}

	@SuppressWarnings("rawtypes")
	@Bean
	public RedisTemplate redisTemplate() {
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(jedisConnectionFactory());
		template.setHashValueSerializer(new RedisObjectSerializer());
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new RedisObjectSerializer());
//		template.setEnableTransactionSupport(true);
		return template;
	}

}
