package com.hui.redisdemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hui.redisdemo.model.User;
import lombok.extern.slf4j.Slf4j;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;


@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RedisdemoApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;
    
    // 定义json 序列化和反序列化框架类
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void one() {
        final String content = "RedisTemplate实战字符串信息";
        final String key = "redis:template:one:string";
        // 将字符串信息写入
        redisTemplate.opsForValue().set(key,content);
        log.info("写入的内容是:{}",content);
        Object result = redisTemplate.opsForValue().get(key);
        log.info("读出来的内容是:{}",result);
    }
    
    // 使用redisTemplate 将对象信息序列化为josn 格式字符串，后写入缓存中
    // 然后将其读出，最后反序列化解析其中内容，并展示在控制台
    @Test
    public void two() throws JsonProcessingException {
        User user = new User(1,"debug","辉哥");
        ValueOperations valueOperations = redisTemplate.opsForValue();
        final String key = "redis:template:two:object";
        final String content = objectMapper.writeValueAsString(user);
        valueOperations.set(key,content);
        log.info("写入的内容是:{}",user);
        Object result = valueOperations.get(key);
        if (result != null) {
            User user1 = objectMapper.readValue(result.toString(),User.class);
            log.info("读出来的缓存内容并反序列化是:{}",result);
        }
    }
    
    @Test
    public void three() {
        final String content = "stringRedisTemplate 实战字符串信息";
        final String key = "redis:three";
        // 将字符串信息写入
        stringRedisTemplate.opsForValue().set(key,content);
        log.info("写入的内容是:{}",content);
        Object result = stringRedisTemplate.opsForValue().get(key);
        log.info("读出来的内容是:{}",result);
    }
    
    
    @Test
    public void four() throws JsonProcessingException {
        User user = new User(1,"redisstring","辉哥");
        ValueOperations valueOperations = stringRedisTemplate.opsForValue();
        final String key = "redis:four";
        final String content = objectMapper.writeValueAsString(user);
        valueOperations.set(key,content);
        log.info("写入的内容是:{}",user);
        Object result = valueOperations.get(key);
        if (result != null) {
            User user1 = objectMapper.readValue(result.toString(),User.class);
            log.info("读出来的缓存内容并反序列化是:{}",result);
        }
    }

}
