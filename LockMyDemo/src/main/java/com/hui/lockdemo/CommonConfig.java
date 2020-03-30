package com.hui.lockdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class CommonConfig {
    
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    
    @Bean
    public RedisTemplate<String,Object> redisTemplate() {
        
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        // 设置redis的连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置 key 序列化策略为 String 序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 设置 value 序列化为 jdk 自带的序列化策略
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        // 指定hash key 序列化策略为 String 序列化--针对hash 散列存储
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        
        return redisTemplate;
    }
    
    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        // 采用默认配置
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return stringRedisTemplate;
    }
}
