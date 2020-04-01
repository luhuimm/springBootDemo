package com.hui.redissondemo.message;

import com.hui.redissondemo.entity.UserLoginDto;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserLoginPublisher {
    
    // 构造基于发布订阅的主题key
    private static final String topicKey = "redissonUserLoginTopicKey";
    //注入客户端实例
    @Autowired
    private RedissonClient redissonClient;
    
    public void sendMsg(UserLoginDto dto) {
    
        RTopic rTopic = redissonClient.getTopic(topicKey);
        // 发布消息
        rTopic.publishAsync(dto);
    }
    
}
