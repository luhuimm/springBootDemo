package com.hui.redissondemo.queue;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class QueuePublisher {
    @Autowired
    private RedissonClient redissonClient;

    public void sendBasicMsg(String msg) {
        final String queueName = "redissonBasicQueue";
        RQueue<String> rQueue = redissonClient.getQueue(queueName);
        rQueue.add(msg);
    }
}
