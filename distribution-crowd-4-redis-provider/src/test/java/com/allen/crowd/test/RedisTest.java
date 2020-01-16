package com.allen.crowd.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Test
	public void testRedisTemplate() {
		//redisTemplate.opsForValue().set("wang", "red");
		redisTemplate.delete("您好，您的验证码是：15579596976");
	}
	
	@Test
	public void testStringRedisTemplate() {
		stringRedisTemplate.opsForValue().set("oldboy", "oleYear");
	}
}
